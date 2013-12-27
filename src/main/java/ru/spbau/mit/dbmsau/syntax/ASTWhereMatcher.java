package ru.spbau.mit.dbmsau.syntax;

import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.relation.WhereMatcher;
import ru.spbau.mit.dbmsau.syntax.ast.ASTNode;
import ru.spbau.mit.dbmsau.syntax.ast.ASTNodeVisitor;
import ru.spbau.mit.dbmsau.syntax.ast.ComparisonNode;

public class ASTWhereMatcher extends ASTNodeVisitor implements WhereMatcher {
    private ASTNode condition;
    private RelationRecord currentRecord;
    private boolean lastResult;

    public ASTWhereMatcher(ASTNode condition) {
        this.condition = condition;
    }

    @Override
    public boolean matches(RelationRecord record) {
        currentRecord = record;
        condition.accept(this);

        return lastResult;
    }

    @Override
    public void visit(ComparisonNode node) {
        int columnIndex = currentRecord.getRelation().getColumnIndex(node.getIdentifier().getLexemeValue());
        lastResult = currentRecord.
            getValueAsString(columnIndex).
            equals(node.getRValue().getLexemeValue());
    }
}
