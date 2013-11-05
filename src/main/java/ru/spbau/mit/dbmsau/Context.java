package ru.spbau.mit.dbmsau;

import ru.spbau.mit.dbmsau.pages.FilePageManager;
import ru.spbau.mit.dbmsau.pages.PageManager;

import java.io.FileNotFoundException;

public class Context {
    private String path;
    private PageManager pageManager;

    public static Context loadContextFromPath(String path) {
        Context obj = new Context(path);
        try {
            obj.pageManager = new FilePageManager(obj);
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

    public void setPath(String path) {
        this.path = path;
    }

    public PageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(PageManager pageManager) {
        this.pageManager = pageManager;
    }

    public void init() throws FileNotFoundException {
        getPageManager().init();
    }
}
