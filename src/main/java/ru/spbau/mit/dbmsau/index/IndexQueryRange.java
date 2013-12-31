package ru.spbau.mit.dbmsau.index;

public class IndexQueryRange {
    private int from, to;

    public IndexQueryRange(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public String toString() {
        return String.format("[%d,%d]", from, to);
    }
}
