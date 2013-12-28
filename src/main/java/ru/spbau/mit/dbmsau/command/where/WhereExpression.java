package ru.spbau.mit.dbmsau.command.where;

import ru.spbau.mit.dbmsau.relation.Relation;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.relation.WhereMatcher;

import java.util.List;

public class WhereExpression implements WhereMatcher {
    private List<ComparisonClause> clauses;

    public WhereExpression(List<ComparisonClause> clauses) {
        this.clauses = clauses;
    }

    @Override
    public boolean matches(RelationRecord record) {
        for (ComparisonClause clause : clauses) {
            if (!clause.matches(record)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void prepareFor(Relation relation) {
        for (ComparisonClause clause : clauses) {
            clause.prepareFor(relation);
        }
    }
}
