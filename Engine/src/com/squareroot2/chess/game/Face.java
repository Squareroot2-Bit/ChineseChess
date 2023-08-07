package com.squareroot2.chess.game;

import com.squareroot2.chess.attributes.Color;
import com.squareroot2.chess.attributes.Location;
import com.squareroot2.chess.attributes.Name;

import java.util.ArrayList;

public class Face {
    //棋局初始局面
    private static final Piece[][] InitFace;

    static {
        InitFace = new Piece[Location.maxX + 1][Location.maxY + 1];
        InitFace[0][0] = Piece.get(Color.Black, Name.Rook);
        InitFace[1][0] = Piece.get(Color.Black, Name.Horse);
        InitFace[2][0] = Piece.get(Color.Black, Name.Minister);
        InitFace[3][0] = Piece.get(Color.Black, Name.Guard);
        InitFace[4][0] = Piece.get(Color.Black, Name.King);
        InitFace[5][0] = Piece.get(Color.Black, Name.Guard);
        InitFace[6][0] = Piece.get(Color.Black, Name.Minister);
        InitFace[7][0] = Piece.get(Color.Black, Name.Horse);
        InitFace[8][0] = Piece.get(Color.Black, Name.Rook);
        InitFace[1][2] = Piece.get(Color.Black, Name.Cannon);
        InitFace[7][2] = Piece.get(Color.Black, Name.Cannon);
        InitFace[0][3] = Piece.get(Color.Black, Name.Pawn);
        InitFace[2][3] = Piece.get(Color.Black, Name.Pawn);
        InitFace[4][3] = Piece.get(Color.Black, Name.Pawn);
        InitFace[6][3] = Piece.get(Color.Black, Name.Pawn);
        InitFace[8][3] = Piece.get(Color.Black, Name.Pawn);
        InitFace[0][9] = Piece.get(Color.Red, Name.Rook);
        InitFace[1][9] = Piece.get(Color.Red, Name.Horse);
        InitFace[2][9] = Piece.get(Color.Red, Name.Minister);
        InitFace[3][9] = Piece.get(Color.Red, Name.Guard);
        InitFace[4][9] = Piece.get(Color.Red, Name.King);
        InitFace[5][9] = Piece.get(Color.Red, Name.Guard);
        InitFace[6][9] = Piece.get(Color.Red, Name.Minister);
        InitFace[7][9] = Piece.get(Color.Red, Name.Horse);
        InitFace[8][9] = Piece.get(Color.Red, Name.Rook);
        InitFace[1][7] = Piece.get(Color.Red, Name.Cannon);
        InitFace[7][7] = Piece.get(Color.Red, Name.Cannon);
        InitFace[0][6] = Piece.get(Color.Red, Name.Pawn);
        InitFace[2][6] = Piece.get(Color.Red, Name.Pawn);
        InitFace[4][6] = Piece.get(Color.Red, Name.Pawn);
        InitFace[6][6] = Piece.get(Color.Red, Name.Pawn);
        InitFace[8][6] = Piece.get(Color.Red, Name.Pawn);
    }

    private final Piece[][] face;

    public Face(Piece[][] face) {
        this.face = face;
    }

    public Face() {
        this(InitFace);
    }

    public Face(Face oldFace) {
        this(new Piece[Location.maxX + 1][Location.maxY + 1]);
        for (int x = 0; x <= Location.maxX; x++) {
            for (int y = 0; y <= Location.maxY; y++) {
                face[x][y] = oldFace.face[x][y];
            }
        }
    }

    //获取某位置上的棋子可移动的位置(不吃子)
    public ArrayList<Location> getMovableRange(Location location) {
        Piece piece = get(location);
        ArrayList<Location> locations = new ArrayList<>();
        Location temp;
        if (piece == null) throw new RuntimeException("位置" + location + "上没有棋子");
        else {
            switch (piece.name()) {
                case King, Guard -> {
                    int[][] MovableRange = null;
                    if (piece.name() == Name.King) MovableRange = MovableRangeOfKing;
                    else if (piece.name() == Name.Guard) MovableRange = MovableRangeOfGuard;
                    if (MovableRange != null) for (int[] step : MovableRange) {
                        temp = location.add(step);
                        if (temp.isInPalace(piece.color()) && get(temp) == null)
                            locations.add(temp);
                    }
                }
                case Minister, Horse -> {
                    int[][][] Mapping = null;
                    Location key;
                    if (piece.name() == Name.Minister) Mapping = MinisterEyeMapping;
                    else if (piece.name() == Name.Horse) Mapping = HorseLegMapping;
                    if (Mapping != null) for (int[][] map : Mapping) {
                        key = location.add(map[0]);
                        if (key.isInBoard() && get(key) == null) {
                            temp = location.add(map[1]);
                            if (temp.isInArea(piece.color()) && get(temp) == null)
                                locations.add(temp);
                        }
                    }
                }
                case Rook, Cannon -> {
                    boolean[] dir = new boolean[]{true, true, true, true};
                    int[][] coef = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
                    boolean tag;
                    for (int i = 1; i <= 9; i++) {
                        tag = false;
                        for (int j = 0; j < 4; j++) {
                            if (dir[j]) {
                                temp = location.add(coef[j][0] * i, coef[j][1] * i);
                                if (temp.isInBoard() && get(temp) == null) locations.add(temp);
                                else dir[j] = false;
                                tag = tag || dir[j];
                            }
                        }
                        if (!tag) break;
                    }
                }
                case Pawn -> {
                    int[][] MovableRange = null;
                    Color color = piece.color();
                    if (color == Color.Red && location.isInArea(color))
                        MovableRange = MovableRangeOfRedNewPawn;
                    else if (color == Color.Red && !location.isInArea(color))
                        MovableRange = MovableRangeOfRedOldPawn;
                    else if (color == Color.Black && location.isInArea(color))
                        MovableRange = MovableRangeOfBlackNewPawn;
                    else if (color == Color.Black && !location.isInArea(color))
                        MovableRange = MovableRangeOfBlackOldPawn;
                    if (MovableRange != null) for (int[] step : MovableRange) {
                        temp = location.add(step);
                        if (temp.isInBoard() && get(temp) == null) locations.add(temp);
                    }
                }
            }
            return locations;
        }
    }

