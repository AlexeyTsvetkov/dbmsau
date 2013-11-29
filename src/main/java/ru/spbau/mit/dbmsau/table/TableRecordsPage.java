package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.pages.Page;
import ru.spbau.mit.dbmsau.pages.RecordsPage;

public class TableRecordsPage extends RecordsPage {
    private Table table;

    public TableRecordsPage(Table table, Page page) {
        super(page, getRecordLengthByTable(table));
        this.table = table;
    }

    private static int getRecordLengthByTable(Table table) {
        return table.getRecordSize();
    }

    public TableRecord getClearTableRecord() {
        return new TableRecord(getClearRecord(), table);
    }
}
