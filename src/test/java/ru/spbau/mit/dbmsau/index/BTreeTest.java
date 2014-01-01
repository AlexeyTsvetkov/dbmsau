package ru.spbau.mit.dbmsau.index;

import org.junit.Test;
import ru.spbau.mit.dbmsau.BaseTest;
import ru.spbau.mit.dbmsau.index.btree.BTree;
import ru.spbau.mit.dbmsau.index.btree.TreeTuple;
import ru.spbau.mit.dbmsau.pages.Page;
import ru.spbau.mit.dbmsau.relation.Type;

import static org.hamcrest.CoreMatchers.is;

public class BTreeTest extends BaseTest {

    @Test
    public void testBTreePutGet() {
        Type[] valueTypes = new Type[]{Type.getIntegerType(), Type.getIntegerType()};
        Type[] keyTypes = new Type[]{Type.getIntegerType()};
        Page rootPage = context.getPageManager().allocatePage();
        int rootPageId = rootPage.getId();
        context.getPageManager().releasePage(rootPage);

        BTree bTree = new BTree(keyTypes, valueTypes, rootPageId, context);
        bTree.initFirstTime();

        int n = 10000;
        for (int i = 0; i < n; ++i) {
            TreeTuple key = TreeTuple.getOneIntTuple(i);
            TreeTuple val = TreeTuple.getTwoIntTuple(2 * i, 2 * i + 1);
            bTree.put(key, val);
        }

        for (int i = 0; i < n; ++i) {
            TreeTuple key = TreeTuple.getOneIntTuple(i);
            TreeTuple res = bTree.get(key);

            int first = res.getInteger(0);
            int second = res.getInteger(4);

            assertThat(first, is(2 * i));
            assertThat(second, is(2 * i + 1));
        }
    }

}
