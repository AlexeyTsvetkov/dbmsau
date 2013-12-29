package ru.spbau.mit.dbmsau.command;

import com.google.common.collect.Lists;
import com.sun.deploy.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.relation.ColumnAccessor;
import ru.spbau.mit.dbmsau.relation.TestChildrenTable;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class SelectCommandJoinTest extends BaseTest {
    private Object[] queryResultOutput(SQLCommandResult result) {
        return Lists.newArrayList(result.iterator()).toArray();
    }

    private String[] shouldBe() {
        String[][] content = TestChildrenTable.getJoinResult();

        String[] result = new String[content.length + 1];

        result[0] = "test_children.id:integer;test_children.value:integer;test.id:integer;test.name:varchar(50)";

        for (int i = 0; i < content.length; i++) {
            String current = StringUtils.join(Arrays.asList(content[i]), ";");
            result[i + 1] = current;
        }

        return result;
    }

    @Test
    public void testJoin() throws Exception {
        SelectCommand command = new SelectCommand(
            Arrays.asList(TestChildrenTable.getResultColumnAccessors()),
            "test",
            null,
            new JoinDescription(
                TestChildrenTable.TABLE_NAME,
                new ColumnAccessor("test", "id"),
                new ColumnAccessor(TestChildrenTable.TABLE_NAME, TestChildrenTable.COLUMN_NAME_TEST_ID)
            )
        );

        command.setContext(context);
        SQLCommandResult result = command.execute();

        assertTrue(result.isIterable());

        Object[] resultOutput = queryResultOutput(result);


        assertArrayEquals(
            shouldBe(),
            resultOutput
        );
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        String initDump = FileUtils.readFileToString(getResourceFileByName("create_test_children.sql"));

        for (String[] testRow : TestChildrenTable.testContent) {
            initDump += String.format("INSERT INTO test (id, name) VALUES(%s, '%s');\n", testRow);
        }

        for (String[] testChildrenRow : TestChildrenTable.testChildrenContent) {
            initDump += String.format("INSERT INTO test_children (id, test_id, value) VALUES(%s, %s, %s);\n", testChildrenRow);
        }

        initSQLDumpLoad(new ByteArrayInputStream(initDump.getBytes()));
    }
}
