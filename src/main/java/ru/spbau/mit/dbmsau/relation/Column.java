package ru.spbau.mit.dbmsau.relation;

public class Column {
    private String name;
    private Type type;

    private String tableName;

    public Column(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Column(String tableName, String name, Type type) {
        this(name, type);
        this.tableName = tableName;
    }


    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String toString() {
        if (tableName != null) {
            return tableName + "." + name + ":" + type.toString();
        }
        return name + ":" + type.toString();
    }
}
