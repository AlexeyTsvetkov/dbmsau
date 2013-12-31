package ru.spbau.mit.dbmsau.syntax.ast;

public class UpdateAssignmentNode extends ASTNode {
    private ColumnAccessorNode columnAccessor;
    private TerminalNode value;

    public UpdateAssignmentNode(ColumnAccessorNode columnAccessor, TerminalNode value) {
        this.columnAccessor = columnAccessor;
        this.value = value;
    }

    public ColumnAccessorNode getColumnAccessor() {
        return columnAccessor;
    }

    public TerminalNode getValue() {
        return value;
    }
}
