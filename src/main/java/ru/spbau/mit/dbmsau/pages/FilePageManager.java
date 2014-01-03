package ru.spbau.mit.dbmsau.pages;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerException;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerInitException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class FilePageManager extends PageManager {
    private static final String dataFilename = "data.db";
    private static final int EMPTY_PAGES_LIST_HEAD_PAGE_ID = 0;
    private static final int RESERVE_PAGES_COUNT = 50;
    private static final int MAX_PAGES_IN_CACHE = 50;

    private RandomAccessFile dataFile;
    private Map<Integer, Integer> cache = new HashMap<>();
    private PagesList emptyPagesList;

    private boolean[] recentlyUsedBits = new boolean[MAX_PAGES_IN_CACHE];
    private Page[]    pagesInCircle = new Page[MAX_PAGES_IN_CACHE];
    private int       circlePointer = 0;

    public FilePageManager(Context context) {
        super(context);
    }

    public PageManager init() throws PageManagerInitException {
        String currentDataFilename = context.getPath() + File.separator + dataFilename;

        File file = new File(currentDataFilename);

        Boolean wasFileNew = false;

        if (!file.exists()) {
            wasFileNew = true;
        }
        try {
            dataFile = new RandomAccessFile(currentDataFilename, "rw");
        } catch (FileNotFoundException e) {
            throw new PageManagerInitException("File not found: " + currentDataFilename);
        }
        emptyPagesList = new PagesList(EMPTY_PAGES_LIST_HEAD_PAGE_ID, context);

        if (wasFileNew) {
            try {
                initNewDataFile();
            } catch (IOException e) {
                throw new PageManagerInitException("IO: " + e.getMessage());
            }
        }

        return this;
    }

    private void initNewDataFile() throws IOException {
        dataFile.setLength(PAGE_SIZE);
        emptyPagesList.initList();
    }

    private long getOffsetByPageId(int id) {
        return ((long) id) * ((long) PAGE_SIZE);
    }

    protected Page doGetPageById(int id) {
        if (cache.containsKey(id)) {
            int pageInCirclePlace = cache.get(id);
            recentlyUsedBits[pageInCirclePlace] = true;

            return pagesInCircle[pageInCirclePlace];
        }

        kickPageFromCircle();

        Page page = readPageFromFile(id);

        this.cache.put(id, circlePointer);
        recentlyUsedBits[circlePointer] = true;
        pagesInCircle[circlePointer] = page;
        circlePointer = (circlePointer + 1) % MAX_PAGES_IN_CACHE;

        return page;
    }

    private void kickPageFromCircle() {
        while (recentlyUsedBits[circlePointer] ||
                (pagesInCircle[circlePointer] != null && isPageBusy(pagesInCircle[circlePointer].getId()))
            ) {
            recentlyUsedBits[circlePointer] = false;
            circlePointer = (circlePointer + 1) % MAX_PAGES_IN_CACHE;
        }

        if (pagesInCircle[circlePointer] != null) {
            this.cache.remove(pagesInCircle[circlePointer].getId());

            try {
                this.savePage(pagesInCircle[circlePointer]);
            } catch (IOException e) {
                throw new PageManagerException("Saving error: " + e.getMessage());
            }
        }
    }

    private Page readPageFromFile(int id) {
        byte[] data = new byte[PAGE_SIZE];

        try {
            dataFile.seek(getOffsetByPageId(id));
            dataFile.readFully(data, 0, PAGE_SIZE);
        } catch (IOException e) {
            throw new PageManagerException(e.getMessage());
        }

        return new Page(id, data);
    }

    protected void doFreePage(int pageId) {
        if (pageId == EMPTY_PAGES_LIST_HEAD_PAGE_ID) {
            throw new Error("freeing of system page");
        }

        recentlyUsedBits[cache.get(pageId)] = false;
        cache.remove(pageId);

        emptyPagesList.put(pageId);
    }

    public int doAllocatePage() {
        Integer pageId = emptyPagesList.pop();

        if (pageId == null) {
            try {
                int nextPageId = (int) (dataFile.length() / (long) PAGE_SIZE);
                dataFile.setLength(dataFile.length() + PAGE_SIZE * RESERVE_PAGES_COUNT);

                for (int i = 0; i < RESERVE_PAGES_COUNT; i++) {
                    emptyPagesList.put(nextPageId + i);
                }

                return doAllocatePage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pageId;
    }

    private void savePage(Page page) throws IOException {
        if (!page.isDirty()) {
            return;
        }

        page.markAsNotDirty();

        dataFile.seek(getOffsetByPageId(page.getId()));
        dataFile.write(page.getBytes(), 0, PAGE_SIZE);
    }

    @Override
    public void onQuit() throws PageManagerException {
        super.onQuit();

        try {
            for (int placeInCircle : cache.values()) {
                savePage(pagesInCircle[placeInCircle]);
            }

            dataFile.close();
        } catch (IOException e) {
            throw new PageManagerException("Database saving failed: " + e.getMessage());
        }
    }
}
