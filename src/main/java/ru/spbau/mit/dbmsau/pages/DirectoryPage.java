package ru.spbau.mit.dbmsau.pages;

public class DirectoryPage extends RecordsPage {
    private static final int RECORDS_LENGTH = 4;

    public DirectoryPage(Page page) {
        super(page, RECORDS_LENGTH);
    }

    public Boolean isDirectoryEmpty() {
        return getMaxRecordsCount() == getFreeSlotsCount()+1;
    }

    public Boolean isDirectoryFull() {
        return getFreeSlotsCount() == 0;
    }

    public int nextDirectoryPageId() {
        return getRecordFromSlot(0).getIntegerValue(0);
    }

    public void setNextDirectoryPageId(int pageId) {
        getRecordFromSlot(0).setIntegerValue(0, pageId);
    }

    public int getPageIdFromSlot(int slotIndex) {
        return getRecordFromSlot(slotIndex).getIntegerValue(0);
    }
}
