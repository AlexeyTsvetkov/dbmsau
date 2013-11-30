package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.pages.Page;
import ru.spbau.mit.dbmsau.pages.PagesList;
import ru.spbau.mit.dbmsau.pages.Record;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class RecordSet implements Iterable<TableRecord>, Iterator<TableRecord> {
    private Table table;
    private PagesList fullPages, notFullPages;
    private Context context;

    private Queue<PagesList> pagesLists = new LinkedList<>();
    private Iterator<Page> currentListIterator;
    private TableRecordsPage currentPage = null;
    private Iterator<Record> currentPageIterator = null;

    public RecordSet(Table table, PagesList fullPages, PagesList notFullPages, Context context) {
        this.table = table;
        this.fullPages = fullPages;
        this.notFullPages = notFullPages;
        this.context = context;
    }

    @Override
    public Iterator<TableRecord> iterator() {
        pagesLists.clear();

        pagesLists.add(notFullPages);
        pagesLists.add(fullPages);

        currentListIterator = pagesLists.poll().iterator();
        currentPageIterator = null;
        return this;
    }

    private void moveUntilNext() {
        while (currentPageIterator == null || !currentPageIterator.hasNext()) {
            if (!currentListIterator.hasNext()) {
                if (pagesLists.size() == 0) {
                    return;
                } else {
                    currentListIterator = pagesLists.poll().iterator();
                }
            } else {
                currentPage = new TableRecordsPage(table, currentListIterator.next());
                currentPageIterator = currentPage.iterator();
            }
        }
    }

    @Override
    public boolean hasNext() {
        moveUntilNext();

        return currentPageIterator != null && currentPageIterator.hasNext();
    }

    @Override
    public TableRecord next() {
        moveUntilNext();

        return new TableRecord(currentPageIterator.next(), table);
    }

    private boolean isFullPagesListProcessing() {
        return pagesLists.size() == 0;
    }

    @Override
    public void remove() {
        currentPageIterator.remove();

        //if page from full pages list -- move it to not full page list
        if (isFullPagesListProcessing()) {
            if (currentPage.isAlmostFull()) {
                currentListIterator.remove();
                if (!currentPage.isEmpty()) {
                    notFullPages.put(currentPage.getId());
                }
            }
        } else {
            //free page if it's empty
            if (currentPage.isEmpty()) {
                currentListIterator.remove();
                context.getPageManager().freePage(currentPage.getId());
            }
        }
    }
}
