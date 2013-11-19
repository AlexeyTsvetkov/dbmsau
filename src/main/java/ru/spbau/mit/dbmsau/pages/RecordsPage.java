package ru.spbau.mit.dbmsau.pages;

import java.util.Iterator;

public class RecordsPage extends Page implements Iterable< Record > {
    private Integer recordsLength;

    public RecordsPage(Page page, Integer recordsLength) {
        super(page.getId(), page.getData());
        this.recordsLength = recordsLength;
    }

    public Integer getRecordsLength() {
        return recordsLength;
    }

    public Integer getMaxRecordsCount() {
        return (8 * PageManager.PAGE_SIZE) / (8 * getRecordsLength() + 1);
    }

    private Integer getBitmapOffset() {
        return getMaxRecordsCount() * getRecordsLength();
    }

    /**
     *
     * @param slotIndex
     * @return индекс байта во всей странице, где лежит бит слота в битмапе
     */
    private Integer getBitmapSlotByteIndex(Integer slotIndex) {
        return getBitmapOffset() + slotIndex/8;
    }

    public Boolean isSlotUsed(Integer slotIndex) {
        int byteNumber = getBitmapSlotByteIndex(slotIndex);
        return (getByteBuffer().get(byteNumber) & (1 << (slotIndex % 8))) != 0;
    }

    private void setSlotStatus(Integer slotIndex, boolean isUsed) {
        if (isSlotUsed(slotIndex) != isUsed) {
            int byteNumber = getBitmapSlotByteIndex(slotIndex);
            byte newValue = (byte)(getByteBuffer().get(byteNumber) ^ (1 << (slotIndex % 8)));
            getByteBuffer().put(byteNumber, newValue);
        }
    }

    private Integer getSlotOffset(Integer slotIndex) {
        return getRecordsLength() * slotIndex;
    }

    public Record getRecordFromSlot(Integer slotIndex) {
        return new Record(this, getSlotOffset(slotIndex));
    }

    protected Integer getFreeSlotsCount() {
        int counter = 0;
        for (int slotIndex = 0; slotIndex < getMaxRecordsCount(); slotIndex++) {
            if (!isSlotUsed(slotIndex)) {
                counter++;
            }
        }

        return counter;
    }

    public boolean isEmpty() {
         return getFreeSlotsCount().equals(getMaxRecordsCount());
    }

    public boolean isFull() {
        return getFreeSlotsCount().equals(0);
    }

    public Record getClearRecord() {
        int slotIndex = 0;
        while (slotIndex < getMaxRecordsCount() && isSlotUsed(slotIndex)) {
            slotIndex++;
        }

        if (slotIndex < getMaxRecordsCount()) {
            setSlotStatus(slotIndex, true);
            return getRecordFromSlot(slotIndex);
        }   else {
            throw new Error("плохо 123");
        }
    }

    public void freeRecord(Integer slotIndex) {
        setSlotStatus(slotIndex, false);
    }

    @Override
    public Iterator<Record> iterator() {
        return new RecordsIterator();
    }

    private class RecordsIterator implements Iterator< Record > {
        int counter = 0;

        @Override
        public boolean hasNext() {
            while (counter < getMaxRecordsCount() && !isSlotUsed(counter)) {
                counter++;
            }
            return counter < getMaxRecordsCount();
        }

        @Override
        public Record next() {
            return getRecordFromSlot(counter++);
        }

        @Override
        public void remove() {

        }
    }
}
