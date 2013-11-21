package ru.spbau.mit.dbmsau.pages;

import ru.spbau.mit.dbmsau.Context;

import java.util.Arrays;
import java.util.Iterator;

public class PagesList implements Iterable<Page> {
    private Integer headPageId;
    private Context context;

    static public PagesList createNewList(Context context) {
        Page page = context.getPageManager().allocatePage();

        PagesList list = new PagesList(page.getId(), context);

        list.initList();

        return list;
    }

    public PagesList(Integer headPageId, Context context) {
        this.headPageId = headPageId;
        this.context = context;
    }

    public void initList() {
        initNewLastPage(context.getPageManager().getPageById(headPageId));
    }

    public Integer peek() {
        DirectoryPage headPage = getHeadPage();

        Integer slotIndex = firstUsedSlotIndex(headPage);

        if (slotIndex == null) {
            return null;
        }

        return headPage.getPageIdFromSlot(slotIndex);
    }

    public Integer pop() {
        DirectoryPage headPage = getHeadPage();
        Integer slotIndex = firstUsedSlotIndex(headPage);

        if (slotIndex == null) {
             return null;
        }

        Integer result = peek();

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

    public Page popPage() {
        Integer result = pop();

        return result == null ? null : context.getPageManager().getPageById(result);
    }

    public void put(Integer newPageId) {
        DirectoryPage page = getHeadPage();

        while (page.isDirectoryFull()) {
            DirectoryPage nextPage = getNextPage(page);

            if (nextPage != null) {
                page = nextPage;
            } else {
                DirectoryPage oldPage = page;
                Page allocatedPage = context.getPageManager().allocatePage();
                page = initNewLastPage(allocatedPage);
                oldPage.setNextDirectoryPageId(page.getId());
                context.getPageManager().savePage(oldPage);
            }
        }

        page.getClearRecord().setIntegerValue(0, newPageId);
        context.getPageManager().savePage(page);
    }

    public Integer getHeadPageId() {
        return headPageId;
    }

    @Override
    public Iterator<Page> iterator() {
        return new PagesIterator();
    }

    private DirectoryPage getHeadPage() {
        return buildDirectoryPage(headPageId);
    }

    private DirectoryPage buildDirectoryPage(int pageId) {
        return new DirectoryPage(context.getPageManager().getPageById(pageId));
    }

    private DirectoryPage getNextPage(DirectoryPage page) {
        Integer nextPageId = page.nextDirectoryPageId();

        if (!nextPageId.equals(Page.NULL_PAGE_ID)) {
            return buildDirectoryPage(nextPageId);
        } else {
            return null;
        }
    }

    private Integer firstUsedSlotIndex(DirectoryPage page) {
        for (int i = 1; i < page.getMaxRecordsCount(); i++) {
            if (page.isSlotUsed(i)) {
                return i;
            }
        }

        return null;
    }

    private DirectoryPage initNewLastPage(Page page) {
        byte[] cleanData = new byte[PageManager.PAGE_SIZE];
        Arrays.fill(cleanData, (byte) 0);

        page.setData(cleanData);

        DirectoryPage directoryPage = new DirectoryPage(page);
        directoryPage.getClearRecord().setIntegerValue(0, Page.NULL_PAGE_ID);

        context.getPageManager().savePage(directoryPage);

        return directoryPage;
    }

    private class PagesIterator implements Iterator<Page> {
        private DirectoryPage currentPage;
        private Integer currentSlot;

        private PagesIterator() {
            currentPage = getHeadPage();
            currentSlot = firstUsedSlotIndex(currentPage);
        }

        private void walkUntilNext() {
            if (currentPage == null) {
                return;
            }

            while (currentSlot == null || currentSlot >= currentPage.getMaxRecordsCount() || !currentPage.isSlotUsed(currentSlot)) {
                if (currentSlot == null|| currentSlot >= currentPage.getMaxRecordsCount()) {
                    currentPage = getNextPage(currentPage);

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
            return  currentPage != null && currentSlot != null;
        }

        @Override
        public Page next() {
            walkUntilNext();
            return context.getPageManager().getPageById(currentPage.getPageIdFromSlot(currentSlot++));
        }

        @Override
        public void remove() {

        }
    }
}
