package ru.spbau.mit.dbmsau.syntax.ast;

import java.util.List;

public class SelectStatementNode extends ASTNode {
    private List<ColumnAccessorNode> accessors;
    private TerminalNode tableFrom;
    private List<ComparisonNode> whereClause;

    public SelectStatementNode(List<ColumnAccessorNode> accessors, TerminalNode tableFrom, List<ComparisonNode> whereClause) {
        this.accessors = accessors;
        this.tableFrom = tableFrom;
        this.whereClause = whereClause;
    }

    public TerminalNode getTableFrom() {
        return tableFrom;
    }

    public List<ComparisonNode> getWhereClause() {
        return whereClause;
    }

    public List<ColumnAccessorNode> getAccessors() {
        return accessors;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
