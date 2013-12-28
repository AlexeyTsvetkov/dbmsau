package ru.spbau.mit.dbmsau.pages;

public class Page {
    public static final int NULL_PAGE_ID = -1;

    private int id;
    private DataHolder data;

    public Page(int id) {
        this.id = id;
    }

    public Page(int id, byte[] bytes) {
        this(id);
        data = new DataHolder(bytes);
    }

    public Page(Page p) {
        this(p.getId());
        this.data = p.data;
    }

    public void setBytes(byte[] bytes) {
        this.data.setBytes(bytes);
    }


    public void clearData() {
        byte[] bytes = new byte[PageManager.PAGE_SIZE];
        this.data.setBytes(bytes);
    }

    public int getId() {
        return id;
    }

    public DataHolder getByteBuffer() {
        return data;
    }

    public byte[] getBytes() {
        return data.byteBuffer.array();
    }

    public void assignDataFrom(Page page) {
        data.copyFrom(page.data);
    }

    public boolean isDirty() {
        return data.isDirty;
    }

    public void markAsNotDirty() {
        data.isDirty = false;
    }
}
