package ru.spbau.mit.dbmsau.pages;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.spbau.mit.dbmsau.BaseTest;

import java.util.Iterator;

public class PagesListTest extends BaseTest {
    private static final int HEAD_PAGE_ID = 100;

    @Test
    public void testList() throws Exception {
        PagesList list = new PagesList(HEAD_PAGE_ID, context);
        list.initList();
        checkBusyPages();

        assertNull(list.peek());
        checkBusyPages();
        assertNull(list.pop());
        checkBusyPages();

        list.put(1000);
        checkBusyPages();
        assertThat(list.pop(), is(1000));
        checkBusyPages();

        for (int i = 1000; i < 5000; i++) {
            list.put(i);
            checkBusyPages();
        }

        for (int i = 1000; i < 5000; i++) {
            if (i == 1990) {
                assertThat(list.peek(), is(i));
            } else {
                assertThat(list.peek(), is(i));
                checkBusyPages();
            }
            assertThat(list.pop(), is(i));
            checkBusyPages();
        }

        checkBusyPages();

        assertNull(list.peek());
        checkBusyPages();
        assertNull(list.pop());
        checkBusyPages();

        checkBusyPages();
    }

    @Test
    public void testIterator() throws Exception {
        PagesList list = new PagesList(HEAD_PAGE_ID, context);
        list.initList();

        assertNull(list.peek());
        assertNull(list.pop());

        Iterator<Page> it = list.iterator();

        assertFalse(it.hasNext());

        for (int i = 1000; i < 5000; i++) {
            list.put(i);
        }

        iterateList(list);
        iterateList(list);

        for (int i = 1000; i < 5000; i++) {
            assertThat(list.peek(), is(i));
            assertThat(list.pop(), is(i));
        }

        assertNull(list.peek());
        assertNull(list.pop());

        checkBusyPages();
    }

    @Test
    public void testIteratorRemoving() throws Exception {
        PagesList list = new PagesList(HEAD_PAGE_ID, context);
        list.initList();

        for (int i = 1000; i < 5000; i++) {
            list.put(i);
        }

        Iterator<Page> it = list.iterator();

        for (int i = 1000; i < 5000; i++) {
            assertTrue(it.hasNext());
            Page next = it.next();

            assertThat(next.getId(), is(i));

            if (i > 1000 && i < 4000) {
                it.remove();
            }
        }

        it = list.iterator();

        for (int i = 1000; i < 5000; i++) {
            if (i > 1000 && i < 4000) {
                continue;
            }
            assertTrue(it.hasNext());
            Page next = it.next();

            assertThat(next.getId(), is(i));
        }

        assertFalse(it.hasNext());
        assertFalse(it.hasNext());

        checkBusyPages();
    }


    private void iterateList(PagesList list) {
        Iterator<Page> it = list.iterator();

        for (int i = 1000; i < 5000; i++) {
            assertTrue(it.hasNext());
            Page next = it.next();

            assertThat(next.getId(), is(i));
        }

        assertFalse(it.hasNext());
        assertFalse(it.hasNext());

        checkBusyPages();
    }
}
