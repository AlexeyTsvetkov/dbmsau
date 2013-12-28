package ru.spbau.mit.dbmsau.index.btree;

import ru.spbau.mit.dbmsau.pages.Page;

public class LeafNode extends Node {
    public LeafNode(int nodeId, NodeData nodeData, BTree bTree) {
        super(nodeId, nodeData, bTree);
    }

    public static LeafNode getNewLeafNode(Page page, int keySize, int valSize, BTree bTree) {
        NodeData nodeData = NodeData.getNewData(true, page, keySize, valSize);
        return new LeafNode(page.getId(), nodeData, bTree);
    }

    public Node put(TreeTuple key, TreeTuple value) {
        Node newLeaf = null;

        // Find insert index.
        int insertIndex = bTree.findInsertIndex(this, key);

        // If the key already exists, then just replace.
        if (insertIndex < nodeData.getAmountOfKeys() && nodeData.getKey(insertIndex).equals(key)) {
            nodeData.setValue(insertIndex, value);
        } else {
            // Insert the new key and value at the found index.
            nodeData.addKey(insertIndex, key);
            nodeData.addValue(insertIndex, value);

            // Do we need to split?
            if (nodeData.getAmountOfKeys() > nodeData.getOrder()) {
                newLeaf = bTree.getNewNode(true);

                for (int i = nodeData.getAmountOfKeys() / 2; i < nodeData.getAmountOfKeys(); i++) {
                    newLeaf.nodeData.addValue(nodeData.getValue(i));
                    newLeaf.nodeData.addKey(nodeData.getKey(i));
                }

                nodeData.resize(nodeData.getAmountOfKeys() / 2);

                newLeaf.nodeData.setNextNodeId(nodeData.getNextNodeId());
                newLeaf.nodeData.setPrevNodeId(this.nodeId);

                if (nodeData.getNextNodeId() != NodeData.NO_NODE_ID) {
                    Node nextNode = bTree.getNodeById(nodeData.getNextNodeId(), true);
                    nextNode.nodeData.setPrevNodeId(newLeaf.nodeId);
                    bTree.releaseNode(nextNode);
                }

                nodeData.setNextNodeId(newLeaf.nodeId);
            }
        }

        return newLeaf;
    }

    /**
     * Returns the guide index of to use when looking for the specified key.
     */
    private int findLeafIndex(TreeTuple key) {
        return bTree.findLeafIndex(this, key);
    }
}
