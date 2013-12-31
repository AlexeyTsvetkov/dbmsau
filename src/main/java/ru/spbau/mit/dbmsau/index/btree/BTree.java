package ru.spbau.mit.dbmsau.index.btree;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.ContextContainer;
import ru.spbau.mit.dbmsau.pages.Page;
import ru.spbau.mit.dbmsau.relation.Relation;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.relation.Type;
import ru.spbau.mit.dbmsau.table.TableRecord;

import java.util.ArrayList;

public class BTree extends ContextContainer {
    public static final int NODE_ID_SIZE = 4; // int size
    private int rootId;
    private int keySize, valSize;
    private Type[] keyType, valType;
    int size;

    public BTree(Type[] keyType, Type[] valType, int rootId, Context context) {
        super(context);

        this.keyType = keyType;
        this.valType = valType;

        keySize = valSize = 0;
        for (Type aKeyType : keyType) {
            keySize += aKeyType.getSize();
        }
        for (Type aValType : valType) {
            valSize += aValType.getSize();
        }

        size = 0;
        this.rootId = rootId;
    }

    public void initFirstTime()
    {
        Page page = context.getPageManager().getPageById(rootId, true);
        Node root = LeafNode.getNewLeafNode(page, keySize, valSize, this);
        releaseNode(root);
    }

    public void releaseNode(int nodeId) {
        context.getPageManager().releasePage(nodeId);
    }

    public void releaseNode(Node node) {
        releaseNode(node.nodeId);
    }

    public TreeTuple getNewNodeIdTuple(int nodeId) {
        return TreeTuple.getOneIntTuple(nodeId);
    }

    public TreeTuple getNewKeyTuple(int[] columnIndexes, RelationRecord record){
        return TreeTuple.getTupleFromRecord(keySize, keyType, columnIndexes, record);
    }

    public TreeTuple getNewValTuple(TableRecord record){
        int pageId = record.getRecord().getPageId();
        int slotIndex = record.getRecord().getSlotIndex();

        return TreeTuple.getTwoIntTuple(pageId, slotIndex);
    }

    public TreeTuple getNewKeyTuple(Object[] objects) {
        return TreeTuple.getTupleFromList(keySize, keyType, objects);
    }

    public TreeTuple getNewValueTuple(Object[] objects) {
        return TreeTuple.getTupleFromList(valSize, valType, objects);
    }

    public Node getNewNode(boolean isLeaf) {
        Page page = context.getPageManager().allocatePage();
        Node newNode;

        if (isLeaf) {
            newNode = LeafNode.getNewLeafNode(page, keySize, valSize, this);
        } else {
            newNode = GuideNode.getNewGuideNode(page, keySize, NODE_ID_SIZE, this);
        }

        return newNode;
    }

    public Node getNodeById(int nodeId, boolean isForWriting) {
        Page page = context.getPageManager().getPageById(nodeId, isForWriting);
        NodeData nodeData = NodeData.getDataFromPage(page, keySize, valSize);

        if (nodeData.isLeaf()) {
            nodeData.changeSizes(keySize, valSize);
            return new LeafNode(page.getId(), nodeData, this);
        } else {
            nodeData.changeSizes(keySize, 4);
            return new GuideNode(page.getId(), nodeData, this);
        }
    }


    private int cmp(TreeTuple first, TreeTuple second) {
        int offset = 0;
        for (int i = 0; i < keyType.length; i++) {
            int cur = 0;
            if (keyType[i].getType() == Type.TYPE_INTEGER) {
                cur = first.getInteger(i) - second.getInteger(i);
            } else {
                int maxLength = keyType[i].getLength();
                cur = first.getString(offset, maxLength).compareTo(second.getString(offset, maxLength));
            }

            if (cur != 0) {
                return cur;
            }

            offset += keyType[i].getSize();
        }

        return 0;
    }

    public int findGuideIndex(Node node, TreeTuple key) {
        for (int i = 1; i < node.nodeData.getAmountOfKeys(); i++) {
            if (cmp(key, node.nodeData.getKey(i)) < 0) {
                return i - 1;
            }
        }

        return node.nodeData.getAmountOfKeys() - 1;
    }

    public int findInsertIndex(Node node, TreeTuple key) {
        int insertIndex = 0;
        while (insertIndex < node.nodeData.getAmountOfKeys()) {
            if (cmp(key, node.nodeData.getKey(insertIndex)) <= 0) {
                break;
            }

            insertIndex++;
        }
        return insertIndex;
    }


    public int findLeafIndex(Node node, TreeTuple key) {
        for (int i = 0; i < node.nodeData.getAmountOfKeys(); i++) {
            if (cmp(key, node.nodeData.getKey(i)) == 0) {
                return i;
            }
        }

        return -1;
    }

    public void put(TreeTuple key, TreeTuple value) {
        if (key == null) {
            throw new NullPointerException();
        }

        // Increment size?
        if (!containsKey(key)) {
            size++;
        }

        // Insert the new key/value into the tree.
        Node root = getNodeById(rootId, true);
        Node newNode = root.put(key, value);
        releaseNode(root);

        // Create new root?
        if (newNode != null) {
            Node newRoot = getNewNode(false);

            newRoot.nodeData.addValue(0, getNewNodeIdTuple(rootId));
            newRoot.nodeData.setAmountOfKeys(1);

            newRoot.nodeData.addValue(1, getNewNodeIdTuple(newNode.nodeId));
            newRoot.nodeData.addKey(1, newNode.nodeData.getKey(0));

            rootId = newRoot.nodeId;

            releaseNode(newRoot);
            releaseNode(newNode);
        }
    }

    public boolean containsKey(TreeTuple key) {
        return get(key) != null;
    }

    /**
     * Returns the value to which this BPTree maps the specified key or null if it contains no mapping for the key.
     */
    public TreeTuple get(TreeTuple key) {
        Node cur = getNodeById(rootId, false);
        while (!cur.nodeData.isLeaf()) {
            int index = findGuideIndex(cur, key);
            int nextNodeIndex = cur.nodeData.getValue(index).getInteger(0);
            releaseNode(cur);
            cur = getNodeById(nextNodeIndex, false);
        }

        int index = findLeafIndex(cur, key);
        TreeTuple res;

        if (index == -1) {
            res = null;
        } else {
            res = cur.nodeData.getValue(index);
        }

        releaseNode(cur);

        return res;
    }
}
