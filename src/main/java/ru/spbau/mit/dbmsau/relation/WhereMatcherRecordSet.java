package ru.spbau.mit.dbmsau.relation;

public class WhereMatcherRecordSet extends RecordSet {
    private RecordSet content;
    private WhereMatcher matcher;
    private RelationRecord nextResult;

    public WhereMatcherRecordSet(RecordSet content, WhereMatcher matcher) {
        super(content.getRelation());
        this.content = content;
        this.matcher = matcher;
    }

    @Override
    public void moveFirst() {
        content.moveFirst();
    }

    private void walkUntilNext() {
        while (nextResult == null && content.hasNext()) {
            RelationRecord candidate = content.next();
            if (matcher.matches(candidate)) {
                nextResult = candidate;
            }
        }
    }

    @Override
    public boolean hasNext() {
        walkUntilNext();
        return nextResult != null;
    }

    @Override
    public RelationRecord next() {
        walkUntilNext();

        RelationRecord currentResult = nextResult;
        nextResult = null;

        return currentResult;
    }

    @Override
    public void remove() {
        content.remove();
    }
}
