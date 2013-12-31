package ru.spbau.mit.dbmsau.command;

import org.junit.Test;
import ru.spbau.mit.dbmsau.command.where.ComparisonClause;
import ru.spbau.mit.dbmsau.command.where.WhereExpression;
import ru.spbau.mit.dbmsau.relation.ColumnAccessor;
import ru.spbau.mit.dbmsau.relation.MemoryRelationRecord;
import ru.spbau.mit.dbmsau.table.Table;
import ru.spbau.mit.dbmsau.table.TestTableTest;

import java.util.Arrays;

public class UpdateCommandTest extends TestTableTest {
    @Test
    public void testUpdate() throws Exception {
        initSQLDumpLoad(Table.class, "create_insert_test.sql");

        String[][] shouldBe = new String[][]{
            new String[]{"1", "xyz"},
            new String[]{"3", "xyz"},
            new String[]{"5", "xyz"}
        };

        UpdateCommand command = new UpdateCommand(
            "test",
            Arrays.asList(
                new ColumnAccessor("test", "name")
            ),
            Arrays.asList(
                "xyz"
            ),
            null
        );

        command.setContext(context);
        command.execute();

        compareTestContent(shouldBe);
    }

    @Test
    public void testUpdateConditional() throws Exception {
        initSQLDumpLoad(Table.class, "create_insert_test.sql");

        String[][] shouldBe = new String[][]{
            new String[]{"1", "abc"},
            new String[]{"3", "xyz"},
            new String[]{"5", "xyz"}
        };

        UpdateCommand command = new UpdateCommand(
            "test",
            Arrays.asList(
                new ColumnAccessor("test", "name")
            ),
            Arrays.asList(
                "xyz"
            ),
            new WhereExpression(
                Arrays.asList(
                    new ComparisonClause(new ColumnAccessor(null, "id"), "1", ">")
                )
            )
        );
        command.setContext(context);
        command.execute();

        compareTestContent(shouldBe);
    }

    @Test
    public void testUpdateBig() throws Exception {
        initSQLDumpLoad(Table.class, "create_test.sql");
        int rows = 6000;

        Table table = context.getTableManager().getTable("test");
        MemoryRelationRecord record = new MemoryRelationRecord(table);

        for (int i = 0; i < rows; i++) {
            record.setValue(TEST_COLUMN_INDEX_ID, i + 1);
            record.setValue(TEST_COLUMN_INDEX_NAME, "pref" + Integer.valueOf(i).toString());

            context.getTableRecordManager().insert(table, record);
        }

        UpdateCommand command = new UpdateCommand(
            "test",
            Arrays.asList(
                new ColumnAccessor("test", "name")
            ),
            Arrays.asList(
                "xyz"
            ),
            new WhereExpression(
                Arrays.asList(
                    new ComparisonClause(new ColumnAccessor(null, "id"), "4000", ">="),
                    new ComparisonClause(new ColumnAccessor(null, "id"), "5000", "<")
                )
            )
        );

        String[][] shouldBe = new String[rows][];

        for (int i = 0; i < rows; i++) {
            String name = "pref" + Integer.valueOf(i).toString();

            if ((i + 1) >= 4000 && (i + 1) < 5000) {
                name = "xyz";
            }

            shouldBe[i] = new String[]{
                Integer.valueOf(i + 1).toString(),
                name
            };
        }

        command.setContext(context);
        command.execute();

        compareTestContent(shouldBe);
    }
}
