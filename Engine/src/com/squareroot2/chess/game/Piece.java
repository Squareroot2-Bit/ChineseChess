package com.squareroot2.chess.game;

import com.squareroot2.chess.attributes.Color;
import com.squareroot2.chess.attributes.Name;

public class Piece {
    //节省空间
    //使得任意2个棋子之间可以用“==”比较
    private static final Piece[][] map = new Piece[2][7];

    static {
        for (Color color : Color.values()) {
            for (Name name : Name.values()) {
                map[color.ordinal()][name.ordinal()]
                        = new Piece(color, name);
            }
        }
    }

    public static Piece get(Color color, Name name) {
        return map[color.ordinal()][name.ordinal()];
    }

    private final Color color;
    private final Name name;

    private Piece(Color color, Name name) {
        this.color = color;
        this.name = name;
    }


    //Getter
    public Color color() {
        return color;
    }

    public Name name() {
        return name;
    }
    //disabled
    public boolean equals(Piece piece) {
        return piece.color == color && piece.name == name;
    }
}
