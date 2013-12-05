package ru.spbau.mit.dbmsau.index.btree;

public abstract class Node {
    int nodeId;
    NodeData nodeData;
    BTree bTree;
    int order;

    public Node(int nodeId, boolean isLeaf, BTree bTree, int order) {
        this.nodeId = nodeId;
        this.nodeData = new NodeData(isLeaf);
        this.bTree = bTree;
        this.order = order;
    }

    public abstract Node put(TreeTuple key, TreeTuple value);
}
