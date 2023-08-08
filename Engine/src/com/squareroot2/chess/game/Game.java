package com.squareroot2.chess.game;

import com.squareroot2.chess.attributes.Result;
import com.squareroot2.chess.parts.Board;

import java.util.Scanner;

public class Game {
    private static Board board = new Board();
    private static boolean gameover = true;

    public static boolean commander(Scanner scanner) {
        String buf;
        Scanner parseLine;
        System.out.print(">>>");
        buf = scanner.nextLine();
        parseLine = new Scanner(buf).useDelimiter(" ");
        if (parseLine.hasNext()) {
            String command = parseLine.next();
            switch (command) {
                case "start" -> {
                    board = new Board();
                    gameover = false;
                    board.print();
                }
                case "click" -> {
                    if (gameover) gameover();
                    else click(parseLine);
                }
                case "print" -> board.print();
                case "record" -> board.printRecords();
                case "quit" -> {
                    return false;
                }
                default -> invalidCommand();
            }
        }
        return true;
    }

    private static void click(Scanner scanner) {
        if (board.result() != Result.InService) {
            System.out.println("   棋局已结束!");
        } else {
            int x, y;
            if (scanner.hasNextInt()) x = scanner.nextInt();
            else {
                invalidCommand();
                return;
            }
            if (scanner.hasNextInt()) y = scanner.nextInt();
            else {
                invalidCommand();
                return;
            }
            boolean tag = board.select(x, y);
            if (!tag) System.out.println("   禁止送将!");
            board.print();
            switch (board.result()) {
                case RedWin -> {
                    System.out.println("棋局结束,红方胜!");
                }
                case BlackWin -> {
                    System.out.println("棋局结束,黑方胜!");
                }
                case Draw -> {
                    System.out.println("棋局结束,平局!");
                }
            }
        }
    }

    private static void invalidCommand() {
        System.out.println("   非法输入!");
    }

    private static void gameover() {
        System.out.println("   棋局已结束!");
    }
}

/**
 * 功能
 * 开始新局 start
 * 点击某位置的点 click x y
 * 打印局面 print
 * 打印棋谱 record
 * 退出 quit
 * 设置局面 setting
 */
