package ru.spbau.mit.dbmsau.pages;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.pages.exception.PageManagerInitException;

import java.lang.reflect.Field;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

public class FilePageManagerTest extends BaseTest {

    @Test
    public void testInit() throws PageManagerInitException {
        Context context = new Context(tempFolder.getRoot().getPath());

        FilePageManager newPageManager = new FilePageManager(context);

        context.setPageManager(newPageManager);

        newPageManager.init();

        DataHolder buffer = newPageManager.getPageById(0, true).getByteBuffer();

        assertThat(buffer.get(0), is(Integer.valueOf(-1).byteValue()));
    }

    @Test
    public void testInitFail() throws PageManagerInitException {
        thrown.expect(PageManagerInitException.class);

        Context context = new Context("/asddasdd");

        FilePageManager newPageManager = new FilePageManager(context);

        context.setPageManager(newPageManager);

        newPageManager.init();
    }

    private void checkCacheSize() throws Exception {
        FilePageManager manager = (FilePageManager) context.getPageManager();

        Field f = FilePageManager.class.getDeclaredField("cache");
        f.setAccessible(true);

        Map<Integer, Page> cache = (Map<Integer, Page>) f.get(manager);

        f = FilePageManager.class.getDeclaredField("MAX_PAGES_IN_CACHE");
        f.setAccessible(true);

        assertThat(
            cache.size(),
            is(
                Matchers.lessThanOrEqualTo(
                    (Integer) f.get(null)
                )
            )
        );
    }

    @Test
    public void testAll() throws Exception {
        setUpContext();

        int[] ids = new int[4000];

        for (int i = 0; i < 4000; i++) {
            Page p = context.getPageManager().allocatePage();
            checkCacheSize();
            p.getByteBuffer().putInt(0, i);
            context.getPageManager().releasePage(p);
            checkCacheSize();
            ids[i] = p.getId();
        }

        checkBusyPages();

        checkCacheSize();

        setUpContext();

        for (int i = 0; i < 4000; i++) {
            Page p = context.getPageManager().getPageById(ids[i], true);
            checkCacheSize();
            assertThat(p.getByteBuffer().getInt(0), is(i));
            context.getPageManager().freePage(p.getId());
            checkCacheSize();
        }

        checkBusyPages();
    }

    @Override
    protected PageManager buildPageManager(Context context) {
        return new FilePageManager(context);
    }

    @Before
    public void setUp() throws Exception {

    }
}
