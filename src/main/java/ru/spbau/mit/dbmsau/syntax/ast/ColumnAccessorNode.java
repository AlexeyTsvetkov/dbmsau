package ru.spbau.mit.dbmsau.syntax.ast;

public class ColumnAccessorNode extends ASTNode {
    private TerminalNode tableIdent;
    private TerminalNode columnIdent;

    public ColumnAccessorNode(TerminalNode tableIdent, TerminalNode columnIdent) {
        this.tableIdent = tableIdent;
        this.columnIdent = columnIdent;
    }

    public ColumnAccessorNode(TerminalNode columnIdent) {
        this.columnIdent = columnIdent;
    }

    public TerminalNode getTableIdent() {
        return tableIdent;
    }

    public TerminalNode getColumnIdent() {
        return columnIdent;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
