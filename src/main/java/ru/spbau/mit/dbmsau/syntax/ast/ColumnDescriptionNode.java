package ru.spbau.mit.dbmsau.syntax.ast;

public class ColumnDescriptionNode extends ASTNode {
    private String identifier;
    private TypeDescriptionNode type;

    public ColumnDescriptionNode(String identifier, TypeDescriptionNode type) {
        this.identifier = identifier;
        this.type = type;
    }

    public ColumnDescriptionNode(TerminalNode identifier, TypeDescriptionNode type) {
        this(identifier.getLexemeValue(), type);
    }

    public String getIdentifier() {
        return identifier;
    }

    public TypeDescriptionNode getType() {
        return type;
    }
}
