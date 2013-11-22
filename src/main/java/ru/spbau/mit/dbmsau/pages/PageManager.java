package ru.spbau.mit.dbmsau.pages;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.ContextContainer;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerException;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerInitException;

abstract public class PageManager extends ContextContainer {
    public static final Integer PAGE_SIZE = 4 * 1024;
    protected static final Integer EMPTY_PAGES_LIST_HEAD_PAGE_ID = 0;

    protected PageManager(Context context) {
        super(context);
    }

    public PageManager init() throws PageManagerInitException {
        return this;
    }

    public void onQuit() throws PageManagerException {

    }

    abstract public Page getPageById(Integer id);

    abstract public void savePage(Page page);

    abstract public void freePage(Integer pageId);

    abstract public Page allocatePage();
}
