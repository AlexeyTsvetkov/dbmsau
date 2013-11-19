package ru.spbau.mit.dbmsau.command;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.table.Column;
import ru.spbau.mit.dbmsau.table.StubTableManager;
import ru.spbau.mit.dbmsau.table.TableManager;
import ru.spbau.mit.dbmsau.table.Type;

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

    @Override
    protected TableManager buildTableManager(Context context) {
        return new StubTableManager(context);
    }
}
