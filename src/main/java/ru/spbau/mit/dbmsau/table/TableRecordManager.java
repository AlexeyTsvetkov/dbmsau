package ru.spbau.mit.dbmsau.table;

import ru.spbau.mit.dbmsau.Context;
import ru.spbau.mit.dbmsau.ContextContainer;
import ru.spbau.mit.dbmsau.index.exception.DuplicateKeyException;
import ru.spbau.mit.dbmsau.pages.Page;
import ru.spbau.mit.dbmsau.pages.PagesList;
import ru.spbau.mit.dbmsau.relation.RecordSet;
import ru.spbau.mit.dbmsau.relation.RelationRecord;
import ru.spbau.mit.dbmsau.table.exception.RecordManagerException;

public class TableRecordManager extends ContextContainer {
    public TableRecordManager(Context context) {
        super(context);
    }

    public void init() {

    }

    public void insert(Table table, RelationRecord record) throws RecordManagerException {
        try {
            context.getIndexManager().checkConstraintsBeforeInsert(table, record);
        } catch (DuplicateKeyException e) {
            throw new RecordManagerException(e.getMessage());
        }

        PagesList notFullPagesList = buildPagesListByHeadPageId(table.getNotFullPagesListHeadPageId());

        Page pageToInsert = notFullPagesList.popPage(true);

        if (pageToInsert == null) {
            pageToInsert = context.getPageManager().allocatePage();
        }

        TableRecordsPage recordsPage = new TableRecordsPage(table, pageToInsert);
        TableRecord newRecord = recordsPage.getClearTableRecord();

        for (int i = 0; i < table.getColumnsCount(); i++) {
            newRecord.setValueFromString(i, record.getValueAsString(i));
        }

        context.getPageManager().releasePage(recordsPage);

        if (!recordsPage.isFull()) {
            notFullPagesList.put(recordsPage.getId());
        } else {
            buildPagesListByHeadPageId(table.getFullPagesListHeadPageId()).put(recordsPage.getId());
        }

        context.getIndexManager().processNewRecord(table, newRecord);
    }

    public RecordSet select(Table table) {
        return new FullScanRecordSet(
            table,
            buildPagesListByHeadPageId(table.getFullPagesListHeadPageId()),
            buildPagesListByHeadPageId(table.getNotFullPagesListHeadPageId()),
            context
        );
    }

    private PagesList buildPagesListByHeadPageId(int headPageId) {
        return new PagesList(headPageId, context);
    }
}
