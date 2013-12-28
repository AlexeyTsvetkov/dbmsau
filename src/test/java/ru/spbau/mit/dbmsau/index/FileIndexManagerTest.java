package ru.spbau.mit.dbmsau.index;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.index.exception.IndexCreationError;
import ru.spbau.mit.dbmsau.table.Table;

import java.io.File;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;

public class FileIndexManagerTest extends BaseTest {
    private final String TEST_INDEX_NAME = "test_index";
    private final String TEST_INDEX_FILE_NAME = "test_index.idx";

    private Table getTestTable() {
        return context.getTableManager().getTable("test");
    }

    private void createTestIndex() throws Exception {
        context.getIndexManager().createIndex(
            TEST_INDEX_NAME,
            getTestTable(),
            Arrays.asList("id", "name")
        );
    }

    private void checkTestIndex() throws Exception {
        assertTrue(
            context.getIndexManager().
                getIndexesForTable(getTestTable()).
                iterator().
                hasNext()
        );

        BTreeIndex index = (BTreeIndex) context.getIndexManager().
            getIndexesForTable(getTestTable()).
            iterator().
            next();

        assertThat(index.getName(), is(TEST_INDEX_NAME));
        assertThat(index.getRootPageId(), is(4));
        assertThat(index.getTable().getName(), is("test"));

        assertArrayEquals(new int[]{0, 1}, index.getColumnIndexes());

        assertTrue(
            FileUtils.contentEquals(
                getResourceFileByName(TEST_INDEX_FILE_NAME),
                new File(tempFolder.getRoot().getAbsolutePath() + "/" + TEST_INDEX_FILE_NAME)
            )
        );
    }

    @Test
    public void testCreateIndex() throws Exception {
        initSQLDumpLoad("create_insert_test.sql");

        assertFalse(
            context.getIndexManager().
                getIndexesForTable(getTestTable()).
                iterator().
                hasNext()
        );

        createTestIndex();
        checkTestIndex();
    }

    @Test
    public void testAlreadyExists() throws Exception {
        thrown.expect(IndexCreationError.class);
        thrown.expectMessage("index `test_index` already exists");

        initSQLDumpLoad("create_insert_test.sql");
        createTestIndex();
        createTestIndex();
    }

    @Test
    public void testLoading() throws Exception {
        FileUtils.copyFile(
            FileUtils.toFile(getClass().getResource("test.tbl")),
            tempFolder.newFile("test.tbl")
        );

        FileUtils.copyFile(
            FileUtils.toFile(getClass().getResource(TEST_INDEX_FILE_NAME)),
            tempFolder.newFile(TEST_INDEX_FILE_NAME)
        );

        setUpContext();
        checkTestIndex();
    }
}
