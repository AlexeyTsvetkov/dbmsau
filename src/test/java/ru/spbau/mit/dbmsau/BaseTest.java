package ru.spbau.mit.dbmsau;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.dbmsau.pages.PageManager;
import ru.spbau.mit.dbmsau.pages.StubPageManager;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerInitException;

public class BaseTest extends  Assert {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected Context context;

    protected Context buildContext() {
        Context context = new Context(tempFolder.getRoot().getPath());
        context.setPageManager(buildPageManager(context));

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

    @Before
    public void setUp() throws Exception {
        setUpContext();
    }

    protected void setUpContext() {
        context = buildContext();
    }
}
