package ru.spbau.mit.dbmsau.index.btree;

import ru.spbau.mit.dbmsau.pages.Page;
import ru.spbau.mit.dbmsau.pages.PageManager;

public class NodeData {
    static final int NO_NODE_ID = -1;

    Page dataPage;

    private static int isLeafOffset = 0;
    private static int prevNodeIdOffset = isLeafOffset + 4;
    private static int nextNodeIdOffset = prevNodeIdOffset + 4;
    private static int amountOfKeysOffset = nextNodeIdOffset + 4;
    private static int headerSize = amountOfKeysOffset + 4;
    private int keySize;
    private int valSize;
    private int order;
    private int keyOffset, valOffset;

    public static NodeData getNewData(boolean isLeaf, Page dataPage, int keySize, int valSize) {
        NodeData res = getDataFromPage(dataPage, keySize, valSize);

        res.setIsLeaf(isLeaf);
        res.setAmountOfKeys(0);
        res.setNextNodeId(NO_NODE_ID);
        res.setPrevNodeId(NO_NODE_ID);

        return res;
    }

    public static NodeData getDataFromPage(Page dataPage, int keySize, int valSize) {
        return new NodeData(dataPage, keySize, valSize);
    }

    private NodeData(Page dataPage, int keySize, int valSize) {
        this.dataPage = dataPage;
        changeSizes(keySize, valSize);
    }

    public int getOrder() {
        return order;
    }

    public void calculateOrder(int keySize, int valSize) {
        order = (PageManager.PAGE_SIZE - headerSize) / (keySize + valSize) - 2;

        keyOffset = headerSize;
        valOffset = headerSize + keySize * (order + 1);
    }

    public void changeSizes(int keySize, int valSize) {
        this.keySize = keySize;
        this.valSize = valSize;

        calculateOrder(keySize, valSize);
    }

    private void shift(int startOffset, int endOffset, int elemSize, int direction) {
        byte[] element = new byte[elemSize];

        while (endOffset != startOffset) {
            dataPage.getByteBuffer().get(endOffset + direction * elemSize, element);
            dataPage.getByteBuffer().put(endOffset, element);

            endOffset = endOffset + direction * elemSize;
        }
    }

    private void shiftRight(int startOffset, int endOffset, int elemSize) {
        shift(startOffset, endOffset, elemSize, -1);
    }

    private void shiftLeft(int startOffset, int endOffset, int elemSize) {
        shift(endOffset, startOffset, elemSize, 1);
    }

    public void addKey(TreeTuple key) {
        this.addKey(getAmountOfKeys(), key);
    }

    public void addKey(int pos, TreeTuple key) {
        int amountOfKeys = getAmountOfKeys();

        shiftRight(keyOffset + pos * keySize, keyOffset + amountOfKeys * keySize, keySize);
        dataPage.getByteBuffer().put(keyOffset + pos * keySize, key.getBytes());

        setAmountOfKeys(amountOfKeys + 1);
    }

    public void addValue(TreeTuple value) {
        this.addValue(getAmountOfKeys(), value);
    }

    public void addValue(int pos, TreeTuple value) {
        int amountOfKeys = getAmountOfKeys();

        shiftRight(valOffset + pos * valSize, valOffset + amountOfKeys * valSize, valSize);
        dataPage.getByteBuffer().put(valOffset + pos * valSize, value.getBytes());
    }

    public void setKey(int pos, TreeTuple key) {
        dataPage.getByteBuffer().put(keyOffset + pos * keySize, key.getBytes());
    }

    public void setValue(int pos, TreeTuple value) {
        dataPage.getByteBuffer().put(valOffset + pos * valSize, value.getBytes());
    }

    public void resize(int newSize) {
        setAmountOfKeys(newSize);
    }

    public void removeKey(int pos) {
        int amountOfKeys = getAmountOfKeys();

        shiftLeft(keyOffset + pos * keySize, keyOffset + amountOfKeys * keySize, keySize);
        setAmountOfKeys(amountOfKeys - 1);
    }

    public void removeValue(int pos) {
        int amountOfKeys = getAmountOfKeys();
        shiftLeft(valOffset + pos * valSize, valOffset + amountOfKeys * valSize, valSize);
    }

    public TreeTuple getKey(int pos) {
        TreeTuple res = new TreeTuple(keySize);
        dataPage.getByteBuffer().get(keyOffset + pos * keySize, res.getBytes());
        return res;
    }

    public TreeTuple getValue(int pos) {
        TreeTuple res = new TreeTuple(valSize);
        dataPage.getByteBuffer().get(valOffset + pos * valSize, res.getBytes());
        return res;
    }

    public boolean isLeaf() {
        return dataPage.getByteBuffer().get(isLeafOffset) != 0;
    }

    private void setIsLeaf(boolean val) {
        if (val) {
            dataPage.getByteBuffer().put(isLeafOffset, (byte) 1);
        } else {
            dataPage.getByteBuffer().put(isLeafOffset, (byte) 0);
        }
    }

    public void setAmountOfKeys(int amountOfKeys) {
        dataPage.getByteBuffer().putInt(amountOfKeysOffset, amountOfKeys);
    }

    public int getAmountOfKeys() {
        return dataPage.getByteBuffer().getInt(amountOfKeysOffset);
    }

    public void setPrevNodeId(int prevNodeId) {
        dataPage.getByteBuffer().putInt(prevNodeIdOffset, prevNodeId);
    }

    public int getPrevNodeId() {
        return dataPage.getByteBuffer().getInt(prevNodeIdOffset);
    }

    public void setNextNodeId(int nextNodeId) {
        dataPage.getByteBuffer().putInt(nextNodeIdOffset, nextNodeId);
    }

    public int getNextNodeId() {
        return dataPage.getByteBuffer().getInt(nextNodeIdOffset);
    }
}
