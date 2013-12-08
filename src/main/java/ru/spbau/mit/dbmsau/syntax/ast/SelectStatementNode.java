package ru.spbau.mit.dbmsau.syntax.ast;

public class SelectStatementNode extends ASTNode {
    private TerminalNode tableFrom;
    private ASTNode whereClause;

    public SelectStatementNode(TerminalNode tableFrom, ASTNode whereClause) {
        this.tableFrom = tableFrom;
        this.whereClause = whereClause;
    }

    public TerminalNode getTableFrom() {
        return tableFrom;
    }

    public ASTNode getWhereClause() {
        return whereClause;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
