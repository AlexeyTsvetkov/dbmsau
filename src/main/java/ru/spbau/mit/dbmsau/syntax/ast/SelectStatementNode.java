package ru.spbau.mit.dbmsau.syntax.ast;

public class SelectStatementNode extends ASTNode {
    private TerminalNode tableFrom;
    private WhereClauseNode whereClause;

    public SelectStatementNode(TerminalNode tableFrom, WhereClauseNode whereClause) {
        this.tableFrom = tableFrom;
        this.whereClause = whereClause;
    }

    public TerminalNode getTableFrom() {
        return tableFrom;
    }

    public WhereClauseNode getWhereClause() {
        return whereClause;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
