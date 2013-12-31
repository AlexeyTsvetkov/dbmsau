package ru.spbau.mit.dbmsau.index;

import ru.spbau.mit.dbmsau.relation.*;

public class IndexJoinRecordSet extends BaseRecordSet {
    private JoinedRelation relation;
    private RecordSet firstRecordSet;
    private Index index;

    private int firstColumnIndex;
    private int secondColumnIndex;

    private RecordSet secondRecordSet;
    private RelationRecord currentFirstRecord;

    public IndexJoinRecordSet(JoinedRelation relation, RecordSet first, Index index) {
        super(relation);
        this.relation = relation;
        this.firstRecordSet = first;
        this.index = index;

        this.firstColumnIndex = relation.getFirstJoinColumnIndex();
        this.secondColumnIndex = relation.getSecondJoinColumnIndex();
    }

    @Override
    protected RelationRecord moveUntilNext() {
        while (true) {
            if (currentFirstRecord == null) {
                if (!firstRecordSet.hasNext()) {
                    return null;
                }

                currentFirstRecord = firstRecordSet.next();
                secondRecordSet = index.buildRecordSetForJoin(currentFirstRecord.getInteger(firstColumnIndex));
                secondRecordSet.moveFirst();
            }

            if (secondRecordSet.hasNext()) {
                RelationRecord currentSecondRecord = secondRecordSet.next();
                return new JoinedRelationRecord(relation, currentFirstRecord, currentSecondRecord);
            }

            currentFirstRecord = null;
        }
    }

    @Override
    public void moveFirst() {
        firstRecordSet.moveFirst();
        secondRecordSet = null;
    }

    @Override
    public void remove() {

    }
}
