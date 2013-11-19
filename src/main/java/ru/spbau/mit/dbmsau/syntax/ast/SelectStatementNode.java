package ru.spbau.mit.dbmsau.syntax.ast;

public class SelectStatementNode extends ASTNode {
    private TerminalNode tableFrom;

    public SelectStatementNode(TerminalNode tableFrom) {
        this.tableFrom = tableFrom;
    }

    public TerminalNode getTableFrom() {
        return tableFrom;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
