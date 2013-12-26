package ru.spbau.mit.dbmsau.index;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.table.Table;

import static org.hamcrest.CoreMatchers.is;

public class IndexManagerTest extends BaseTest {
    private Table getTestTable() {
        return context.getTableManager().getTable("test");
    }

    @Test
    public void testSearchIndexedRecordSet() throws Exception {
        RecordSet indexedRecordSet = context.
                getIndexManager().
                buildIndexedRecordSetIfPossible(
                        getTestTable(),
                        new String[]{"name", "id"},
                        new String[]{"abc", "1"}
                );

        assertNotNull(indexedRecordSet);
        assertThat(indexedRecordSet.toString(), is("1, abc"));

        indexedRecordSet = context.
                getIndexManager().
                buildIndexedRecordSetIfPossible(
                        getTestTable(),
                        new String[]{"id", "secondname"},
                        new String[]{"1", "abc"}
                );

        assertNotNull(indexedRecordSet);
        assertThat(indexedRecordSet.toString(), is("1"));

        indexedRecordSet = context.
                getIndexManager().
                buildIndexedRecordSetIfPossible(
                        getTestTable(),
                        new String[]{"name", "secondname"},
                        new String[]{"1", "abc"}
                );

        assertNull(indexedRecordSet);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        FileUtils.copyFile(
                FileUtils.toFile(getClass().getResource("test.tbl")),
                tempFolder.newFile("test.tbl")
        );

        FileUtils.copyFile(
                FileUtils.toFile(getClass().getResource("test_index.idx")),
                tempFolder.newFile("test_index.idx")
        );

        super.setUp();
    }
}
