package ru.spbau.mit.dbmsau.index.btree;

public abstract class Node {
    int nodeId;
    NodeData nodeData;
    BTree bTree;

    public Node(int nodeId, NodeData nodeData, BTree bTree) {
        this.nodeId = nodeId;
        this.nodeData = nodeData;
        this.bTree = bTree;
    }

    public abstract Node put(TreeTuple key, TreeTuple value);

    public NodeData getNodeData() {
        return nodeData;
    }

    public int getNodeId() {
        return nodeId;
    }
}
