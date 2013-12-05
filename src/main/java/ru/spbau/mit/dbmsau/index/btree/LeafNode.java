package ru.spbau.mit.dbmsau.index.btree;

import java.util.ArrayList;

public class LeafNode extends Node{
    public LeafNode(int nodeId, BTree bTree, int order) {
        super(nodeId, true, bTree, order);
    }

    public Node put(TreeTuple key, TreeTuple value)
    {
        Node newLeaf = null;

        // Find insert index.
        int insertIndex = bTree.findInsertIndex(this, key);

        // If the key already exists, then just replace.
        if(insertIndex < nodeData.amountOfKeys && nodeData.getKey(insertIndex).equals(key))
        {
            nodeData.setValue(insertIndex, value);
        }
        else
        {
            // Insert the new key and value at the found index.
            nodeData.addKey(insertIndex, key);
            nodeData.addValue(insertIndex, value);

            // Do we need to split?
            if(nodeData.amountOfKeys > order)
            {
                newLeaf = bTree.getNewNode(true);

                for(int i = nodeData.amountOfKeys/2; i< nodeData.amountOfKeys; i++)
                {
                    newLeaf.nodeData.addKey(nodeData.getKey(i));
                    newLeaf.nodeData.addValue(nodeData.getValue(i));
                }

                nodeData.resize(nodeData.amountOfKeys / 2);

                newLeaf.nodeData.nextNodeId = nodeData.nextNodeId;
                newLeaf.nodeData.prevNodeId = this.nodeId;
                if(nodeData.nextNodeId != NodeData.NO_NODE_ID)
                {
                    bTree.getNodeById(nodeData.nextNodeId).nodeData.prevNodeId = newLeaf.nodeId;
                }
                nodeData.nextNodeId = newLeaf.nodeId;
            }
        }

        return newLeaf;
    }

    /**
     * Returns the guide index of to use when looking for the specified key.
     */
    private int findLeafIndex(TreeTuple key)
    {
        return bTree.findLeafIndex(this, key);
    }
}
