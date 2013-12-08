package ru.spbau.mit.dbmsau.command;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.table.Column;
import ru.spbau.mit.dbmsau.table.StubTableManager;
import ru.spbau.mit.dbmsau.table.TableManager;
import ru.spbau.mit.dbmsau.table.Type;
import ru.spbau.mit.dbmsau.table.exception.SemanticError;
import ru.spbau.mit.dbmsau.table.exception.TableManagerException;

import java.util.ArrayList;

public class CreateTableCommandTest extends BaseTest {
    @Test
    public void testExecute() throws Exception {
        ArrayList<Column> columns = new ArrayList<>();

        columns.add(new Column("id", Type.getType(Type.TYPE_IDENTIFIER_INTEGER)));

        CreateTableCommand command = new CreateTableCommand("test", columns);
        command.setContext(context);

        assertNull(context.getTableManager().getTable("test"));
        command.execute();
        assertThat(context.getTableManager().getTable("test").getName(), is("test"));
    }

    @Test
    public void testColumnsUnique() throws Exception {
        thrown.expect(SemanticError.class);
        thrown.expectMessage("Column `id` referenced more than once");

        Column column = new Column("id", Type.getType(Type.TYPE_IDENTIFIER_INTEGER));

        ArrayList<Column> columns = new ArrayList<>();
        columns.add(column);
        columns.add(column);

        CreateTableCommand command = new CreateTableCommand("test", columns);
        command.setContext(context);

        assertNotNull(command.execute());
    }

    @Test
    public void testAlreadyExists() throws Exception {
        thrown.expect(SemanticError.class);
        thrown.expectMessage("Table `test` already exists");

        ArrayList<Column> columns = new ArrayList<>();

        columns.add(new Column("id", Type.getType(Type.TYPE_IDENTIFIER_INTEGER)));

        CreateTableCommand command = new CreateTableCommand("test", columns);
        command.setContext(context);

        command.execute();
        command.execute();
    }

    @Override
    protected TableManager buildTableManager(Context context) {
        return new StubTableManager(context);
    }
}