    //获取某位置上的棋子可吃子的位置
    public ArrayList<Location> getCapturedRange(Location location) {
        Piece piece = get(location);
        ArrayList<Location> locations = new ArrayList<>();
        Location temp = null;
        if (piece == null) throw new RuntimeException("该位置上没有棋子");
        else {
            switch (piece.name()) {
                case King, Guard -> {
                    int[][] MovableRange = null;
                    if (piece.name() == Name.King) MovableRange = MovableRangeOfKing;
                    else if (piece.name() == Name.Guard) MovableRange = MovableRangeOfGuard;
                    if (MovableRange != null) for (int[] step : MovableRange) {
                        temp = location.add(step);
                        if (temp.isInPalace(piece.color()) && get(temp) != null && get(temp).color() != piece.color())
                            locations.add(temp);
                    }
                    //白脸将
                    if (piece.name() == Name.King) for (int i = 1; i <= 9; i++) {
                        switch ((piece.color())) {
                            case Red -> temp = location.add(0, -i);
                            case Black -> temp = location.add(0, i);
                        }
                        if (!temp.isInBoard()) break;
                        else if (get(temp) != null) {
                            if (get(temp).color() != piece.color() && get(temp).name() == Name.King)
                                locations.add(temp);
                            break;
                        }
                    }
                }
                case Minister, Horse -> {
                    int[][][] Mapping = null;
                    Location key;
                    if (piece.name() == Name.Minister) Mapping = MinisterEyeMapping;
                    else if (piece.name() == Name.Horse) Mapping = HorseLegMapping;
                    for (int[][] map : Mapping) {
                        key = location.add(map[0]);
                        if (key.isInBoard() && get(key) == null) {
                            temp = location.add(map[1]);
                            if (temp.isInArea(piece.color()) && get(temp) != null && get(temp).color() != piece.color())
                                locations.add(temp);
                        }
                    }
                }
                case Rook, Cannon -> {
                    int cross = 0;
                    if (piece.name() == Name.Rook) cross = 0;
                    else if (piece.name() == Name.Cannon) cross = 1;
                    int[] dir = new int[]{0, 0, 0, 0};
                    int[][] coef = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
                    boolean tag;
                    for (int i = 1; i <= 9; i++) {
                        tag = false;
                        for (int j = 0; j < 4; j++) {
                            if (dir[j] <= cross) {
                                temp = location.add(coef[j][0] * i, coef[j][1] * i);
                                if (!temp.isInBoard()) dir[j] = inf;
                                else if (get(temp) != null) {
                                    dir[j]++;
                                    if (dir[j] > cross && get(temp).color() != piece.color())
                                        locations.add(temp);
                                }
                                tag = tag || dir[j] <= cross;
                            }
                        }
                        if (!tag) break;
                    }
                }
                case Pawn -> {
                    int[][] MovableRange = null;
                    Color color = piece.color();
                    if (color == Color.Red && location.isInArea(color))
                        MovableRange = MovableRangeOfRedNewPawn;
                    else if (color == Color.Red && !location.isInArea(color))
                        MovableRange = MovableRangeOfRedOldPawn;
                    else if (color == Color.Black && location.isInArea(color))
                        MovableRange = MovableRangeOfBlackNewPawn;
                    else if (color == Color.Black && !location.isInArea(color))
                        MovableRange = MovableRangeOfBlackOldPawn;
                    if (MovableRange != null) for (int[] step : MovableRange) {
                        temp = location.add(step);
                        if (temp.isInBoard() && get(temp) != null && get(temp).color() != piece.color())
                            locations.add(temp);
                    }
                }
            }
            return locations;
        }
    }

