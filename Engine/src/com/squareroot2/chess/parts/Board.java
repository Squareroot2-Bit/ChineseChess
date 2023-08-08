package com.squareroot2.chess.parts;

import com.squareroot2.chess.attributes.Color;
import com.squareroot2.chess.attributes.Location;
import com.squareroot2.chess.attributes.Result;
import com.squareroot2.chess.record.Record;
import com.squareroot2.chess.record.RecordList;

public class Board {
    private Face face;
    private RecordList recordList;

    private Location highlighted;

    private Color movingColor;

    private Result result;

    //构造函数

    public Board(Face face, RecordList recordList, Location highlighted, Color movingColor, Result result) {
        this.face = face;
        this.recordList = recordList;
        this.highlighted = highlighted;
        this.movingColor = movingColor;
        this.result = result;
    }

    public Board(Face face, RecordList recordList, Location highlighted, Color movingColor) {
        this(face, recordList, highlighted, movingColor, Result.InService);
    }

    public Board(Face face, RecordList recordList) {
        this(face, recordList, null, Color.Red);
    }

    public Board(Face face) {
        this(face, new RecordList());
    }

    public Board() {
        this(new Face());
    }

    //将from处的棋子移至to处
    private void move(Location from, Location to) {
        Face temp = face.move(from, to);
        if (temp != null) {
            recordList.add(new Record(face, from, to));
            face = temp;
            if (movingColor == Color.Red)
                movingColor = Color.Black;
            else movingColor = Color.Red;
        } else throw new RuntimeException("该位置没有棋子");
    }

    //点击棋盘的特定位置
    //不合法（送死）返回false
    public boolean select(Location location) {
        if (result == Result.InService) {
            boolean tag = true;
            Piece selected = face.get(location);
            if (selected != null && selected.color() == movingColor)
                highlighted = location;
            else {
                Face newFace = face.move(highlighted, location);
                //判断是否可以移动
                if (newFace == null)
                    highlighted = null;
                else if (newFace.isChecked(movingColor)) {
                    //移动后不能使得本方被将军
                    highlighted = null;
                    tag = false;
                } else {
                    move(highlighted, location);
                    highlighted = null;
                    if (face().isStalemated(movingColor)) {
                        if (movingColor == Color.Red)
                            setResult(Result.BlackWin);
                        else setResult(Result.RedWin);
                    }
                }
            }
            return tag;
        } else return false;
    }

    public boolean select(int x, int y) {
        return select(new Location(x, y));
    }

    //Getter
    public Face face() {
        return face;
    }

    public RecordList recordList() {
        return recordList;
    }

    public Location highlighted() {
        return highlighted;
    }

    public Color movingColor() {
        return movingColor;
    }

    public Result result() {
        return result;
    }

    //setter
    public void setFace(Face face) {
        this.face = face;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    //print
    public void print() {
        System.out.println("moving color:" + movingColor);
        if (highlighted != null)
            face.print(highlighted);
        else face.print();
    }

    public void printRecords() {
        System.out.print(recordList.toString());
    }
}
