package ru.spbau.mit.dbmsau.syntax;

import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.syntax.ast.ComparisonNode;
import ru.spbau.mit.dbmsau.syntax.ast.TerminalNode;

public class ASTWhereMatcherTest extends BaseTest {
    private RelationRecord buildStubTestTableRecord() {
        return new StubRelationRecord(context.getTableManager().getTable("test"));
    }

    @Test
    public void testMatching() throws Exception {
        initSQLDumpLoad("create_insert_test.sql");

        ComparisonNode node = new ComparisonNode(
                new TerminalNode("id"),
                new TerminalNode("="),
                new TerminalNode("2")
        );

        ASTWhereMatcher matcher = new ASTWhereMatcher(node);

        RelationRecord record = buildStubTestTableRecord();

        record.setValue(TEST_COLUMN_INDEX_ID, "2");
        record.setValue(TEST_COLUMN_INDEX_NAME, "abc");

        assertTrue(matcher.matches(record));

        record.setValue(TEST_COLUMN_INDEX_ID, "1");

        assertFalse(matcher.matches(record));
    }
}
