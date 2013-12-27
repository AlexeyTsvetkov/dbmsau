package ru.spbau.mit.dbmsau.pages;

import ru.spbau.mit.dbmsau.Context;

import java.util.Arrays;
import java.util.Iterator;

public class PagesList implements Iterable<Page> {
    private static final int SLOT_NOT_FOUND = -1;
    private int headPageId;
    private Context context;

    static public PagesList createNewList(Context context) {
        int pageId = context.getPageManager().doAllocatePage();

        PagesList list = new PagesList(pageId, context);

        list.initList();

        return list;
    }

    public PagesList(int headPageId, Context context) {
        this.headPageId = headPageId;
        this.context = context;
    }

    public void initList() {
        context.getPageManager().releasePage(
            initNewLastPage(context.getPageManager().getPageById(headPageId, true))
        );
    }

    public Integer peek() {
        DirectoryPage headPage = getHeadPage(false);

        int slotIndex = firstUsedSlotIndex(headPage);

        if (slotIndex == SLOT_NOT_FOUND) {
            return null;
        }

        return headPage.getPageIdFromSlot(slotIndex);
    }

    private boolean deleteElement(int pageId, int slotIndex) {
        DirectoryPage page = buildDirectoryPage(pageId, true);

        page.freeRecord(slotIndex);

        boolean wasPagesShifted = false;

        while (page.isDirectoryEmpty()) {
            wasPagesShifted = true;
            int nextPageId = page.nextDirectoryPageId();
            if (nextPageId != Page.NULL_PAGE_ID) {
                Page nextPage = context.getPageManager().getPageById(nextPageId, true);
                page.assignDataFrom(nextPage);

                context.getPageManager().freePage(nextPageId);
            } else {
                context.getPageManager().releasePage(page);
                return wasPagesShifted;
            }
        }

        context.getPageManager().releasePage(page);

        return wasPagesShifted;
    }

    public Integer pop() {
        DirectoryPage headPage = getHeadPage(false);
        int slotIndex = firstUsedSlotIndex(headPage);

        if (slotIndex == SLOT_NOT_FOUND) {
            context.getPageManager().releasePage(headPage);
            return null;
        }

        Integer result = peek();

        deleteElement(getHeadPageId(), slotIndex);

        return result;
    }

    public Page popPage(boolean isForWriting) {
        Integer pageId = pop();

        if (pageId == null) {
            return null;
        }

        return context.getPageManager().getPageById(pageId, isForWriting);
    }

    public void put(int newPageId) {
        DirectoryPage page = getHeadPage(true);

        while (page.isDirectoryFull()) {
            DirectoryPage oldPage = page;
            DirectoryPage nextPage = getNextPage(page, true);

            if (nextPage != null) {
                page = nextPage;
            } else {
                Page allocatedPage = context.getPageManager().allocatePage();
                page = initNewLastPage(allocatedPage);
                oldPage.setNextDirectoryPageId(page.getId());
            }

            context.getPageManager().releasePage(oldPage);
        }

        page.getClearRecord().setIntegerValue(0, newPageId);
        context.getPageManager().releasePage(page);
    }

    public int getHeadPageId() {
        return headPageId;
    }

    @Override
    public Iterator<Page> iterator() {
        return new PagesIterator();
    }

    private DirectoryPage getHeadPage(boolean isForWriting) {
        return buildDirectoryPage(headPageId, isForWriting);
    }

    private DirectoryPage buildDirectoryPage(int pageId, boolean isForWriting) {
        return new DirectoryPage(context.getPageManager().getPageById(pageId, isForWriting));
    }

    private DirectoryPage getNextPage(DirectoryPage page, boolean isForWriting) {
        int nextPageId = page.nextDirectoryPageId();

        if (nextPageId != Page.NULL_PAGE_ID) {
            return buildDirectoryPage(nextPageId, isForWriting);
        } else {
            return null;
        }
    }

    private int firstUsedSlotIndex(DirectoryPage page) {
        for (int i = 1; i < page.getMaxRecordsCount(); i++) {
            if (page.isSlotUsed(i)) {
                return i;
            }
        }

        return -1;
    }

    private DirectoryPage initNewLastPage(Page page) {
        byte[] cleanData = new byte[PageManager.PAGE_SIZE];
        Arrays.fill(cleanData, (byte) 0);

        page.setBytes(cleanData);

        DirectoryPage directoryPage = new DirectoryPage(page);
        directoryPage.getClearRecord().setIntegerValue(0, Page.NULL_PAGE_ID);

        return directoryPage;
    }

    private class PagesIterator implements Iterator<Page> {
        private DirectoryPage currentPage;
        private int currentSlot;

        private PagesIterator() {
            currentPage = getHeadPage(false);
            currentSlot = firstUsedSlotIndex(currentPage);
        }

        private void walkUntilNext() {
            if (currentPage == null) {
                return;
            }

            while (currentSlot == SLOT_NOT_FOUND || currentSlot >= currentPage.getMaxRecordsCount() || !currentPage.isSlotUsed(currentSlot)) {
                if (currentSlot == SLOT_NOT_FOUND || currentSlot >= currentPage.getMaxRecordsCount()) {
                    currentPage = getNextPage(currentPage, false);

                    if (currentPage == null) {
                        return;
                    }

                    currentSlot = firstUsedSlotIndex(currentPage);
                } else {
                    currentSlot++;
                }
            }
        }

        @Override
        public boolean hasNext() {
            walkUntilNext();
            return currentPage != null && currentSlot != SLOT_NOT_FOUND;
        }

        @Override
        public Page next() {
            walkUntilNext();
            return context.getPageManager().getPageById(currentPage.getPageIdFromSlot(currentSlot++), false);
        }

        @Override
        public void remove() {
            //deleteElement returns true when pages swapped
            if (deleteElement(currentPage.getId(), currentSlot - 1)) {
                currentSlot = firstUsedSlotIndex(currentPage);
            }
        }
    }
}
