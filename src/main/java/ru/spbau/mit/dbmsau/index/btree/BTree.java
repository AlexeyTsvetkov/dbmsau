package ru.spbau.mit.dbmsau.index.btree;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.ContextContainer;
import ru.spbau.mit.dbmsau.pages.Page;
import ru.spbau.mit.dbmsau.pages.PageManager;
import ru.spbau.mit.dbmsau.pages.RecordsPage;
import ru.spbau.mit.dbmsau.table.Type;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.Objects;

public class BTree extends ContextContainer {
    public static final int NODE_ID_SIZE = 4;
    private int rootId;
    private int keySize, valSize;
    private ArrayList<Type> keyType, valType;
    int size;

    public BTree(ArrayList<Type> keyType, ArrayList<Type> valType, Context context) {
        super(context);

        this.keyType = keyType;
        this.valType = valType;

        keySize = valSize = 0;
        for (Type aKeyType : keyType) keySize += aKeyType.getSize();
        for (Type aValType : valType) valSize += aValType.getSize();

        size = 0;

        Node root = getNewNode(true);
        rootId = root.nodeId;
        releaseNode(rootId);
    }

    public void releaseNode(int nodeId)
    {
        context.getPageManager().releasePage(nodeId);
    }

    public void releaseNode(Node node)
    {
        releaseNode(node.nodeId);
    }

    public TreeTuple getNewNodeIdTuple(int nodeId)
    {
        TreeTuple res = new TreeTuple(NODE_ID_SIZE);
        res.setInteger(0, nodeId);

        return res;
    }

    public TreeTuple getNewKeyTuple(ArrayList<Object> objects)
    {
        return getTreeTupleFromList(keySize, keyType, objects);
    }

    public TreeTuple getNewValueTuple(ArrayList<Object> objects)
    {
        return getTreeTupleFromList(valSize, valType, objects);
    }

    private TreeTuple getTreeTupleFromList(int typeSize, ArrayList<Type> type, ArrayList<Object> objects)
    {
        TreeTuple res = new TreeTuple(typeSize);

        int offset = 0;
        for(int i=0; i<objects.size(); i++)
        {
            if(type.get(i).getType() == Type.TYPE_INTEGER)
            {
                res.setInteger(offset, (int)objects.get(i));
            }
            else
            {
                res.setString(offset, (String)objects.get(i), type.get(i).getLength());
            }

            offset += type.get(i).getSize();
        }

        return res;
    }


    public Node getNewNode(boolean isLeaf)
    {
        Page page = context.getPageManager().allocatePage();
        Node newNode;

        if(isLeaf)
        {
            newNode = LeafNode.getNewLeafNode(page, keySize, valSize, this);
        }
        else
        {
            newNode = GuideNode.getNewGuideNode(page, keySize, NODE_ID_SIZE, this);
        }

        return newNode;
    }

    public Node getNodeById(int nodeId, boolean isForWriting)
    {
        Page page = context.getPageManager().getPageById(nodeId, isForWriting);
        NodeData nodeData = NodeData.getDataFromPage(page, keySize, valSize);

        if(nodeData.isLeaf())
        {
            nodeData.changeSizes(keySize, valSize);
            return new LeafNode(page.getId(), nodeData, this);
        }
        else
        {
            nodeData.changeSizes(keySize, 4);
            return new GuideNode(page.getId(), nodeData, this);
        }
    }


    private int cmp(TreeTuple first, TreeTuple second)
    {
        int offset = 0;
        for(int i=0; i<keyType.size(); i++)
        {
            int cur = 0;
            if(keyType.get(i).getType() == Type.TYPE_INTEGER)
            {
                cur = first.getInteger(i) - second.getInteger(i);
            }
            else
            {
                int maxLength = keyType.get(i).getLength();
                cur = first.getString(offset, maxLength).compareTo(second.getString(offset, maxLength));
            }

            if(cur!=0)
                return cur;

            offset += keyType.get(i).getSize();
        }

        return 0;
    }

    public int findGuideIndex(Node node, TreeTuple key)
    {
        for(int i = 1; i < node.nodeData.getAmountOfKeys(); i++)
        {
            if(cmp(key, node.nodeData.getKey(i)) < 0)
                return i - 1;
        }

        return node.nodeData.getAmountOfKeys() - 1;
    }

    public int findInsertIndex(Node node, TreeTuple key)
    {
        int insertIndex = 0;
        while(insertIndex < node.nodeData.getAmountOfKeys())
        {
            if(cmp(key, node.nodeData.getKey(insertIndex)) <= 0)
                break;

            insertIndex++;
        }
        return insertIndex;
    }


    public int findLeafIndex(Node node, TreeTuple key)
    {
        for(int i = 0; i < node.nodeData.getAmountOfKeys(); i++)
        {
            if(cmp(key, node.nodeData.getKey(i)) == 0)
                return i;
        }

        return -1;
    }

    public TreeTuple put(TreeTuple key, TreeTuple value)
    {
        if(key == null)
            throw new NullPointerException();

        // Increment size?
        if(!containsKey(key))
            size++;

        // Get previous value at the key.
        TreeTuple ret = get(key);

        // Insert the new key/value into the tree.
        Node root = getNodeById(rootId, true);
        Node newNode = root.put(key, value);
        releaseNode(root);

        // Create new root?
        if(newNode != null)
        {
            Node newRoot = getNewNode(false);

            newRoot.nodeData.addValue(0, getNewNodeIdTuple(rootId));
            newRoot.nodeData.setAmountOfKeys(1);

            newRoot.nodeData.addValue(1, getNewNodeIdTuple(newNode.nodeId));
            newRoot.nodeData.addKey(1, newNode.nodeData.getKey(0));

            rootId = newRoot.nodeId;

            releaseNode(newRoot);
            releaseNode(newNode);
        }

        // Return the previous value.
        return ret;
    }

    public boolean containsKey(TreeTuple key)
    {
        return get(key) != null;
    }

    /**
     * Returns the value to which this BPTree maps the specified key or null if it contains no mapping for the key.
     */
    public TreeTuple get(TreeTuple key)
    {
        Node cur = getNodeById(rootId, false);
        while(!cur.nodeData.isLeaf())
        {
            int index = findGuideIndex(cur, key);
            int nextNodeIndex = cur.nodeData.getValue(index).getInteger(0);
            releaseNode(cur);
            cur = getNodeById(nextNodeIndex, false);
        }

        int index = findLeafIndex(cur, key);
        TreeTuple res;

        if(index == -1)
            res = null;
        else
            res =  cur.nodeData.getValue(index);

        releaseNode(cur);

        return res;
    }
}
