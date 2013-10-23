package ru.spbau.mit.dbmsau;

public class Context {
    private String path;

    public static Context loadContextFromPath(String path) {
        return new Context(path);
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
}
