package ru.spbau.mit.dbmsau.command.create_table;

public class ColumnDescription {
    private String name;
    private TypeDescription type;

    public ColumnDescription(String name, TypeDescription type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public TypeDescription getType() {
        return type;
    }

    public String toString() {
        return name + ":" + type.toString();
    }
}
