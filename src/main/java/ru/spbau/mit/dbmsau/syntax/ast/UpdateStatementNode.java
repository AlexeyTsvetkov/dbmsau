package ru.spbau.mit.dbmsau.syntax.ast;

import java.util.List;

public class UpdateStatementNode extends ASTNode {
    private TerminalNode tableName;
    private List<UpdateAssignmentNode> assignments;
    private List<ComparisonNode> where;

    public UpdateStatementNode(TerminalNode tableName, List<UpdateAssignmentNode> assignments, List<ComparisonNode> where) {
        this.tableName = tableName;
        this.assignments = assignments;
        this.where = where;
    }

    public TerminalNode getTableName() {
        return tableName;
    }

    public List<UpdateAssignmentNode> getAssignments() {
        return assignments;
    }

    public List<ComparisonNode> getWhere() {
        return where;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
