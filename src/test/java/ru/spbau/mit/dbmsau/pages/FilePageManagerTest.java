package ru.spbau.mit.dbmsau.pages;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerInitException;

import java.nio.ByteBuffer;

public class FilePageManagerTest extends BaseTest {

    @Test
    public void testInit() throws PageManagerInitException {
        Context context = new Context(tempFolder.getRoot().getPath());

        FilePageManager newPageManager = new FilePageManager(context);

        context.setPageManager(newPageManager);

        newPageManager.init();

        ByteBuffer buffer = newPageManager.getPageById(0).getByteBuffer();

        assertThat(buffer.getShort(0), is(Integer.valueOf(-1).shortValue()));
    }

    @Test
    public void testInitFail() throws PageManagerInitException {
        thrown.expect(PageManagerInitException.class);

        Context context = new Context("/asddasdd");

        FilePageManager newPageManager = new FilePageManager(context);

        context.setPageManager(newPageManager);

        newPageManager.init();
    }

    @Test
    public void testAll() throws Exception {
        setUpContext();

        int[] ids = new int[4000];

        for (int i = 0; i < 4000; i++) {
            Page p = context.getPageManager().allocatePage();
            p.getByteBuffer().putInt(0, i);
            context.getPageManager().savePage(p);
            ids[i] = p.getId();
        }

        setUpContext();

        for (int i = 0; i < 4000; i++) {
            Page p = context.getPageManager().getPageById(ids[i]);
            assertThat(p.getByteBuffer().getInt(0), is(i));
            context.getPageManager().freePage(p.getId());
        }
    }

    @Override
    protected PageManager buildPageManager(Context context) {
        return new FilePageManager(context);
    }

    @Before
    public void setUp() throws Exception {

    }
}
