package ru.spbau.mit.dbmsau.index.btree;

import ru.spbau.mit.dbmsau.pages.DataHolder;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.relation.Type;
import ru.spbau.mit.dbmsau.table.TableRecord;

import java.util.ArrayList;

public class TreeTuple {
    private DataHolder data;
    private int sizeInBytes;


    public static TreeTuple getOneIntTuple(int val)
    {
        TreeTuple res = new TreeTuple(4);
        res.setInteger(0, val);
        return res;
    }

    public static TreeTuple getTwoIntTuple(int val1, int val2)
    {
        TreeTuple res = new TreeTuple(8);
        res.setInteger(0, val1);
        res.setInteger(4, val2);
        return res;
    }

    public static TreeTuple getTupleFromRecord(int typeSize, Type[] type, int[] columnIndexes, RelationRecord record)
    {
        TreeTuple res = new TreeTuple(typeSize);

        int offset = 0;
        for (int i = 0; i < columnIndexes.length; i++) {
            if (type[i].getType() == Type.TYPE_INTEGER) {
                res.setInteger(offset, record.getInteger(columnIndexes[i]));
            } else {
                res.setString(offset, record.getString(columnIndexes[i]), type[i].getLength());
            }

            offset += type[i].getSize();
        }

        return res;
    }

    public static TreeTuple getTupleFromList(int typeSize, Type[] type, Object[] objects) {
        TreeTuple res = new TreeTuple(typeSize);

        int offset = 0;
        for (int i = 0; i < objects.length; i++) {
            if (type[i].getType() == Type.TYPE_INTEGER) {
                res.setInteger(offset, (int) objects[i]);
            } else {
                res.setString(offset, (String) objects[i], type[i].getLength());
            }

            offset += type[i].getSize();
        }

        return res;
    }

    public TreeTuple(byte[] bytes) {
        data = new DataHolder(bytes);
        sizeInBytes = bytes.length;
    }

    public TreeTuple(int sizeInBytes) {
        this(new byte[sizeInBytes]);
    }

    public int getInteger(int offset) {
        return data.getInt(offset);
    }

    public TreeTuple setInteger(int offset, int val) {
        data.putInt(offset, val);
        return this;
    }

    public String getString(int offset, int maxLength) {
        return data.getString(offset, maxLength);
    }

    public TreeTuple setString(int offset, String str, int maxLength) {
        data.putString(offset, str, maxLength);
        return this;
    }

    public int getSize() {
        return sizeInBytes;
    }

    public byte[] getBytes() {
        return data.getBytes();
    }
}
