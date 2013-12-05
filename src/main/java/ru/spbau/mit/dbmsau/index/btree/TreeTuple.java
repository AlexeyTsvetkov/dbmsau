package ru.spbau.mit.dbmsau.index.btree;

public class TreeTuple {
    private int value;

    public TreeTuple()
    {
        value = 0;
    }

    public int getInteger(int pos)
    {
        return value;
    }

    public TreeTuple(int value) {
        this.value = value;
    }

    public int compareTo(TreeTuple other)
    {
        return Integer.compare(value, other.value);
    }
}
