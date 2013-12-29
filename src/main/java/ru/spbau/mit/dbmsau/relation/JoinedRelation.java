package ru.spbau.mit.dbmsau.relation;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;

public class JoinedRelation extends Relation {
    private Relation first;
    private Relation second;
    private int firstJoinColumnIndex;
    private int secondJoinColumnIndex;

    private static ArrayList<Column> buildColumns(Relation first, Relation second) {
        ArrayList<Column> result = new ArrayList<>();
        return Lists.newArrayList(Iterables.concat(first.getColumns(), second.getColumns()));
    }

    public JoinedRelation(Relation first, Relation second, int firstJoinColumnIndex, int secondJoinColumnIndex) {
        super(buildColumns(first, second));
        this.first = first;
        this.second = second;
        this.firstJoinColumnIndex = firstJoinColumnIndex;
        this.secondJoinColumnIndex = secondJoinColumnIndex;
    }

    public Relation getFirst() {
        return first;
    }

    public Relation getSecond() {
        return second;
    }

    public int getFirstJoinColumnIndex() {
        return firstJoinColumnIndex;
    }

    public int getSecondJoinColumnIndex() {
        return secondJoinColumnIndex;
    }
}
