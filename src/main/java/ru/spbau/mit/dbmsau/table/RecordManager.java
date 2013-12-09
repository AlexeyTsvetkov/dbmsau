package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.ContextContainer;
import ru.spbau.mit.dbmsau.pages.Page;
import ru.spbau.mit.dbmsau.pages.PagesList;
import ru.spbau.mit.dbmsau.table.exception.RecordManagerException;

import java.util.Iterator;
import java.util.List;

public class RecordManager extends ContextContainer {
    public RecordManager(Context context) {
        super(context);
    }

    public void init() {

    }

    public void insert(Table table, List<String> columns, List<String> values) throws RecordManagerException {

        PagesList notFullPagesList = buildPagesListByHeadPageId(table.getNotFullPagesListHeadPageId());

        Page pageToInsert = notFullPagesList.popPage(true);

        if (pageToInsert == null) {
            pageToInsert = context.getPageManager().allocatePage();
        }

        TableRecordsPage recordsPage = new TableRecordsPage(table, pageToInsert);
        TableRecord newRecord = recordsPage.getClearTableRecord();

        for (int i = 0; i < columns.size(); i++) {
            newRecord.setValue(columns.get(i), values.get(i));
        }

        context.getPageManager().releasePage(recordsPage);

        if (!recordsPage.isFull()) {
            notFullPagesList.put(recordsPage.getId());
        } else {
            buildPagesListByHeadPageId(table.getFullPagesListHeadPageId()).put(recordsPage.getId());
        }
    }

    public RecordSet select(Table table, WhereMatcher matcher) {
        return new RecordSet(
                table,
                matcher,
                buildPagesListByHeadPageId(table.getFullPagesListHeadPageId()),
                buildPagesListByHeadPageId(table.getNotFullPagesListHeadPageId()),
                context
        );
    }

    public RecordSet select(Table table) {
        return select(table, new StubMatcher());
    }

    public void delete(Table table) {
        RecordSet recordSet = select(table, new StubMatcher());

        Iterator<TableRecord> iterator = recordSet.iterator();

        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    private PagesList buildPagesListByHeadPageId(int headPageId) {
        return new PagesList(headPageId, context);
    }
}
