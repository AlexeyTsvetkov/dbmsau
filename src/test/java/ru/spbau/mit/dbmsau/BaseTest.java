package ru.spbau.mit.dbmsau;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.dbmsau.pages.PageManager;
import ru.spbau.mit.dbmsau.pages.StubPageManager;

import java.io.FileNotFoundException;

public class BaseTest extends  Assert {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    protected Context context;

    protected Context buildContext() {
        Context context = new Context(tempFolder.getRoot().getPath());
        context.setPageManager(buildPageManager(context));

        try {
            context.init();
        } catch (FileNotFoundException e) {
            assertTrue(false);
        }

        return context;
    }

    protected PageManager buildPageManager(Context context) {
        return new StubPageManager(context);
    }

    @Before
    public void setUp() throws Exception {
         context = buildContext();
    }
}
