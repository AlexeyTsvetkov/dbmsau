package ru.spbau.mit.dbmsau.index.btree;

import ru.spbau.mit.dbmsau.pages.DataHolder;

public class TreeTuple {
    private DataHolder data;
    private int sizeInBytes;

    public TreeTuple(byte[] bytes)
    {
        data = new DataHolder(bytes);
        sizeInBytes = bytes.length;
    }

    public TreeTuple(int sizeInBytes)
    {
        this(new byte[sizeInBytes]);
    }

    public int getInteger(int offset)
    {
        return data.getInt(offset);
    }

    public TreeTuple setInteger(int offset, int val)
    {
        data.putInt(offset, val);
        return this;
    }

    public String getString(int offset, int maxLength)
    {
        return data.getString(offset, maxLength);
    }

    public TreeTuple setString(int offset, String str, int maxLength)
    {
        data.putString(offset, str, maxLength);
        return this;
    }

    public int getSize() {
        return sizeInBytes;
    }

    public byte[] getBytes()
    {
        return data.getBytes();
    }
}
