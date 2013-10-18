package ru.spbau.mit.dbmsau.syntax.ast;

public class TypeDescriptionNode extends ASTNode {
    private String typeIdentifier;
    private Integer length = null;

    public TypeDescriptionNode(String typeIdentifier, Integer length) {
        this.typeIdentifier = typeIdentifier;
        this.length = length;
    }

    public TypeDescriptionNode(TerminalNode typeIdentifier) {
        this(typeIdentifier.getLexemeValue(), null);
    }

    public TypeDescriptionNode(TerminalNode typeIdentifier, TerminalNode length) {
        this(typeIdentifier.getLexemeValue(), Integer.valueOf(length.getLexemeValue()));
    }

    public TypeDescriptionNode(String typeIdentifier) {
        this.typeIdentifier = typeIdentifier;
    }

    public String getTypeIdentifier() {
        return typeIdentifier;
    }

    public Integer getLength() {
        return length;
    }

}
