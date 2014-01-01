package ru.spbau.mit.dbmsau;

import junitx.util.PrivateAccessor;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.spbau.mit.dbmsau.command.*;
import ru.spbau.mit.dbmsau.command.where.ComparisonClause;
import ru.spbau.mit.dbmsau.command.where.WhereExpression;
import ru.spbau.mit.dbmsau.relation.ColumnAccessor;
import ru.spbau.mit.dbmsau.syntax.SyntaxAnalyzer;
import ru.spbau.mit.dbmsau.syntax.exception.SyntaxErrors;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class SyntaxAnalyzerTest extends Assert {

    private Iterator<AbstractSQLCommand> getResults(String query) {
        return new SyntaxAnalyzer(query);
    }

    private AbstractSQLCommand getFirstResult(String query) {
        Iterator<AbstractSQLCommand> all = getResults(query);

        assertTrue(all.hasNext());

        AbstractSQLCommand result = all.next();

        assertNotNull(result);

        return result;
    }

    @Test
    public void testCreateTable() {
        CreateTableCommand command = (CreateTableCommand) getFirstResult("CREATE TABLE TEST ( id integer, name VARCHAR(50) ); ");

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
        InsertCommand command = (InsertCommand) getFirstResult("INSERT INTO test (id, name) VALUES(1,'2');");

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
        SelectCommand command = (SelectCommand) getFirstResult("SELECT * FROM test;");

        assertThat(command.getTableName(), is("test"));
        assertNull(PrivateAccessor.getField(command, "join"));
    }

    private List<ComparisonClause> getClausesFromConditionalCommand(ConditionalCommand command) throws Exception {
        WhereExpression where = (WhereExpression) PrivateAccessor.getField(command, "where");
        assertNotNull(where);

        List<ComparisonClause> clauses = (List<ComparisonClause>) PrivateAccessor.getField(where, "clauses");

        return clauses;
    }

    @Test
    public void testSelectWhere() throws Exception {
        SelectCommand command = (SelectCommand) getFirstResult("SELECT * FROM test WHERE id=2;");

        assertThat(command.getTableName(), is("test"));

        List<ComparisonClause> clauses = getClausesFromConditionalCommand(command);
        assertThat(clauses.size(), is(1));
        ComparisonClause clause = clauses.get(0);

        assertThat(clause.toString(), is("id=2"));
    }

    @Test
    public void testSelectWhereClauses() throws Exception {
        SelectCommand command = (SelectCommand) getFirstResult(
            "SELECT * FROM test WHERE " +
                "id<=2 and " +
                "test.name='abc' AND " +
                "test.id < 3 AND " +
                "test.name <> 2 AND test.id < 'cde' AND 3 >= name;"
        );

        assertThat(command.getTableName(), is("test"));

        List<ComparisonClause> clauses = getClausesFromConditionalCommand(command);
        assertThat(clauses.size(), is(6));

        assertThat(clauses.get(0).toString(), is("id<=2"));
        assertThat(clauses.get(1).toString(), is("test.name=abc"));

        assertThat(clauses.get(2).toString(), is("test.id<3"));
        assertThat(clauses.get(3).toString(), is("test.name<>2"));
        assertThat(clauses.get(4).toString(), is("test.id<cde"));
        assertThat(clauses.get(5).toString(), is("name<=3"));
    }

    @Test
    public void testSelectAccessors() throws Exception {
        SelectCommand command = (SelectCommand) getFirstResult("SELECT test.name, id FROM test;");

        assertThat(command.getTableName(), is("test"));

        List<ColumnAccessor> columnAccessorList = (List<ColumnAccessor>) PrivateAccessor.getField(command, "columnAccessors");
        assertNotNull(columnAccessorList);

        assertThat(columnAccessorList.get(0).toString(), is("test.name"));
        assertThat(columnAccessorList.get(1).toString(), is("id"));
    }

    @Test
    public void testJoin() throws Exception {
        SelectCommand command = (SelectCommand) getFirstResult("SELECT * FROM test JOIN test ON test.id = test.name WHERE id=2;");

        JoinDescription join = (JoinDescription) PrivateAccessor.getField(command, "join");

        assertThat(join.getTableName(), is("test"));
        assertThat(join.getLeft().toString(), is("test.id"));
        assertThat(join.getRight().toString(), is("test.name"));

        List<ComparisonClause> clauses = getClausesFromConditionalCommand(command);
        assertThat(clauses.size(), is(1));

        assertThat(clauses.get(0).toString(), is("id=2"));
    }

    @Test
    public void testSelectSyntaxError() throws Exception {
        thrown.expect(SyntaxErrors.class);
        thrown.expectMessage("Illegal character <#>");
        getFirstResult("SELECT #* FROM 124;");
    }

    @Test
    public void testDelete() throws Exception {
        DeleteCommand command = (DeleteCommand) getFirstResult("delete FROM test;");

        assertThat(command.getTableName(), is("test"));
    }

    @Test
    public void testDeleteWhere() throws Exception {
        DeleteCommand command = (DeleteCommand) getFirstResult("delete FROM test WHERE id=2;");

        assertThat(command.getTableName(), is("test"));

        List<ComparisonClause> clauses = getClausesFromConditionalCommand(command);
        assertThat(clauses.size(), is(1));
        assertThat(clauses.get(0).toString(), is("id=2"));
    }

    @Test
    public void testDeleteSyntaxError() throws Exception {
        thrown.expect(SyntaxErrors.class);
        thrown.expectMessage("Syntax error at: 'values' at line 1, column 13");
        getFirstResult("delete FROM values;");
    }

    @Test
    public void testUpdate() throws Exception {
        UpdateCommand command = (UpdateCommand) getFirstResult("UPDATE TEST SET id = 1,test.name='b';");

        assertThat((String) PrivateAccessor.getField(command, "tableName"), is("test"));

        List<ColumnAccessor> columnAccessors = (List<ColumnAccessor>) PrivateAccessor.getField(command, "columnAccessors");
        assertThat(columnAccessors.size(), is(2));
        assertThat(columnAccessors.get(0).toString(), is("id"));
        assertThat(columnAccessors.get(1).toString(), is("test.name"));

        List<String> values = (List<String>) PrivateAccessor.getField(command, "values");
        assertThat(values.size(), is(2));
        assertThat(values.get(0), is("1"));
        assertThat(values.get(1), is("b"));

        assertNull(PrivateAccessor.getField(command, "where"));
    }

    @Test
    public void testUpdateWhere() throws Exception {
        UpdateCommand command = (UpdateCommand) getFirstResult("UPDATE test SET id = 1,test.name='b' WHERE id=2;");

        assertThat((String) PrivateAccessor.getField(command, "tableName"), is("test"));

        List<ColumnAccessor> columnAccessors = (List<ColumnAccessor>) PrivateAccessor.getField(command, "columnAccessors");
        assertThat(columnAccessors.size(), is(2));
        assertThat(columnAccessors.get(0).toString(), is("id"));
        assertThat(columnAccessors.get(1).toString(), is("test.name"));

        List<String> values = (List<String>) PrivateAccessor.getField(command, "values");
        assertThat(values.size(), is(2));
        assertThat(values.get(0), is("1"));
        assertThat(values.get(1), is("b"));

        List<ComparisonClause> clauses = getClausesFromConditionalCommand(command);
        assertThat(clauses.size(), is(1));
        assertThat(clauses.get(0).toString(), is("id=2"));
    }

    @Test
    public void testUpdateSyntaxError() throws Exception {
        thrown.expect(SyntaxErrors.class);
        thrown.expectMessage("Syntax error at: 'where' at line 1, column 22");
        getFirstResult("UPDATE TEST SET A=0, WHERE a=1");
    }

    @Test
    public void testCreateIndex() throws Exception {
        CreateIndexCommand command = (CreateIndexCommand) getFirstResult(
            "CREATE INDEX test_index ON test (id, name);"
        );

        assertThat((String) PrivateAccessor.getField(command, "name"), is("test_index"));
        assertThat((String) PrivateAccessor.getField(command, "tableName"), is("test"));

        List<String> columns = (List<String>) PrivateAccessor.getField(command, "columns");

        assertThat(columns.size(), is(2));
        assertThat(columns.get(0), is("id"));
        assertThat(columns.get(1), is("name"));
    }

    @Test
    public void testCreateIndexError() throws Exception {
        thrown.expect(SyntaxErrors.class);
        thrown.expectMessage("Syntax error at: ')' at line 1, column 34");
        getFirstResult("CREATE INDEX test on test_index ()");
    }

    @Test
    public void testLoadDump() throws Exception {
        LoadDumpCommand command = (LoadDumpCommand) getFirstResult(
            "LOAD DUMP FOR test FROM '/home/test.dump'"
        );

        assertThat((String) PrivateAccessor.getField(command, "tableName"), is("test"));
        assertThat((String) PrivateAccessor.getField(command, "path"), is("/home/test.dump"));
    }

    @Test
    public void testLoadDumpError() throws Exception {
        thrown.expect(SyntaxErrors.class);
        thrown.expectMessage("Syntax error at: 'test' at line 1, column 11");
        getFirstResult("LOAD DUMP test FROM '/home/test.dump'");
    }
}
