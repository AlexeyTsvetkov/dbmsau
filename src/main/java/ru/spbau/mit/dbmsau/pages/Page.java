package ru.spbau.mit.dbmsau.pages;

import java.nio.ByteBuffer;

public class Page {
    public static final Integer NULL_PAGE_ID = -1;

    private Integer id;
    private byte[] data;
    private ByteBuffer byteBuffer;

    public Page(Integer id, byte[] data) {
        this.id = id;
        this.data = data;

        this.byteBuffer = ByteBuffer.wrap(data);
    }

    public Integer getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public void assignDataFrom(Page page) {
        data = page.data;
        byteBuffer = page.byteBuffer;
    }
}
