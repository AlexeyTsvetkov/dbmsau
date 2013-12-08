package ru.spbau.mit.dbmsau.table;

import java.util.Iterator;

abstract public class RecordSet implements Iterable<TableRecord>, Iterator<TableRecord> {
    @Override
    abstract public Iterator<TableRecord> iterator();

    @Override
    abstract public boolean hasNext();

    @Override
    abstract public TableRecord next();

    @Override
    abstract public void remove();

    public String toString() {
        return "";
    }
}
