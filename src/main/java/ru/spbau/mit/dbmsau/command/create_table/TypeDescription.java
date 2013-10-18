package ru.spbau.mit.dbmsau.command.create_table;

public class TypeDescription {
    private String identifier;
    private Integer length;

    public TypeDescription(String identifier, Integer length) {
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
