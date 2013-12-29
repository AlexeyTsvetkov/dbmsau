package ru.spbau.mit.dbmsau.relation;

public class NestedLoopJoinRecordSet extends BaseRecordSet {
    private JoinedRelation relation;
    private RecordSet firstRecordSet;
    private RecordSet secondRecordSet;
    int firstColumnIndex;
    int secondColumnIndex;

    private RelationRecord currentFirstRecord = null;

    public NestedLoopJoinRecordSet(JoinedRelation relation, RecordSet firstRecordSet, RecordSet secondRecordSet) {
        super(relation);
        this.relation = relation;
        this.firstRecordSet = firstRecordSet;
        this.secondRecordSet = secondRecordSet;
        this.firstColumnIndex = relation.getFirstJoinColumnIndex();
        this.secondColumnIndex = relation.getSecondJoinColumnIndex();
    }

    protected RelationRecord moveUntilNext() {
        while (true) {
            if (currentFirstRecord == null) {
                if (!firstRecordSet.hasNext()) {
                    return null;
                }
                currentFirstRecord = firstRecordSet.next();
                secondRecordSet.moveFirst();
            }

            while (secondRecordSet.hasNext()) {
                RelationRecord currentSecondRecord = secondRecordSet.next();
                if (currentFirstRecord.getInteger(firstColumnIndex) == currentSecondRecord.getInteger(secondColumnIndex)) {
                    return new JoinedRelationRecord(relation, currentFirstRecord, currentSecondRecord);
                }
            }

            currentFirstRecord = null;
        }
    }

    @Override
    public void moveFirst() {
        firstRecordSet.moveFirst();
        currentFirstRecord = null;
    }

    @Override
    public void remove() {

    }
}
