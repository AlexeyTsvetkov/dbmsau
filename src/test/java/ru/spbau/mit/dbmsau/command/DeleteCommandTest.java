package ru.spbau.mit.dbmsau.command;

import org.junit.Test;
import ru.spbau.mit.dbmsau.command.where.ComparisonClause;
import ru.spbau.mit.dbmsau.command.where.WhereExpression;
import ru.spbau.mit.dbmsau.relation.ColumnAccessor;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TestTableTest;

import java.util.Arrays;

public class DeleteCommandTest extends TestTableTest {
    @Test
    public void testDelete() throws Exception {
        initSQLDumpLoad(Table.class, "create_insert_test.sql");

        String[][] shouldBe = new String[][]{
        };

        DeleteCommand command = new DeleteCommand("test", null);
        command.setContext(context);
        command.execute();

        compareTestContent(shouldBe);
    }

    @Test
    public void testDeleteConditional() throws Exception {
        initSQLDumpLoad(Table.class, "create_insert_test.sql");

        String[][] shouldBe = new String[][]{
            new String[]{"3", "cde"},
            new String[]{"5", "efg"}
        };

        DeleteCommand command = new DeleteCommand(
            "test",
            new WhereExpression(
                Arrays.asList(
                    new ComparisonClause(new ColumnAccessor(null, "id"), "1", "=")
                )
            )
        );
        command.setContext(context);
        command.execute();

        compareTestContent(shouldBe);
    }

}
