package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.ContextContainer;
import ru.spbau.mit.dbmsau.pages.Page;
import ru.spbau.mit.dbmsau.pages.PagesList;
import ru.spbau.mit.dbmsau.pages.Record;
import ru.spbau.mit.dbmsau.pages.RecordsPage;
import ru.spbau.mit.dbmsau.table.exception.RecordManagerException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RecordManager extends ContextContainer {
    public RecordManager(Context context) {
        super(context);
    }

    public void init() {

    }

    public void insert(Table table, List<String> columns, List<String> values) throws RecordManagerException {
        if (columns.size() != values.size()) {
            throw new RecordManagerException("Columns and values size are not equal");
        }

        for (String column : columns) {
            if (!table.hasColumn(column)) {
                throw new RecordManagerException("No such column `" +  column + "`");
            }
        }

        PagesList notFullPagesList = buildPagesListByHeadPageId(table.getNotFullPagesListHeadPageId());

        Page pageToInsert = notFullPagesList.popPage();

        if (pageToInsert == null) {
            pageToInsert = context.getPageManager().allocatePage();
        }

        TableRecordsPage recordsPage = new TableRecordsPage(table, pageToInsert);
        TableRecord newRecord = recordsPage.getClearTableRecord();

        for (int i = 0; i < columns.size(); i++) {
            newRecord.setValue(columns.get(i), values.get(i));
        }

        context.getPageManager().savePage(recordsPage);

        if (!recordsPage.isFull()) {
            notFullPagesList.put(recordsPage.getId());
        } else {
            buildPagesListByHeadPageId(table.getFullPagesListHeadPageId()).put(recordsPage.getId());
        }
    }

    public Iterable<TableRecord> select(Table table) {
        return new RecordsIterator(table);
    }

    private PagesList buildPagesListByHeadPageId(int headPageId) {
        return new PagesList(headPageId, context);
    }

    private class RecordsIterator implements Iterable<TableRecord>, Iterator<TableRecord> {
        private Table table;
        private Queue<PagesList> pagesLists = new LinkedList<>();
        private PagesList currentList;
        private Iterator<Record> currentPageIterator = null;

        private RecordsIterator(Table table) {
            this.table = table;
        }

        @Override
        public Iterator<TableRecord> iterator() {
            pagesLists.clear();

            pagesLists.add(buildPagesListByHeadPageId(table.getFullPagesListHeadPageId()));
            pagesLists.add(buildPagesListByHeadPageId(table.getNotFullPagesListHeadPageId()));

            currentList = pagesLists.poll();
            currentPageIterator = null;
            return this;
        }

        private void moveUntilNext() {
            while (currentPageIterator == null || !currentPageIterator.hasNext()) {
                Page next = currentList.popPage();

                if (next == null) {
                    if (pagesLists.size() == 0) {
                        return;
                    } else {
                        currentList = pagesLists.poll();
                        continue;
                    }
                }

                currentPageIterator = new TableRecordsPage(table, next).iterator();
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
}
