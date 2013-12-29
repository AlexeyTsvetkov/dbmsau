package ru.spbau.mit.dbmsau.syntax.ast;

import java.util.List;

public class DeleteStatementNode extends ASTNode {
    private TerminalNode tableName;
    private List<ComparisonNode> whereClause;

    public DeleteStatementNode(TerminalNode tableName, List<ComparisonNode> whereClause) {
        this.tableName = tableName;
        this.whereClause = whereClause;
    }

    public TerminalNode getTableName() {
        return tableName;
    }

    public List<ComparisonNode> getWhereClause() {
        return whereClause;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
