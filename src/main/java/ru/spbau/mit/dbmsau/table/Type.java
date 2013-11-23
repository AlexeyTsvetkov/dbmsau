package ru.spbau.mit.dbmsau.table;

public class Type {
    public static final int TYPE_INTEGER = 0;
    public static final int TYPE_VARCHAR = 1;

    public static final String TYPE_IDENTIFIER_INTEGER = "integer";
    public static final String TYPE_IDENTIFIER_VARCHAR = "varchar";

    private static final String[] identifiers = new String[]{ TYPE_IDENTIFIER_INTEGER, TYPE_IDENTIFIER_VARCHAR };

    private int type;
    private Integer length;

    public Type(int type, Integer length) {
        if (length != null && length.equals(-1)) {
            length = null;
        }
        this.type = type;
        this.length = length;
    }

    public static Type getType(String identifier, Integer length) {
        switch (identifier.toLowerCase()) {
            case TYPE_IDENTIFIER_INTEGER:
                return new Type(TYPE_INTEGER, null);
            case TYPE_IDENTIFIER_VARCHAR:
                return new Type(TYPE_VARCHAR, length);
        }
        return null;
    }

    public static Type getType(String identifier) {
        return getType(identifier, null);
    }

    public String getIdentifier() {
        return identifiers[type];
    }

    public Integer getLength() {
        return length;
    }

    public String toString() {
        if (length == null) {
            return getIdentifier();
        }

        return getIdentifier() + "(" + length.toString() + ")";
    }

    public int getType() {
        return type;
    }

    public int getSize() {
        return 4;
    }
}
