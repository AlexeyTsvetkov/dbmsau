package ru.spbau.mit.dbmsau.table;

public class Type {
    private String identifier;
    private Integer length;

    public Type(String identifier, Integer length) {
        this.identifier = identifier;
        this.length = length;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Integer getLength() {
        return length;
    }

    public String toString() {
        if (length == null) {
            return identifier;
        }

        return identifier + "(" + length.toString() + ")";
    }
}
