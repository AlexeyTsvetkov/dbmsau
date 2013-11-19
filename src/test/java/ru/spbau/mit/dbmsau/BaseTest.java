package ru.spbau.mit.dbmsau;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.dbmsau.command.AbstractSQLCommand;
import ru.spbau.mit.dbmsau.pages.PageManager;
import ru.spbau.mit.dbmsau.pages.StubPageManager;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerInitException;
import ru.spbau.mit.dbmsau.syntax.SyntaxAnalyzer;
import ru.spbau.mit.dbmsau.table.FileTableManager;
import ru.spbau.mit.dbmsau.table.RecordManager;
import ru.spbau.mit.dbmsau.table.TableManager;

import java.io.FileInputStream;

public class BaseTest extends Assert {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected Context context;

    protected Context buildContext() {
        Context context = new Context(tempFolder.getRoot().getPath());
        context.setPageManager(buildPageManager(context));
        context.setTableManager(buildTableManager(context));
        context.setRecordManager(new RecordManager(context));

        try {
            context.init();
        } catch (PageManagerInitException e) {
            assertTrue(false);
        }

        return context;
    }

    protected PageManager buildPageManager(Context context) {
        return new StubPageManager(context);
    }

    protected TableManager buildTableManager(Context context) {
        return new FileTableManager(context);
    }


    @Before
    public void setUp() throws Exception {
        setUpContext();
    }

    protected void setUpContext() throws Exception {
        context = buildContext();
        initSQLDumpLoad();
    }
    
    protected void initSQLDumpLoad() throws Exception {
        String res = getInitSQLDumpResourceName();
        if (res != null) {
            SyntaxAnalyzer analyzer = new SyntaxAnalyzer(new FileInputStream(
                    FileUtils.toFile(getClass().getResource(res))
            ));

            for (AbstractSQLCommand command : analyzer) {
                command.setContext(context);
                command.execute();
            }
        }
    }
    
    protected String getInitSQLDumpResourceName() {
        return null;
    }
}
