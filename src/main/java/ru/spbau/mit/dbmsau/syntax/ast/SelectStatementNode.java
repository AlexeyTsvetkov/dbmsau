package ru.spbau.mit.dbmsau.syntax.ast;

public class SelectStatementNode extends ASTNode {
    private TerminalNode tableFrom;
    private WhereClauseNode filter;

    public SelectStatementNode(TerminalNode tableFrom, WhereClauseNode filter) {
        this.tableFrom = tableFrom;
        this.filter = filter;
    }

    public TerminalNode getTableFrom() {
        return tableFrom;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
