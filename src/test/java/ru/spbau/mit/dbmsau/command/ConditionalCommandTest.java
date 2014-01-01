package ru.spbau.mit.dbmsau.command;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;
import ru.spbau.mit.dbmsau.command.where.ComparisonClause;
import ru.spbau.mit.dbmsau.command.where.WhereExpression;
import ru.spbau.mit.dbmsau.index.IndexManager;
import ru.spbau.mit.dbmsau.relation.ColumnAccessor;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.table.FullScanRecordSet;
import ru.spbau.mit.dbmsau.table.Table;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;

public class ConditionalCommandTest extends BaseTest {
    @Test
    public void testIndexChoice() throws Exception {
        Table table = context.getTableManager().getTable("test");

        StubConditionalCommand command = new StubConditionalCommand(null);
        command.setContext(context);

        assertTrue(command.createAppropriateRecordSet(table) instanceof FullScanRecordSet);

        command = new StubConditionalCommand(
            new WhereExpression(
                Arrays.asList(
                    new ComparisonClause(new ColumnAccessor("test", "id"), "1", "="),
                    new ComparisonClause(new ColumnAccessor("test", "name"), "2", ">"),
                    new ComparisonClause(new ColumnAccessor("test", "id"), "3", "<"),
                    new ComparisonClause(new ColumnAccessor("test", "secondname"), "abc", "="),
                    new ComparisonClause(new ColumnAccessor("test", "name"), "5", "=")
                )
            )
        );
        command.setContext(context);

        RecordSet recordSet = command.createAppropriateRecordSet(table);
        assertFalse(recordSet instanceof FullScanRecordSet);

        assertThat(recordSet.toString(), is("[1,1], [5,5]"));
    }

    private class StubConditionalCommand extends ConditionalCommand {
        private StubConditionalCommand(WhereExpression where) {
            super(where);
        }

        @Override
        public RecordSet createAppropriateRecordSet(Table table) {
            return super.createAppropriateRecordSet(table);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected SQLCommandResult doExecute() throws CommandExecutionException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    @Override
    @Before
    public void setUp() throws Exception {
        FileUtils.copyFile(
            FileUtils.toFile(IndexManager.class.getResource("test.tbl")),
            tempFolder.newFile("test.tbl")
        );

        FileUtils.copyFile(
            FileUtils.toFile(IndexManager.class.getResource("test_index.idx")),
            tempFolder.newFile("test_index.idx")
        );

        super.setUp();
    }
}
