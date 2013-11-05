package ru.spbau.mit.dbmsau.pages;

import ru.spbau.mit.dbmsau.Context;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StubPageManager extends PageManager {
    public StubPageManager(Context context) {
        super(context);
    }

    private Set<Integer> freePages = new HashSet<>();
    private Map<Integer, Page> allPages = new HashMap<>();

    private static final int STUB_PAGES_COUNT = 20000;

    @Override
    public PageManager init() throws FileNotFoundException {
        for (int i = 0; i < STUB_PAGES_COUNT; i++) {
            allPages.put(i, new Page(i, new byte[PAGE_SIZE]));

            if (i > 0) {
                freePages.add(i);
            }
        }

        PagesList emptyDirList = new PagesList(0, context);
        emptyDirList.initList();

        return this;
    }

    @Override
    public Page getPageById(Integer id) {
         return allPages.get(id);
    }

    @Override
    public void savePage(Page page) {
        allPages.put(page.getId(), page);
    }

    @Override
    public void freePage(Integer pageId) {
        freePages.add(pageId);
    }

    @Override
    public Page allocatePage() {
        Integer id = freePages.iterator().next();
        freePages.remove(id);

        return getPageById(id);
    }
}
