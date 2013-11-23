package ru.spbau.mit.dbmsau.pages;

public class Record {
    private RecordsPage page;
    private Integer offset;

    public Record(RecordsPage page, Integer offset) {
        this.page = page;
        this.offset = offset;
    }

    public int getIntegerValue(int recordOffset) {
         return page.getByteBuffer().getInt(offset + recordOffset);
    }

    public void setIntegerValue(int recordOffset, int value) {
         page.getByteBuffer().putInt(offset + recordOffset, value);
    }
}
