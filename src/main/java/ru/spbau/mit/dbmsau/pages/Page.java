package ru.spbau.mit.dbmsau.pages;

import java.nio.ByteBuffer;

public class Page {
    public static final int NULL_PAGE_ID = -1;

    private int id;
    private PageData data;

    public Page(int id) {
        this.id = id;
    }

    public Page(int id, byte[] bytes) {
        this(id);
        data = new PageData(bytes);
    }

    public Page(Page p) {
        this(p.getId());
        this.data = p.data;
    }

    public void setBytes(byte[] bytes) {
        this.data.setBytes(bytes);
    }

    public int getId() {
        return id;
    }

    public PageData getByteBuffer() {
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

    public class PageData {
        private byte[] bytes;
        private ByteBuffer byteBuffer;
        private boolean isDirty = false;

        private PageData(byte[] bytes) {
            setBytes(bytes);
            isDirty = false;
        }

        private void markAsDirty() {
            isDirty = true;
        }

        private void setBytes(byte[] bytes) {
            this.bytes = bytes;
            this.byteBuffer = ByteBuffer.wrap(bytes);
            markAsDirty();
        }

        private void copyFrom(PageData data) {
            setBytes(data.bytes);
        }

        public byte get(int byteNumber) {
            return byteBuffer.get(byteNumber);
        }

        public PageData put(int byteNumber, byte v) {
            markAsDirty();
            byteBuffer.put(byteNumber, v);
            return this;
        }

        public int getInt(int byteNumber) {
            return byteBuffer.getInt(byteNumber);
        }

        public PageData putInt(int byteNumber, int v) {
            markAsDirty();
            byteBuffer.putInt(byteNumber, v);
            return this;
        }
    }
}
