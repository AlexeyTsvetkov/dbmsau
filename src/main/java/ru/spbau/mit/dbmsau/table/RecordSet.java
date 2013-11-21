package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.pages.Page;
import ru.spbau.mit.dbmsau.pages.PagesList;
import ru.spbau.mit.dbmsau.pages.Record;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class RecordSet implements Iterable<TableRecord>, Iterator<TableRecord> {
    private Table table;
    private PagesList fullPages, notFullPages;

    private Queue<PagesList> pagesLists = new LinkedList<>();
    private Iterator<Page> currentListIterator;
    private Iterator<Record> currentPageIterator = null;

    public RecordSet(Table table, PagesList fullPages, PagesList notFullPages) {
        this.table = table;
        this.fullPages = fullPages;
        this.notFullPages = notFullPages;
    }

    @Override
    public Iterator<TableRecord> iterator() {
        pagesLists.clear();

        pagesLists.add(fullPages);
        pagesLists.add(notFullPages);

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
                    continue;
                }
            } else {
                 currentPageIterator = new TableRecordsPage(table, currentListIterator.next()).iterator();
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

    @Override
    public void remove() {

    }
}