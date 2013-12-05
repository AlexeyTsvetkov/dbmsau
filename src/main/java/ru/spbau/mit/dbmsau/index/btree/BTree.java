package ru.spbau.mit.dbmsau.index.btree;

import java.util.ArrayList;

public class BTree {
    private ArrayList<Node> nodes;
    private Node root;
    private int leafOrder, guideOrder;
    int size;

    public BTree() {
        this.nodes = new ArrayList<>();

        leafOrder = guideOrder = 3;
        size = 0;

        root = getNewNode(true);
    }

    public Node getNewNode(boolean isLeaf)
    {
        int newNodeId = nodes.size();
        Node newNode;

        if(isLeaf)
            newNode = new LeafNode(newNodeId, this, leafOrder);
        else
            newNode = new GuideNode(newNodeId, this, guideOrder);

        nodes.add(newNode);
        return newNode;
    }

    public Node getNodeById(int nodeId)
    {
        return nodes.get(nodeId);
    }

    public int findGuideIndex(Node node, TreeTuple key)
    {
        for(int i = 1; i < node.nodeData.amountOfKeys; i++)
        {
            if(key.compareTo(node.nodeData.getKey(i)) < 0)
                return i - 1;
        }

        return node.nodeData.amountOfKeys - 1;
    }

    public int findInsertIndex(Node node, TreeTuple key)
    {
        int insertIndex = 0;
        while(insertIndex < node.nodeData.amountOfKeys)
        {
            if(key.compareTo(node.nodeData.getKey(insertIndex)) <= 0)
                break;

            insertIndex++;
        }
        return insertIndex;
    }


    public int findLeafIndex(Node node, TreeTuple key)
    {
        for(int i = 0; i < node.nodeData.amountOfKeys; i++)
        {
            if(key.compareTo(node.nodeData.getKey(i)) == 0)
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
        Node newNode = root.put(key, value);

        // Create new root?
        if(newNode != null)
        {
            Node newRoot = getNewNode(false);
            newRoot.nodeData.addKey(newNode.nodeData.getKey(0));
            newRoot.nodeData.addValue(new TreeTuple(root.nodeId));
            newRoot.nodeData.addValue(new TreeTuple(newNode.nodeId));;

            root = newRoot;
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
        Node cur = root;
        while(!cur.nodeData.isLeaf())
        {
            int index = findGuideIndex(cur, key);
            int nextNodeIndex = cur.nodeData.getValue(index).getInteger(0);
            cur = getNodeById(nextNodeIndex);
        }

        int index = findLeafIndex(cur, key);
        if(index == -1)
            return null;
        else
            return cur.nodeData.getValue(index);
    }
}