    //生成将from处的棋子移至to处的局面
    //如不合法，返回null
    public Face move(Location from, Location to) {
        var movableRange = getMovableRange(from);
        var capturedRange = getCapturedRange(from);
        Face newFace;
        if (movableRange.contains(to) || capturedRange.contains(to)) {
            newFace = new Face(this);
            newFace.set(to, newFace.get(from));
            newFace.set(from, null);
        } else newFace = null;
        return newFace;
    }


    //检测所指颜色是否被将军
    public boolean isChecked(Color color) {
        for (int x = 0; x <= Location.maxX; x++) {
            for (int y = 0; y <= Location.maxY; y++) {
                Piece piece = get(x, y);
                if (piece != null && piece.color() != color) {
                    for (Location l : getCapturedRange(new Location(x, y))) {
                        Piece p = get(l);
                        if (p != null && p.name() == Name.King && p.color() == color) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //检测所指颜色是否被困毙
    public boolean isStalemated(Color color) {
        Face newFace;
        for (int x = 0; x <= Location.maxX; x++) {
            for (int y = 0; y <= Location.maxY; y++) {
                Piece piece = get(x, y);
                if (piece != null && piece.color() == color) {
                    Location from = new Location(x, y);
                    for (Location to : getMovableRange(from)) {
                        newFace = move(from, to);
                        if (!newFace.isChecked(color)) return false;
                    }
                    for (Location to : getCapturedRange(from)) {
                        newFace = move(from, to);
                        if (!newFace.isChecked(color)) return false;
                    }
                }
            }
        }
        return true;
    }

    //检测所指颜色是否被将死
    public boolean isCheckmated(Color color) {
        return isChecked(color) && isStalemated(color);
    }

    //获取对应字符串二维数组
    public String[][] toStrings() {
        String[][] strings = new String[Location.maxX + 1][Location.maxY + 1];
        String s;
        for (int y = 0; y <= Location.maxY; y++) {
            for (int x = 0; x <= Location.maxX; x++) {
                if (face[x][y] == null) strings[x][y] = " ";
                else {
                    s = String.valueOf(face[x][y].name().toString().charAt(0));
                    if (face[x][y].color() == Color.Red) s = s.toUpperCase();
                    else s = s.toLowerCase();
                    strings[x][y] = s;
                }
            }
        }
        return strings;
    }

    //打印局面
    public void print() {
        String[][] strings = toStrings();
        for (int y = 0; y <= Location.maxY; y++) {
            for (int x = 0; x <= Location.maxX; x++) {
                System.out.print(strings[x][y]);
            }
            System.out.println();
        }
    }

    public void print(Location location) {
        String[][] strings = toStrings();
        if (get(location) != null) {
            var locations = getMovableRange(location);
            for (Location l : locations) {
                strings[l.x()][l.y()] = "O";
            }
            locations = getCapturedRange(location);
            for (Location l : locations) {
                strings[l.x()][l.y()] = "X";
            }
        }
        for (int y = 0; y <= Location.maxY; y++) {
            for (int x = 0; x <= Location.maxX; x++) {
                System.out.print(strings[x][y]);
            }
            System.out.println();
        }
    }

    //getter
    public Piece get(int x, int y) {
        return face[x][y];
    }

    public Piece get(Location location) {
        return face[location.x()][location.y()];
    }

    //setter
    private void set(int x, int y, Piece piece) {
        face[x][y] = piece;
    }

    private void set(Location location, Piece piece) {
        set(location.x(), location.y(), piece);
    }

    //以下为各棋子的移动范围（映射）
    private static final int[][] MovableRangeOfGuard = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1},};
    private static final int[][] MovableRangeOfKing = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1},};
    private static final int[][][] MinisterEyeMapping = new int[][][]{{{1, 1}, {2, 2}}, {{1, -1}, {2, -2}}, {{-1, 1}, {-2, 2}}, {{-1, -1}, {-2, -2}},};
    private static final int[][][] HorseLegMapping = new int[][][]{{{0, 1}, {1, 2}}, {{0, 1}, {-1, 2}}, {{0, -1}, {1, -2}}, {{0, -1}, {-1, -2}}, {{1, 0}, {2, 1}}, {{1, 0}, {2, -1}}, {{-1, 0}, {-2, 1}}, {{-1, 0}, {-2, -1}},};
    private static final int[][] MovableRangeOfRedNewPawn = new int[][]{{0, -1},};
    private static final int[][] MovableRangeOfBlackNewPawn = new int[][]{{0, 1},};
    private static final int[][] MovableRangeOfRedOldPawn = new int[][]{{0, -1}, {1, 0}, {-1, 0},};
    private static final int[][] MovableRangeOfBlackOldPawn = new int[][]{{0, 1}, {1, 0}, {-1, 0},};

    public static final int inf = 999;
}
