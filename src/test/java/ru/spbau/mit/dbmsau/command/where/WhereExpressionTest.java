package ru.spbau.mit.dbmsau.command.where;

import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.relation.ColumnAccessor;
import ru.spbau.mit.dbmsau.relation.MemoryRelationRecord;
import ru.spbau.mit.dbmsau.relation.Relation;

import java.util.Arrays;

public class WhereExpressionTest extends BaseTest {
    @Test
    public void testWhere() throws Exception {
        WhereExpression expression = new WhereExpression(
            Arrays.asList(
                new ComparisonClause(new ColumnAccessor("test", "name"), "abc", "="),
                new ComparisonClause(new ColumnAccessor("test", "id"), "1", "="),
                new ComparisonClause(new ColumnAccessor("test", "id"), "-1", ">="),
                new ComparisonClause(new ColumnAccessor("test", "id"), "-1", "<>")
            )
        );

        Relation relation = buildTestTable();

        expression.prepareFor(relation);
        MemoryRelationRecord record = new MemoryRelationRecord(relation);
        record.setValue(TEST_COLUMN_INDEX_ID, 1);
        record.setValue(TEST_COLUMN_INDEX_NAME, "abc");

        assertTrue(expression.matches(record));

        record.setValue(TEST_COLUMN_INDEX_NAME, "cde");
        assertFalse(expression.matches(record));

        record.setValue(TEST_COLUMN_INDEX_NAME, "abc");
        record.setValue(TEST_COLUMN_INDEX_ID, 2);
        assertFalse(expression.matches(record));

        record.setValue(TEST_COLUMN_INDEX_ID, 1);
        assertTrue(expression.matches(record));

        expression = new WhereExpression(
            Arrays.asList(
                new ComparisonClause(new ColumnAccessor("test", "id"), "1", ">")
            )
        );
        expression.prepareFor(relation);
        assertFalse(expression.matches(record));
    }
}
