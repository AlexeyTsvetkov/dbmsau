package ru.spbau.mit.dbmsau.syntax.ast;

public class JoinNode extends ASTNode {
    private TerminalNode table;
    private ColumnAccessorNode left;
    private ColumnAccessorNode right;

    public JoinNode(TerminalNode table, ColumnAccessorNode left, ColumnAccessorNode right) {
        this.table = table;
        this.left = left;
        this.right = right;
    }

    public TerminalNode getTable() {
        return table;
    }

    public ColumnAccessorNode getLeft() {
        return left;
    }

    public ColumnAccessorNode getRight() {
        return right;
    }
}
