package ru.spbau.mit.dbmsau.pages;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.ContextContainer;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerException;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerInitException;

import java.util.HashSet;
import java.util.Set;

abstract public class PageManager extends ContextContainer {
    public static final int PAGE_SIZE = 4 * 1024;
    protected static final int EMPTY_PAGES_LIST_HEAD_PAGE_ID = 0;

    private final Set<Integer> busyPagesIds = new HashSet<>();

    protected PageManager(Context context) {
        super(context);
    }

    public PageManager init() throws PageManagerInitException {
        return this;
    }

    public void releasePage(int id) {
        busyPagesIds.remove(id);
    }

    public void releasePage(Page page) {
        releasePage(page.getId());
    }

    protected void markPageAsBusy(int id) {
        busyPagesIds.add(id);
    }

    protected void markPageAsBusy(Page p) {
        markPageAsBusy(p.getId());
    }

    public void onQuit() throws PageManagerException {

    }

    public Page getPageById(int id, boolean isForWriting) {
        Page result = doGetPageById(id);

        if (isForWriting) {
            markPageAsBusy(result);
        }

        return result;
    }

    public void freePage(int pageId) {
        doFreePage(pageId);
        releasePage(pageId);
    }

    public Page allocatePage() {
        int id = doAllocatePage();
        return getPageById(id, true);
    }

    public boolean isThereBusyPages() {
        return busyPagesIds.size() > 0;
    }

    protected boolean isPageBusy(int id) {
        return busyPagesIds.contains(id);
    }

    abstract protected Page doGetPageById(int id);

    abstract protected void doFreePage(int pageId);

    public abstract int doAllocatePage();
}
