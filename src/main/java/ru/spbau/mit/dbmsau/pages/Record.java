package ru.spbau.mit.dbmsau.pages;

public class Record {
    private RecordsPage page;
    private int slotIndex;

    public Record(RecordsPage page, int slotIndex) {
        this.page = page;
        this.slotIndex = slotIndex;
    }

    private DataHolder getPageByteBuffer() {
        return page.getByteBuffer();
    }

    private int getRecordOffset() {
        return page.getSlotOffset(slotIndex);
    }

    public int getIntegerValue(int valueOffset) {
         return getPageByteBuffer().getInt(getRecordOffset() + valueOffset);
    }

    public void setIntegerValue(int valueOffset, int value) {
         getPageByteBuffer().putInt(getRecordOffset() + valueOffset, value);
    }

    public void setStringValue(int valueOffset, String value, int maxLength) {
        getPageByteBuffer().putString(getRecordOffset() + valueOffset, value, maxLength);
    }

    public String getStringValue(int valueOffset, int maxLength) {
        int beginOffset = getRecordOffset() + valueOffset;
        return getPageByteBuffer().getString(beginOffset, maxLength);
    }

    public int getPageId() {
        return page.getId();
    }

    public int getSlotIndex() {
        return slotIndex;
    }
}
