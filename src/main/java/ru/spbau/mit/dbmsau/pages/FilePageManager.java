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
import java.util.Queue;
import java.util.LinkedList;

public class FilePageManager extends PageManager {
    private static final String dataFilename = "data.db";
    private static final Integer EMPTY_PAGES_LIST_HEAD_PAGE_ID = 0;
    private static final Integer RESERVE_PAGES_COUNT = 50;
    private static final Integer MAX_PAGES_IN_CACHE = 50;

    private RandomAccessFile dataFile;
    private Map< Integer, Page > cache = new HashMap<>();
    private PagesList emptyPagesList;
    private Queue< Page > usageOrder = new LinkedList<>();

    public FilePageManager(Context context) {
        super(context);
    }

    public PageManager init() throws PageManagerInitException {
        String currentDataFilename =  context.getPath() + "/" +dataFilename;

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

    private Long getOffsetByPageId(Integer id) {
        return Long.valueOf(id) * Long.valueOf(PAGE_SIZE);
    }

    protected Page doGetPageById(Integer id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }

        if(cache.size() >= MAX_PAGES_IN_CACHE) {
            this.shrinkCache(MAX_PAGES_IN_CACHE - 1);
        }

        Page page = readPageFromFile(id);

        this.usageOrder.add(page);
        this.cache.put(id, page);

        return page;
    }

    private void shrinkCache(int desiredSize) {
        if (usageOrder.size() == 0) {
            return;
        }

        while(this.cache.size() > desiredSize) {
            Page firstInFirstOut = usageOrder.poll();

            Integer pageId = firstInFirstOut.getId();
            if(this.cache.containsKey(pageId)) {
                this.cache.remove(pageId);
            }
            try {
                this.savePage(firstInFirstOut);
            } catch (IOException e) {
                throw new PageManagerException("Saving error: " + e.getMessage());
            }
        }
    }

    private Page readPageFromFile(Integer id) {
        byte[] data = new byte[PAGE_SIZE];

        try {
            dataFile.seek(getOffsetByPageId(id));
            dataFile.readFully(data, 0, PAGE_SIZE);
        } catch (IOException e) {
            throw new PageManagerException(e.getMessage());
        }

        return new Page(id, data);
    }

    protected void doFreePage(Integer pageId) {
        if (pageId.equals(EMPTY_PAGES_LIST_HEAD_PAGE_ID)) {
            throw new Error("freeing of system page");
        }

        cache.remove(pageId);
        emptyPagesList.put(pageId);
    }

    public Integer doAllocatePage() {
        Integer pageId = emptyPagesList.pop();

        if (pageId == null) {
            try {
                Integer nextPageId = Long.valueOf(dataFile.length() / PAGE_SIZE).intValue();
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
        dataFile.seek(getOffsetByPageId(page.getId()));
        dataFile.write(page.getByteBuffer().array(), 0, PAGE_SIZE);
    }

    @Override
    public void onQuit() throws PageManagerException {
        super.onQuit();

        try {
            for (Page p : cache.values()) {
                savePage(p);
            }

            dataFile.close();
        } catch (IOException e) {
            throw new PageManagerException("Database saving failed: " + e.getMessage());
        }
    }
}
