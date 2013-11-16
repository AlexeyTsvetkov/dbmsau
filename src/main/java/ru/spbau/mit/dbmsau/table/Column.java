package ru.spbau.mit.dbmsau.table;

public class Column {
    private String name;
    private Type type;

    public Column(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        return name + ":" + type.toString();
    }
}
