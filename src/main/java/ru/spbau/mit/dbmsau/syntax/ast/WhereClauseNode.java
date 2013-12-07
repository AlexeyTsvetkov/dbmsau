package ru.spbau.mit.dbmsau.syntax.ast;

import java.util.List;

public class WhereClauseNode extends ASTNode {

    private List<ComparisonNode> comparisons;

    public WhereClauseNode(List<ComparisonNode> comparisons) {
        this.comparisons = comparisons;
    }

    public List<ComparisonNode> getComparisons() {
        return comparisons;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
