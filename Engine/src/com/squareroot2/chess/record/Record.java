package com.squareroot2.chess.record;

import com.squareroot2.chess.attributes.Color;
import com.squareroot2.chess.attributes.Location;
import com.squareroot2.chess.attributes.Name;
import com.squareroot2.chess.parts.Face;
import com.squareroot2.chess.parts.Piece;

public class Record {
    private final Face face;
    private final Location from, to;

    public Record(Face face, Location from, Location to) {
        this.face = face;
        this.from = from;
        this.to = to;
    }

    //生成对应四字棋谱
    @Override
    public String toString() {
        int num = getNum();
        if (num == 0) throw new RuntimeException("该位置没有棋子");
        Piece p = face.get(from);
        Color c = p.color();
        String record = null;
        switch (p.name()) {
            case Guard, Minister, Horse -> {
                if (num == 1)
                    record = getName() + getCol(from, c) + getMove() + getCol(to, c);
                else if (num > 1)
                    record = getOrd(num) + getName() + getMove() + getCol(to, c);
            }
            case King, Rook, Cannon, Pawn -> {
                if (num == 1) {
                    if (from.y() == to.y())
                        record = getName() + getCol(from, c) + getMove() + getCol(to, c);
                    else record = getName() + getCol(from, c) + getMove() + getDistance();
                } else if (num > 1) {
                    if (isSpecial()) {
                        if (from.y() == to.y())
                            record = getOrd(num) + getCol(from, c) + getMove() + getCol(to, c);
                        else record = getOrd(num) + getCol(from, c) + getMove() + getDistance();
                    } else {
                        if (from.y() == to.y())
                            record = getOrd(num) + getName() + getMove() + getCol(to, c);
                        else record = getOrd(num) + getName() + getMove() + getDistance();
                    }
                }
            }
        }
        return record;
    }

    //生成对应字符串
    private String getName() {
        Piece p = face.get(from);
        if (p == null)
            return null;
        String name = null;
        switch (p.name()) {
            case King -> name = p.color() == Color.Red ? "帅" : "将";
            case Guard -> name = "士";
            case Minister -> name = p.color() == Color.Red ? "相" : "象";
            case Horse -> name = "马";
            case Rook -> name = "车";
            case Cannon -> name = "炮";
            case Pawn -> name = p.color() == Color.Red ? "兵" : "卒";
        }
        return name;
    }

    //该列的名称
    private String getCol(Location l, Color color) {
        return colChar[color.ordinal()][l.x()];
    }

    private int getNum() {
        Piece p = face.get(from);
        if (p == null)
            return 0;
        int col = from.x();
        return getNum(p, col);
    }

    //该列的与p相同的棋子数量
    private int getNum(Piece p, int col) {
        int num = 0;
        for (int y = 0; y <= Location.maxY; y++) {
            Piece temp = face.get(col, y);
            if (temp != null && temp.equals(p)) num++;
        }
        return num;
    }

    //棋子p排该列棋子的序数
    private String getOrd(int num) {
        if (num <= 1) return null;
        Piece p = face.get(from);
        if (p == null)
            return null;
        int ord = 0;
        if (p.color() == Color.Red) {
            for (int y = 0; y < from.y(); y++) {
                if (face.get(from.x(), y).equals(p)) ord++;
            }
        } else {
            for (int y = from.y() - 1; y >= 0; y--) {
                if (face.get(from.x(), y).equals(p)) ord++;
            }
        }
        return ordChar[num][ord];
    }

    //棋子的移动方式
    private String getMove() {
        Piece p = face.get(from);
        if (p == null)
            return null;
        String move;
        if (from.y() == to.y()) move = "平";
        else if (from.y() < to.y()) {
            if (p.color() == Color.Red) move = "退";
            else move = "进";
        } else {
            if (p.color() == Color.Red) move = "进";
            else move = "退";
        }
        return move;
    }

    //棋子前后移动的距离
    private String getDistance() {
        Piece p = face.get(from);
        if (p == null)
            return null;
        int dis = Math.abs(to.y() - from.y());
        return distanceChar[p.color().ordinal()][dis];

    }

    //是否为特殊情况（两列均有超过两个相同棋子）
    private boolean isSpecial() {
        Piece p = face.get(from);
        if (p.name() == Name.Pawn) {
            if (getNum() < 2) return false;
            int tag = 0;
            for (int x = 0; x <= Location.maxX; x++) {
                if (getNum(p, x) >= 2) tag++;
            }
            return tag > 1;
        }
        return false;
    }

    private static final String[] ChineseCharacters = {
            "前", "一", "二", "三", "四",
            "五", "六", "七", "八", "九",
            "中", "后", "车", "马", "炮",
            "士", "相", "象", "兵", "卒",
            "帅", "将", "进", "平", "退"
    };
    private static final String[] nameChar = {
            "车", "马", "炮", "士", "相", "象", "兵", "卒", "帅", "将"
    }, moveChar = {"进", "平", "退"};
    private static final String[][] colChar = {
            {"九", "八", "七", "六", "五", "四", "三", "二", "一"},      //红
            {"1", "2", "3", "4", "5", "6", "7", "8", "9"}             //黑
    }, ordChar = {
            {null},
            {null},
            {"前", "后"},
            {"前", "中", "后"},
            {"前", "二", "三", "四"},
            {"前", "二", "三", "四", "五"}
    }, distanceChar = {
            {null, "一", "二", "三", "四", "五", "六", "七", "八", "九"},
            {null, "1", "2", "3", "4", "5", "6", "7", "8", "9"}
    };
}
