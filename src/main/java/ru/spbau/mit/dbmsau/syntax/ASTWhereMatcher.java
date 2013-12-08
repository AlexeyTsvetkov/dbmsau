package ru.spbau.mit.dbmsau.syntax;

import ru.spbau.mit.dbmsau.syntax.ast.ASTNode;
import ru.spbau.mit.dbmsau.syntax.ast.ASTNodeVisitor;
import ru.spbau.mit.dbmsau.syntax.ast.ComparisonNode;
import ru.spbau.mit.dbmsau.table.TableRecord;
import ru.spbau.mit.dbmsau.table.WhereMatcher;

public class ASTWhereMatcher extends ASTNodeVisitor implements WhereMatcher {
    private ASTNode condition;
    private TableRecord currentRecord;
    private boolean lastResult;

    public ASTWhereMatcher(ASTNode condition) {
        this.condition = condition;
    }

    @Override
    public boolean matches(TableRecord record) {
        currentRecord = record;
        condition.accept(this);

        return lastResult;
    }

    @Override
    public void visit(ComparisonNode node) {
        lastResult = currentRecord.
                getValueAsString(node.getIdentifier().getLexemeValue()).
                equals(node.getRValue().getLexemeValue());
    }
}
