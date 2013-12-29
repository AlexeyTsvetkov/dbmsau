package ru.spbau.mit.dbmsau.syntax.ast;

import java.util.List;

public class SelectStatementNode extends ASTNode {
    private List<ColumnAccessorNode> accessors;
    private TerminalNode tableFrom;
    private List<ComparisonNode> whereClause;
    private JoinNode join;

    public SelectStatementNode(List<ColumnAccessorNode> accessors, TerminalNode tableFrom, List<ComparisonNode> whereClause, JoinNode join) {
        this.accessors = accessors;
        this.tableFrom = tableFrom;
        this.whereClause = whereClause;
        this.join = join;
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

    public JoinNode getJoin() {
        return join;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
