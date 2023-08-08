package com.squareroot2.chess.record;

import java.util.ArrayList;

public class RecordList {
    private final ArrayList<Record> records;

    public RecordList(ArrayList<Record> records) {
        this.records = records;
    }

    public RecordList() {
        this(new ArrayList<Record>());
    }

    public Record get(int i) {
        return records.get(i);
    }

    public boolean add(Record record) {
        return records.add(record);
    }

    @Override
    public String toString() {
        if (records.isEmpty())
            return "没有棋谱\n";
        StringBuilder s = new StringBuilder();
        boolean tag = false;
        for (Record r : records) {
            s.append(r).append(tag ? "\n" : "  ");
            tag = !tag;
        }
        return s.toString();
    }
}
