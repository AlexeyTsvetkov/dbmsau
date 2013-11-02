package ru.spbau.mit.dbmsau.pages;

import ru.spbau.mit.dbmsau.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PageManager {
    public static final Integer PAGE_SIZE = 4 * 1024;
    private static final String dataFilename = "data.db";
    private static final Integer EMPTY_PAGES_LIST_HEAD_PAGE_ID = 0;
    private static final Integer RESERVE_PAGES_COUNT = 50;

    private Context context;
    private RandomAccessFile dataFile;
    private Map< Integer, Page > cache = new HashMap<>();
    private PagesList emptyPagesList;

    public PageManager(Context context) throws FileNotFoundException {
        this.context = context;
        String currentDataFilename =  context.getPath() + "/" +dataFilename;

        File file = new File(currentDataFilename);

        Boolean wasFileNew = false;

        if (!file.exists()) {
            wasFileNew = true;
        }

        dataFile = new RandomAccessFile(currentDataFilename, "rw");

        if (wasFileNew) {
            initNewDataFile();
        }

        emptyPagesList = new PagesList(EMPTY_PAGES_LIST_HEAD_PAGE_ID, context);
    }

    public void initNewDataFile() {
        byte[] cleanData = new byte[PAGE_SIZE];
        Arrays.fill(cleanData, (byte)0);

        DirectoryPage page = new DirectoryPage(new Page(EMPTY_PAGES_LIST_HEAD_PAGE_ID, cleanData));
        page.getClearRecord().setIntegerValue(0, Page.NULL_PAGE_ID);

        savePage(page);
    }

    private Long getOffsetByPageId(Integer id) {
        return Long.valueOf(id) * Long.valueOf(PAGE_SIZE);
    }

    public Page getPageById(Integer id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }

        byte[] data = new byte[PAGE_SIZE];

        try {
            dataFile.seek(getOffsetByPageId(id));
            dataFile.readFully(data, 0, PAGE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Page page = new Page(id, data);
        cache.put(id, page);

        return page;
    }

    public RecordsPage getRecordPageById(Integer id, Integer length) {
        return new RecordsPage(getPageById(id), length);
    }

    public void savePage(Page page) {
        try {
            dataFile.seek(getOffsetByPageId(page.getId()));
            dataFile.write(page.getData(), 0, PAGE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void freePage(Integer pageId) {
        if (pageId.equals(EMPTY_PAGES_LIST_HEAD_PAGE_ID)) {
            throw new Error("freeing of system page");
        }
        cache.remove(pageId);
        emptyPagesList.put(pageId);
    }

    public Page allocatePage() {
        Integer pageId = emptyPagesList.pop();

        if (pageId == null) {
            try {
                Integer nextPageId = Long.valueOf(dataFile.length() / PAGE_SIZE).intValue();
                dataFile.setLength(dataFile.length() + PAGE_SIZE * RESERVE_PAGES_COUNT);

                for (int i = 0; i < RESERVE_PAGES_COUNT; i++) {
                    emptyPagesList.put(nextPageId + i);
                }

                return allocatePage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return getPageById(pageId);
    }
}
