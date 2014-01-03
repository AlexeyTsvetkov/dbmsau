package ru.spbau.mit.dbmsau.index.btree;

import ru.spbau.mit.dbmsau.pages.Page;

public class GuideNode extends Node {
    public GuideNode(int nodeId, NodeData nodeData, BTree bTree) {
        super(nodeId, nodeData, bTree);
    }

    public static GuideNode getNewGuideNode(Page page, int keySize, int valSize, BTree bTree) {
        NodeData nodeData = NodeData.getNewData(false, page, keySize, valSize);
        return new GuideNode(page.getId(), nodeData, bTree);
    }

    /**
     * Maps the specified key to the specified value in this Node.
     *
     * @return A new right node if this node was split, else null.
     */
    public Node put(TreeTuple key, TreeTuple value) {
        Node newGuide = null;

        int guideIndex = bTree.findGuideIndex(this, key);

        // Recurse to child.
        int childId = this.nodeData.getValue(guideIndex).getInteger(0);
        Node child = this.bTree.getNodeById(childId, true);
        Node newNode = child.put(key, value);
        bTree.releaseNode(childId);

        // Did we split?
        if (newNode != null) {
            // Insert the new key and node at the found index.
            this.nodeData.addKey(guideIndex + 1, newNode.nodeData.getKey(0));
            this.nodeData.addValue(guideIndex + 1, bTree.getNewNodeIdTuple(newNode.nodeId));

            // Do we need to split?
            if (nodeData.getAmountOfKeys() > nodeData.getOrder()) {
                newGuide = bTree.getNewNode(false);


                this.nodeData.splitTo(newGuide.getNodeData());

                newGuide.nodeData.setNextNodeId(nodeData.getNextNodeId());
                newGuide.nodeData.setPrevNodeId(this.nodeId);

                if (nodeData.getNextNodeId() != NodeData.NO_NODE_ID) {
                    Node nextNode = bTree.getNodeById(nodeData.getNextNodeId(), true);
                    nextNode.nodeData.setPrevNodeId(newGuide.nodeId);
                    bTree.releaseNode(nextNode);
                }
                nodeData.setNextNodeId(newGuide.nodeId);
            }

            bTree.releaseNode(newNode);
        }

        return newGuide;
    }
}
