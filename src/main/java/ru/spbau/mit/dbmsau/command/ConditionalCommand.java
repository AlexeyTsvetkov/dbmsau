package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.where.ComparisonClause;
import ru.spbau.mit.dbmsau.command.where.WhereExpression;
import ru.spbau.mit.dbmsau.index.Index;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.WhereMatcherRecordSet;
import ru.spbau.mit.dbmsau.table.Table;

import java.util.List;

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

    private RecordSet buildFullScanRecordSet(Table table) {
        return getContext().getTableRecordManager().select(table);
    }

    protected RecordSet createAppropriateRecordSet(Table table) {
        if (where == null) {
            return buildFullScanRecordSet(table);
        }

        where.prepareFor(table);

        List<ComparisonClause> indexCandidates = where.getIndexCandidates();

        if (indexCandidates.size() == 0) {
            return buildFullScanRecordSet(table);
        }

        int[] candidatesIndexes = new int[indexCandidates.size()];

        for (int i = 0; i < candidatesIndexes.length; i++) {
            candidatesIndexes[i] = indexCandidates.get(i).getColumnIndex();
        }

        Index index = getContext().getIndexManager().findAppropriateIndex(table, candidatesIndexes);

        if (index == null) {
            return buildFullScanRecordSet(table);
        }

        String[] values = new String[indexCandidates.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = indexCandidates.get(i).getValue();
        }

        return index.buildRecordSetMatchingEqualityCondition(candidatesIndexes, values);
    }
}
