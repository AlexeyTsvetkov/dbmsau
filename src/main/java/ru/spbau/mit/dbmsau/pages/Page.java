package ru.spbau.mit.dbmsau.pages;

import java.nio.ByteBuffer;

public class Page {
    public static final Integer NULL_PAGE_ID = -1;

    private Integer id;
    private PageData data;

    public Page(Integer id) {
        this.id = id;
    }

    public Page(Integer id, byte[] bytes) {
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

    public Integer getId() {
        return id;
    }

    public ByteBuffer getByteBuffer() {
        return data.byteBuffer;
    }

    public void assignDataFrom(Page page) {
        data.copyFrom(page.data);
    }

    private class PageData {
        private byte[] bytes;
        private ByteBuffer byteBuffer;

        private PageData(byte[] bytes) {
            setBytes(bytes);
        }

        private void setBytes(byte[] bytes) {
            this.bytes = bytes;
            this.byteBuffer = ByteBuffer.wrap(bytes);
        }

        private void copyFrom(PageData data) {
            setBytes(data.bytes);
        }
    }
}
