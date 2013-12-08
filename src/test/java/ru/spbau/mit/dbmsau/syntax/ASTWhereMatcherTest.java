package ru.spbau.mit.dbmsau.syntax;

import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.pages.Record;
import ru.spbau.mit.dbmsau.pages.RecordsPage;
import ru.spbau.mit.dbmsau.syntax.ast.ComparisonNode;
import ru.spbau.mit.dbmsau.syntax.ast.TerminalNode;
import ru.spbau.mit.dbmsau.table.TableRecord;

public class ASTWhereMatcherTest extends BaseTest {
    private TableRecord buildStubTestTableRecord() {
        return new StubTableRecord(context.getTableManager().getTable("test"));
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

        TableRecord record = buildStubTestTableRecord();

        record.setValue("id", "2");
        record.setValue("name", "abc");

        assertTrue(matcher.matches(record));

        record.setValue("id", "1");

        assertFalse(matcher.matches(record));
    }
}
