package ru.spbau.mit.dbmsau.index.btree;

import java.util.ArrayList;

public class NodeData {
    static final int NO_NODE_ID = -1;

    private boolean isLeaf;
    int prevNodeId;
    int nextNodeId;
    int amountOfKeys;

    private ArrayList<TreeTuple> keys;
    private ArrayList<TreeTuple> values;

    public NodeData(boolean isLeaf)
    {
        this(isLeaf, NO_NODE_ID, NO_NODE_ID);
    }

    public NodeData(boolean leaf, int prevNodeId, int nextNodeId) {
        isLeaf = leaf;
        this.prevNodeId = prevNodeId;
        this.nextNodeId = nextNodeId;
        this.amountOfKeys = 0;

        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void addKey(TreeTuple key)
    {
        this.addKey(amountOfKeys, key);
    }

    public void addKey(int pos, TreeTuple key)
    {
        keys.add(pos, key);
        amountOfKeys+=1;
    }

    public void addValue(TreeTuple value)
    {
        values.add(value);
    }

    public void addValue(int pos, TreeTuple value)
    {
        values.add(pos, value);
    }

    public void setKey(int pos, TreeTuple key)
    {
        keys.set(pos, key);
    }

    public void setValue(int pos, TreeTuple value)
    {
        values.set(pos, value);
    }

    public void resize(int newSize)
    {
        ArrayList<TreeTuple> newKeys = new ArrayList<>();
        ArrayList<TreeTuple> newValues = new ArrayList<>();

        newKeys.addAll(keys.subList(0, newSize));
        newValues.addAll(values.subList(0, newSize));

        keys = newKeys;
        values = newValues;
        amountOfKeys = keys.size();
    }

    public void removeKey(int pos)
    {
        keys.remove(pos);
        amountOfKeys -= 1;
    }

    public void removeValue(int pos)
    {
        values.remove(pos);
    }

    public TreeTuple getKey(int pos)
    {
        return keys.get(pos);
    }

    public TreeTuple getValue(int pos)
    {
        return values.get(pos);
    }


    public boolean isLeaf() {
        return isLeaf;
    }

    public void setAmountOfKeys(int amountOfKeys) {
        this.amountOfKeys = amountOfKeys;
    }

    public void setPrevNodeId(int prevNodeId) {
        this.prevNodeId = prevNodeId;
    }

    public void setNextNodeId(int nextNodeId) {
        this.nextNodeId = nextNodeId;
    }

    public int getPrevNodeId() {
        return prevNodeId;
    }

    public int getNextNodeId() {
        return nextNodeId;
    }

    public int getAmountOfKeys() {
        return amountOfKeys;
    }
}
