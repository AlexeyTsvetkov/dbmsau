package ru.spbau.mit.dbmsau.table;

public class Type {
    public static final int UNDEFINED_LENGTH = -1;

    public static final int TYPE_INTEGER = 0;
    public static final int TYPE_VARCHAR = 1;

    public static final String TYPE_IDENTIFIER_INTEGER = "integer";
    public static final String TYPE_IDENTIFIER_VARCHAR = "varchar";

    private static final int INTEGER_SIZE = 4;

    private static final String[] identifiers = new String[]{ TYPE_IDENTIFIER_INTEGER, TYPE_IDENTIFIER_VARCHAR };

    private static final Type INTEGER_TYPE = new Type(TYPE_INTEGER, UNDEFINED_LENGTH);

    private int type;
    private int length;
    private int size;

    public Type(int type, int length) {
        this.type = type;
        this.length = length;

        if (type == TYPE_INTEGER) {
            size = INTEGER_SIZE;
        } else {
            size = length;
        }
    }

    public static Type getType(String identifier, int length) {
        switch (identifier.toLowerCase()) {
            case TYPE_IDENTIFIER_INTEGER:
                return getIntegerType();
            case TYPE_IDENTIFIER_VARCHAR:
                return new Type(TYPE_VARCHAR, length);
        }
        return null;
    }

    public static Type getIntegerType() {
        return INTEGER_TYPE;
    }

    public static Type getType(String identifier) {
        return getType(identifier, UNDEFINED_LENGTH);
    }

    public String getIdentifier() {
        return identifiers[type];
    }

    public Integer getLength() {
        return length;
    }

    public String toString() {
        if (length == UNDEFINED_LENGTH) {
            return getIdentifier();
        }

        return getIdentifier() + "(" + Integer.valueOf(length).toString() + ")";
    }

    public int getType() {
        return type;
    }

    public int getSize() {
        return size;
    }
}
