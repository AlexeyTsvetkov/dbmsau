package ru.spbau.mit.dbmsau.pages;

import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;

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

        Set<Integer> testSet = new HashSet<>();

        for (int i = 1000; i < 5000; i++) {
            list.put(i);
            checkBusyPages();

            testSet.add(i);
        }

        for (int i = 1000; i < 5000; i++) {
            assertTrue(testSet.contains(list.peek()));
            checkBusyPages();

            int curVal = list.pop();
            assertTrue(testSet.contains(curVal));
            checkBusyPages();

            testSet.remove(curVal);
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

        Set<Integer> testSet = new HashSet<>();
        for (int i = 1000; i < 5000; i++) {
            list.put(i);
            testSet.add(i);
        }

        iterateList(list);
        iterateList(list);

        for (int i = 1000; i < 5000; i++) {
            int curVal = list.peek();

            assertTrue(testSet.contains(curVal));
            assertThat(list.pop(), is(curVal));
            testSet.remove(curVal);
        }

        assertTrue(testSet.isEmpty());
        
        assertNull(list.peek());
        assertNull(list.pop());

        checkBusyPages();
    }

    @Test
    public void testIteratorRemoving() throws Exception {
        PagesList list = new PagesList(HEAD_PAGE_ID, context);
        list.initList();

        Set<Integer> testSet = new HashSet<>();
        for (int i = 1000; i < 5000; i++) {
            list.put(i);
            testSet.add(i);
        }

        Iterator<Page> it = list.iterator();

        for (int i = 1000; i < 5000; i++) {
            assertTrue(it.hasNext());
            Page next = it.next();

            int curVal = next.getId();
            assertTrue(testSet.contains(curVal));

            if (curVal > 1000 && curVal < 4000) {
                it.remove();
                testSet.remove(curVal);
            }
        }

        it = list.iterator();

        for (int i = 1000; i < 5000; i++) {
            if (i > 1000 && i < 4000) {
                continue;
            }
            assertTrue(it.hasNext());
            Page next = it.next();

            int curVal = next.getId();
            assertTrue(testSet.contains(curVal));
            testSet.remove(curVal);
        }

        assertTrue(testSet.isEmpty());
        assertFalse(it.hasNext());
        assertFalse(it.hasNext());

        checkBusyPages();
    }


    private void iterateList(PagesList list) {
        Iterator<Page> it = list.iterator();

        Set<Integer> testSet = new HashSet<>();
        for (int i = 1000; i < 5000; i++) {
            testSet.add(i);
        }

        for (int i = 1000; i < 5000; i++) {
            assertTrue(it.hasNext());
            Page next = it.next();

            int curVal = next.getId();
            assertTrue(testSet.contains(curVal));
            testSet.remove(curVal);
        }

        assertTrue(testSet.isEmpty());

        assertFalse(it.hasNext());
        assertFalse(it.hasNext());

        checkBusyPages();
    }
}
