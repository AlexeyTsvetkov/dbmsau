package ru.spbau.mit.dbmsau.pages;

public class Record {
    private RecordsPage page;
    private int slotIndex;

    public Record(RecordsPage page, int slotIndex) {
        this.page = page;
        this.slotIndex = slotIndex;
    }

    private Page.PageData getPageByteBuffer() {
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
        if (value.length() > maxLength) {
            value = value.substring(0, maxLength);
        }

        getPageByteBuffer().put(getRecordOffset() + valueOffset, value.getBytes());

        if (value.length() < maxLength) {
            getPageByteBuffer().put(getRecordOffset() + valueOffset + value.length(), (byte) 0);
        }

    }

    public String getStringValue(int valueOffset, int maxLength) {
        int beginOffset = getRecordOffset() + valueOffset;
        int maxOffset = beginOffset + maxLength;

        int end = beginOffset;

        while (end < maxOffset) {
            if (getPageByteBuffer().get(end) == 0) {
                break;
            }

            end++;
        }

        //string within [beginOffset,end) interval

        byte[] content = new byte[end - beginOffset];

        getPageByteBuffer().get(beginOffset, content);

        return new String(content);
    }
}
