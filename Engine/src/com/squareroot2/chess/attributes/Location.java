package com.squareroot2.chess.attributes;

public record Location(int x, int y) {
    public static final int maxX = 8;
    public static final int maxY = 9;

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public Location add(int x, int y) {
        return new Location(this.x + x, this.y + y);
    }

    public Location add(int[] ints) {
        if (ints.length == 2)
            return add(ints[0], ints[1]);
        else throw new RuntimeException("该数组不能作为Location.add()的参数");
    }

    //判断该位置是否在棋盘内
    public boolean isInBoard() {
        return x >= 0 && x <= maxX && y >= 0 && y <= maxY;
    }

    //判断该位置是否在某方区域内
    public boolean isInArea(Color color) {
        if (isInBoard())
            switch (color) {
                case Red -> {
                    return y >= 5;
                }
                case Black -> {
                    return y <= 4;
                }
            }
        return false;
    }

    //判断该位置是否在某方九宫内
    public boolean isInPalace(Color color) {
        if (isInBoard() && x >= 3 && x <= 5)
            switch (color) {
                case Red -> {
                    return y >= 7;
                }
                case Black -> {
                    return y <= 2;
                }
            }
        return false;
    }

    //不常用
    //判断该位置是否是某方士能走到的位置
    public boolean inGuardLocations(Color color) {
        if (isInBoard())
            switch (color) {
                case Red -> {
                    return equals(new Location(3, 9)) ||
                            equals(new Location(5, 9)) ||
                            equals(new Location(4, 8)) ||
                            equals(new Location(3, 7)) ||
                            equals(new Location(5, 7));
                }
                case Black -> {
                    return equals(new Location(3, 0)) ||
                            equals(new Location(5, 0)) ||
                            equals(new Location(4, 1)) ||
                            equals(new Location(3, 2)) ||
                            equals(new Location(5, 2));
                }
            }
        return false;
    }

    //判断该位置是否是某方相/象能走到的位置
    public boolean inMinisterLocations(Color color) {
        if (isInBoard())
            switch (color) {
                case Red -> {
                    return equals(new Location(2, 9)) ||
                            equals(new Location(6, 9)) ||
                            equals(new Location(0, 7)) ||
                            equals(new Location(4, 7)) ||
                            equals(new Location(8, 7)) ||
                            equals(new Location(2, 5)) ||
                            equals(new Location(6, 5));
                }
                case Black -> {
                    return equals(new Location(2, 0)) ||
                            equals(new Location(6, 0)) ||
                            equals(new Location(0, 2)) ||
                            equals(new Location(4, 2)) ||
                            equals(new Location(8, 2)) ||
                            equals(new Location(2, 4)) ||
                            equals(new Location(6, 4));
                }
            }
        return false;
    }
}
