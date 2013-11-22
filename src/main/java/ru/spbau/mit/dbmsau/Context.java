package ru.spbau.mit.dbmsau;

import ru.spbau.mit.dbmsau.pages.FilePageManager;
import ru.spbau.mit.dbmsau.pages.PageManager;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerInitException;
import ru.spbau.mit.dbmsau.table.FileTableManager;
import ru.spbau.mit.dbmsau.table.RecordManager;
import ru.spbau.mit.dbmsau.table.TableManager;

import java.io.File;

public class Context {
    private String path;
    private PageManager pageManager;
    private TableManager tableManager;
    private RecordManager recordManager;

    public static Context loadContextFromPath(String path) {
        Context obj = new Context(path);
        try {
            obj.pageManager = new FilePageManager(obj);
            obj.tableManager = new FileTableManager(obj);
            obj.recordManager = new RecordManager(obj);
            obj.init();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return obj;
    }

    public Context(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public File getRootDir() {
        return new File(getPath());
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(PageManager pageManager) {
        this.pageManager = pageManager;
    }

    public TableManager getTableManager() {
        return tableManager;
    }

    public void setTableManager(TableManager tableManager) {
        this.tableManager = tableManager;
    }

    public RecordManager getRecordManager() {
        return recordManager;
    }

    public void setRecordManager(RecordManager recordManager) {
        this.recordManager = recordManager;
    }

    public void init() throws PageManagerInitException {
        getPageManager().init();
        getTableManager().init();
        getRecordManager().init();
    }

    public void onQuit() {

    }
}
