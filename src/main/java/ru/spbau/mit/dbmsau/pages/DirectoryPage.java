package ru.spbau.mit.dbmsau.pages;

public class DirectoryPage extends RecordsPage {
    private static final int RECORDS_LENGTH = 4;

    public DirectoryPage(Page page) {
        super(page, RECORDS_LENGTH);
    }

    public Boolean isDirectoryEmpty() {
        return getMaxRecordsCount().equals(getFreeSlotsCount()+1);
    }

    public Boolean isDirectoryFull() {
        return getFreeSlotsCount().equals(0);
    }

    public Integer nextDirectoryPageId() {
        return getRecordFromSlot(0).getIntegerValue(0);
    }

    public void setNextDirectoryPageId(Integer pageId) {
        getRecordFromSlot(0).setIntegerValue(0, pageId);
    }
}
