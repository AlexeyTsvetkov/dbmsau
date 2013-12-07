package ru.spbau.mit.dbmsau.index.btree;

import ru.spbau.mit.dbmsau.pages.Page;

public abstract class Node {
    int nodeId;
    NodeData nodeData;
    BTree bTree;

    public Node(int nodeId, NodeData nodeData, BTree bTree)
    {
        this.nodeId = nodeId;
        this.nodeData = nodeData;
        this.bTree = bTree;
    }

    public abstract Node put(TreeTuple key, TreeTuple value);
}
