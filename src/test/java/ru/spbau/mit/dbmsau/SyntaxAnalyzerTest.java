package ru.spbau.mit.dbmsau;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.spbau.mit.dbmsau.command.AbstractSQLCommand;
import ru.spbau.mit.dbmsau.command.create_table.CreateTableCommand;
import ru.spbau.mit.dbmsau.syntax.SyntaxAnalyzer;
import ru.spbau.mit.dbmsau.syntax.exception.SyntaxErrors;

import java.util.Iterator;

public class SyntaxAnalyzerTest extends Assert {

    private Iterator< AbstractSQLCommand > getResults(String query) {
         return new SyntaxAnalyzer(query);
    }

    private AbstractSQLCommand getFirstResult(String query) {
        Iterator< AbstractSQLCommand > all = getResults(query);

        assertTrue(all.hasNext());

        AbstractSQLCommand result = all.next();

        assertNotNull(result);

        return result;
    }

    @Test
    public void testCreateTable() {
        CreateTableCommand command = (CreateTableCommand)getFirstResult("CREATE TABLE TEST ( id integer, name VARCHAR(50) ); ");

        assertThat(command.getTableName(), is("test"));
        assertThat(command.getColumns().size(), is(2));

        assertThat(command.getColumns().get(0).toString(), is("id:integer"));
        assertThat(command.getColumns().get(1).toString(), is("name:varchar(50)"));
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCreateTableSyntaxError() {
        thrown.expect(SyntaxErrors.class);
        thrown.expectMessage("Syntax error at: ')' at line 1, column 51");

        getFirstResult("CREATE TABLE TEST ( id integer, name VARCHAR(50), ); ");
    }
}
