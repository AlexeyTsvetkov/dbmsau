package ru.spbau.mit.dbmsau.pages;

public class Record {
    private RecordsPage page;
    private Integer offset;

    public Record(RecordsPage page, Integer offset) {
        this.page = page;
        this.offset = offset;
    }

    public Integer getIntegerValue(int recordOffset) {
         return page.getByteBuffer().getInt(offset + recordOffset);
    }

    public void setIntegerValue(int recordOffset, Integer value) {
         page.getByteBuffer().putInt(offset + recordOffset, value);
    }
}
