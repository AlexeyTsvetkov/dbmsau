package ru.spbau.mit.dbmsau;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.dbmsau.pages.PageManager;
import ru.spbau.mit.dbmsau.pages.StubPageManager;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerInitException;
import ru.spbau.mit.dbmsau.table.StubTableManager;
import ru.spbau.mit.dbmsau.table.TableManager;

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
        return new StubTableManager(context);
    }


    @Before
    public void setUp() throws Exception {
        setUpContext();
    }

    protected void setUpContext() {
        context = buildContext();
    }

    protected void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }
}
