package ru.spbau.mit.dbmsau.pages;

import java.nio.ByteBuffer;

public class DataHolder {
    private byte[] bytes;
    public ByteBuffer byteBuffer;
    public boolean isDirty = false;

    public DataHolder(byte[] bytes) {
        setBytes(bytes);
        isDirty = false;
    }

    public void markAsDirty() {
        isDirty = true;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
        this.byteBuffer = ByteBuffer.wrap(bytes);
        markAsDirty();
    }

    public void copyFrom(DataHolder data) {
        setBytes(data.bytes);
    }

    public byte get(int byteNumber) {
        return byteBuffer.get(byteNumber);
    }

    public DataHolder put(int byteNumber, byte v) {
        markAsDirty();
        byteBuffer.put(byteNumber, v);
        return this;
    }

    public int getInt(int byteNumber) {
        return byteBuffer.getInt(byteNumber);
    }

    public String getString(int beginOffset, int maxLength) {
        int maxOffset = beginOffset + maxLength;

        int end = beginOffset;

        while (end < maxOffset) {
            if (get(end) == 0) {
                break;
            }

            end++;
        }

        //string within [beginOffset,end) interval

        byte[] content = new byte[end - beginOffset];

        get(beginOffset, content);

        return new String(content);
    }

    public DataHolder putString(int beginOffset, String value, int maxLength) {
        if (value.length() > maxLength) {
            value = value.substring(0, maxLength);
        }

        put(beginOffset, value.getBytes());

        if (value.length() < maxLength) {
            put(beginOffset + value.length(), (byte) 0);
        }

        return this;
    }

    public DataHolder putInt(int byteNumber, int v) {
        markAsDirty();
        byteBuffer.putInt(byteNumber, v);
        return this;
    }

    public DataHolder put(int bufferOffset, byte[] src) {
        markAsDirty();

        byteBuffer.position(bufferOffset);
        byteBuffer.put(src);

        return this;
    }

    public DataHolder get(int bufferOffset, byte[] dst) {
        byteBuffer.position(bufferOffset);
        byteBuffer.get(dst);

        return this;
    }

    public byte[] getBytes() {
        return bytes;
    }
}