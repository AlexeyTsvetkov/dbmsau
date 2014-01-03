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

                this.nodeData.splitTo(newLeaf.getNodeData());

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
}
