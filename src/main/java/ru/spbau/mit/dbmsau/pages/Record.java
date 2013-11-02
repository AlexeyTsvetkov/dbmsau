package ru.spbau.mit.dbmsau.pages;

public class Record {
    private RecordsPage page;
    private Integer offset;

    public Record(RecordsPage page, Integer offset) {
        this.page = page;
        this.offset = offset;
    }

    public Integer getIntegerValue(Integer fieldNumber) {
         return page.getByteBuffer().getInt(offset);
    }

    public void setIntegerValue(Integer fieldNumber, Integer value) {
         page.getByteBuffer().putInt(offset, value);
    }
}
