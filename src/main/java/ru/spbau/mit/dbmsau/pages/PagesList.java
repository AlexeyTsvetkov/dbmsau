package ru.spbau.mit.dbmsau.pages;

import ru.spbau.mit.dbmsau.Context;

public class PagesList {
    private Integer headPageId;
    private Context context;

    public PagesList(Integer headPageId, Context context) {
        this.headPageId = headPageId;
        this.context = context;
    }

    private DirectoryPage getHeadPage() {
        return new DirectoryPage(context.getPageManager().getPageById(headPageId));
    }

    private Integer firstUsedSlotIndex(DirectoryPage page) {
        for (int i = 1; i < page.getMaxRecordsCount(); i++) {
            if (page.isSlotUsed(i)) {
                return i;
            }
        }

        return null;
    }

    public Integer peek() {
        DirectoryPage page = getHeadPage();
        return page.getRecordFromSlot(firstUsedSlotIndex(page)).getIntegerValue(0);
    }

    public Integer pop() {
        DirectoryPage headPage = getHeadPage();
        Integer slotIndex = firstUsedSlotIndex(headPage);

        if (slotIndex == null) {
             return null;
        }

        Integer result = headPage.getRecordFromSlot(slotIndex).getIntegerValue(0);

        headPage.freeRecord(slotIndex);

        if (headPage.isDirectoryEmpty()) {
            Integer nextPageId = headPage.nextDirectoryPageId();
            if (!nextPageId.equals(Page.NULL_PAGE_ID)) {
                Page nextPage = context.getPageManager().getPageById(nextPageId);
                headPage.assignDataFrom(nextPage);

                context.getPageManager().freePage(nextPageId);
            }
        }

        context.getPageManager().savePage(headPage);

        return result;
    }

    public void put(Integer newPageId) {
        DirectoryPage page = getHeadPage();

        while (page.isDirectoryFull()) {

            Integer nextPageId = page.nextDirectoryPageId();

            if (!nextPageId.equals(Page.NULL_PAGE_ID)) {
                page = new DirectoryPage(context.getPageManager().getPageById(nextPageId));
            } else {
                DirectoryPage oldPage = page;
                Page allocatedPage = context.getPageManager().allocatePage();
                page = new DirectoryPage(allocatedPage);
                oldPage.setNextDirectoryPageId(page.getId());
                context.getPageManager().savePage(oldPage);
            }
        }

        page.getClearRecord().setIntegerValue(0, newPageId);
        context.getPageManager().savePage(page);
    }

}
