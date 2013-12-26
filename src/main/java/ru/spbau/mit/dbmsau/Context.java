package ru.spbau.mit.dbmsau;

import ru.spbau.mit.dbmsau.index.FileIndexManager;
import ru.spbau.mit.dbmsau.index.IndexManager;
import ru.spbau.mit.dbmsau.pages.FilePageManager;
import ru.spbau.mit.dbmsau.pages.PageManager;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerInitException;
import ru.spbau.mit.dbmsau.table.FileTableManager;
import ru.spbau.mit.dbmsau.table.SemanticValidator;
import ru.spbau.mit.dbmsau.table.TableManager;
import ru.spbau.mit.dbmsau.table.TableRecordManager;

import java.io.File;

public class Context {
    private String path;
    private PageManager pageManager;
    private TableManager tableManager;
    private TableRecordManager tableRecordManager;
    private IndexManager indexManager;
    private SemanticValidator semanticValidator;

    public static Context loadContextFromPath(String path) {
        Context obj = new Context(path);
        try {
            obj.pageManager = new FilePageManager(obj);
            obj.tableManager = new FileTableManager(obj);
            obj.tableRecordManager = new TableRecordManager(obj);
            obj.indexManager = new FileIndexManager(obj);

            obj.init();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return obj;
    }

    public Context(String path) {
        this.path = path;
        this.semanticValidator = new SemanticValidator();
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

    public TableRecordManager getTableRecordManager() {
        return tableRecordManager;
    }

    public void setTableRecordManager(TableRecordManager tableRecordManager) {
        this.tableRecordManager = tableRecordManager;
    }

    public IndexManager getIndexManager() {
        return indexManager;
    }

    public void setIndexManager(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    public SemanticValidator getSemanticValidator() {
        return semanticValidator;
    }

    public void init() throws PageManagerInitException {
        getPageManager().init();
        getTableManager().init();
        getTableRecordManager().init();
        getIndexManager().init();
    }

    public void onQuit() throws Exception {
        getPageManager().onQuit();
    }
}
