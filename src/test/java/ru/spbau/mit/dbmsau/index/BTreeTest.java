package ru.spbau.mit.dbmsau.index;

import org.junit.Before;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.index.btree.BTree;
import ru.spbau.mit.dbmsau.index.btree.TreeTuple;
import ru.spbau.mit.dbmsau.pages.FilePageManager;
import ru.spbau.mit.dbmsau.pages.Page;
import ru.spbau.mit.dbmsau.pages.PageManager;
import ru.spbau.mit.dbmsau.relation.Type;
import ru.spbau.mit.dbmsau.table.TestTableTest;

import java.util.*;
import java.util.concurrent.ThreadFactory;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;

public class BTreeTest extends BaseTest {

    @Test
    public void testPutGetPairs() {
        Type[] valueTypes = new Type[]{Type.getIntegerType(), Type.getIntegerType()};
        Type[] keyTypes = new Type[]{Type.getIntegerType()};
        Page rootPage = context.getPageManager().allocatePage();
        int rootPageId = rootPage.getId();
        context.getPageManager().releasePage(rootPage);

        BTree bTree = new BTree(keyTypes, valueTypes, rootPageId, context);
        bTree.initFirstTime();

        int n = 100000;
        for (int i = n-24; i < n; ++i) {
            TreeTuple key = TreeTuple.getOneIntTuple(i);
            TreeTuple val = TreeTuple.getTwoIntTuple(2 * i, 2 * i + 1);
            bTree.put(key, val);
        }

        for (int i = 1; i<n-24; ++i) {
            TreeTuple key = TreeTuple.getOneIntTuple(i);
            TreeTuple val = TreeTuple.getTwoIntTuple(2 * i, 2 * i + 1);
            bTree.put(key, val);
        }

        for (int i = 1; i < n; i++) {
            TreeTuple key = TreeTuple.getOneIntTuple(i);
            TreeTuple res = bTree.get(key);

//            if (i % 2 == 1) {
//                assertNull(res);
//            } else {
                int first = res.getInteger(0);
                int second = res.getInteger(4);

                assertThat(first, is(2 * i));
                assertThat(second, is(2 * i + 1));
            //}
        }
    }

    @Override
    protected PageManager buildPageManager(Context context) {
        return new FilePageManager(context);
    }

    @Test
    public void testGetRandom() {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        BTree bTree = initWithRandom(map);

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int key = entry.getKey();
            TreeTuple res = bTree.get(TreeTuple.getOneIntTuple(key));

            assertThat(res.getInteger(0), is(entry.getValue()));
        }
    }

    @Test
    public void testLowerBound() {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        BTree bTree = initWithRandom(map);

        int n = 10000;
        Random rnd = new Random(65432);
        for (int i = 0; i < n; ++i) {
            int cur = rnd.nextInt();

            Integer expected = map.ceilingKey(cur);
            BTree.ItemLocation loc = bTree.lower_bound(TreeTuple.getOneIntTuple(cur));
            TreeTuple res = bTree.getLowerBoundKey(loc);

            if (expected == null) {
                assertNull(res);
            } else {
                assertThat(res.getInteger(0), is(expected));
            }
        }
    }

    @Test
    public void testRemove() {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        BTree bTree = initWithRandom(map);
        Random rnd = new Random(123);

        ArrayList<Integer> a = new ArrayList<>();
        a.addAll(map.keySet());
        Collections.shuffle(a, rnd);

        int t = map.size() / 10;
        for (int i = 0; i < t; ++i) {
            bTree.remove(TreeTuple.getOneIntTuple(a.get(i)));
        }

        for (int i = 0; i < a.size(); ++i) {
            TreeTuple res = bTree.get(TreeTuple.getOneIntTuple(a.get(i)));
            if (i < t) {
                assertNull(res);
            } else {
                assertThat(res.getInteger(0), is(map.get(a.get(i))));
            }
        }
    }

    public BTree initWithRandom(TreeMap<Integer, Integer> map) {
        int n = 10000;

        Type[] valueTypes = new Type[]{Type.getIntegerType()};
        Type[] keyTypes = new Type[]{Type.getIntegerType()};
        Page rootPage = context.getPageManager().allocatePage();
        int rootPageId = rootPage.getId();
        context.getPageManager().releasePage(rootPage);

        BTree bTree = new BTree(keyTypes, valueTypes, rootPageId, context);
        bTree.initFirstTime();

        Random rnd = new Random(12345);
        for (int i = 0; i < n; ++i) {
            int key = rnd.nextInt();
            int val = rnd.nextInt();

            bTree.put(TreeTuple.getOneIntTuple(key), TreeTuple.getOneIntTuple(val));
            map.put(key, val);
        }

        return bTree;
    }
}
