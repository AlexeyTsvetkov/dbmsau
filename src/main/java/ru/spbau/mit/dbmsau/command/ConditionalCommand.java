package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.where.WhereExpression;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.WhereMatcherRecordSet;

abstract public class ConditionalCommand extends AbstractSQLCommand {
    private WhereExpression where;

    protected ConditionalCommand(WhereExpression where) {
        this.where = where;
    }

    protected RecordSet filterRecordSet(RecordSet recordSet) {
        if (where == null) {
            return recordSet;
        }

        where.prepareFor(recordSet.getRelation());

        return new WhereMatcherRecordSet(recordSet, where);
    }
}
