package ru.spbau.mit.dbmsau;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.spbau.mit.dbmsau.command.AbstractSQLCommand;
import ru.spbau.mit.dbmsau.command.CreateTableCommand;
import ru.spbau.mit.dbmsau.command.InsertCommand;
import ru.spbau.mit.dbmsau.command.SelectCommand;
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

    @Test
    public void testInsert() throws Exception {
        InsertCommand command = (InsertCommand)getFirstResult("INSERT INTO test (id, name) VALUES(1,'2');");

        assertThat(command.getTableName(), is("test"));
        assertThat(command.getColumns().size(), is(2));

        assertThat(command.getColumns().get(0), is("id"));
        assertThat(command.getColumns().get(1), is("name"));

        assertThat(command.getValues().get(0), is("1"));
        assertThat(command.getValues().get(1), is("2"));
    }

    @Test
    public void testInsertSyntaxError() throws Exception {
        thrown.expect(SyntaxErrors.class);
        thrown.expectMessage("Syntax error at: ',' at line 1, column 19");
        getFirstResult("INSERT INTO test (, name) (1,'2'); ");
    }

    @Test
    public void testSelect() throws Exception {
        SelectCommand command = (SelectCommand)getFirstResult("SELECT * FROM test;");

        assertThat(command.getTable(), is("test"));
    }

    @Test
    public void testSelectSyntaxError() throws Exception {
        thrown.expect(SyntaxErrors.class);
        thrown.expectMessage("Illegal character <#>");
        getFirstResult("SELECT #* FROM 124;");
    }
}
