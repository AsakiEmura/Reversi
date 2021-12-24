package com.bjut.reversi.reference;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class ChessBoardRationality {
    private int n;

    private int color;

    private int [][]chessboard;

    private String filePath;

    private boolean AI;

    private List<Integer> steps;

    public int getN() {
        return n;
    }

    public int getColor() {
        return color;
    }

    public int[][] getChessboard() {
        return chessboard;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isAI() {
        return AI;
    }

    public List<Integer> getSteps() {
        return steps;
    }

    static final int[][] DIRECTION = {
            {1, 1}, {1, 0}, {1, -1}, {0, 1},
            {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}
    };

    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
      1.棋盘错误
        棋盘非8*8
        code：101
      2.棋子错误
        棋子类型非3种。
        code：102
      3.缺少行棋方
        code：103
      4.文件格式错误
        code：104
      5.非法落子
        code：105
      6.其他错误
        code：106
     */

    public void checkRationality(JFrame main, String filePath) {
        String[] strArray = filePath.split("\\.");
        int suffixIndex = strArray.length - 1;

        String s = readJsonFile(filePath);
        JSONObject jobj = JSON.parseObject(s);
        String n = jobj.getString("n");
        this.n = parseInt(n);
        String color = jobj.getString("color");
        JSONArray arr = jobj.getJSONArray("chessboard");
        JSONArray steps = jobj.getJSONArray("steps");

        List<Integer> finalSteps = new ArrayList<Integer>();
        for (Object step : steps) {
            finalSteps.add(parseInt(step.toString()));
        }
        this.steps = finalSteps;

        List<Integer> chessboard = new ArrayList<Integer>();
        for (Object chess : arr) {
            chessboard.add(parseInt(chess.toString()));
        }

        for (int i = 0; i < chessboard.size(); i++) {
            if (!(chessboard.get(i) == 1 || chessboard.get(i) == -1 || chessboard.get(i) == 0)) {
                JOptionPane.showMessageDialog(main, "Wrong Chess in ChessBoard", "Code:102", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if (!(color.equals("-1") || color.equals("1"))) {
            JOptionPane.showMessageDialog(main, "Missing players", "Code:103", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!strArray[suffixIndex].equals("json")) {
            JOptionPane.showMessageDialog(main, "Wrong file format", "Code:104", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(checkStepsRationality(parseInt(n),parseInt(color),chessboard,finalSteps)){
            JOptionPane.showMessageDialog(main, "Illegal move", "Code:105", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String [] options = {"Human VS Computer","Human VS Human"};
        String info = (String)JOptionPane.showInputDialog(main,"Choose Game Mode:","Message",JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if(info == "Human VS Computer"){
            this.AI = true;
        }
        else{
            this.AI = false;
        }
    }

    public boolean checkStepsRationality(int n, int color, List<Integer> chessboard, List<Integer> steps) {
        int[][] finalChessboard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < chessboard.size(); j++) {
                finalChessboard[i][j % 8] = chessboard.get((i * 8) + (j % 8));
            }
        }

        boolean valid = true;
        for (int i = 0; i < n; i++) {
            int moveX = steps.get(i*2);
            int moveY = steps.get((i*2)+1);
            System.out.println(moveX);
            System.out.println(moveY);
            if (valid && isValidMove(finalChessboard, moveX, moveY, color)) {
                finalChessboard[moveX][moveY] = color;
                for (int[] dir : DIRECTION) {
                    int x = moveX + dir[0];
                    int y = moveY + dir[1];
                    boolean found = false;
                    boolean hasOpposite = false;
                    int endx = -1, endy = -1;
                    while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                        if (finalChessboard[x][y] == color) {
                            if (hasOpposite) {
                                found = true;
                                endx = x;
                                endy = y;
                            }
                            break;
                        } else if (finalChessboard[x][y] == 0) {
                            break;
                        } else if (finalChessboard[x][y] == -color) {
                            hasOpposite = true;
                        }
                        x += dir[0];
                        y += dir[1];
                    }
                    if (found) {
                        x = moveX + dir[0];
                        y = moveY + dir[1];
                        do {
                            finalChessboard[x][y] = color;
                            x += dir[0];
                            y += dir[1];
                        } while (x != endx || y != endy);
                    }
                }
                color = -color;
            } else {
                valid = false;
            }
        }

        if(!valid) {
            this.color = color;
        }
        this.chessboard = finalChessboard;
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                System.out.printf("%3d", finalChessboard[j][k]);
            }
            System.out.println();
        }

        return valid;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int color = in.nextInt();
        int[][] chessboard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessboard[i][j] = in.nextInt();
            }
        }

        boolean valid = true;
        for (int i = 0; i < n; i++) {
            int moveX = in.nextInt();
            int moveY = in.nextInt();
            if (valid && isValidMove(chessboard, moveX, moveY, color)) {
                chessboard[moveX][moveY] = color;
                for (int[] dir : DIRECTION) {
                    int x = moveX + dir[0];
                    int y = moveY + dir[1];
                    boolean found = false;
                    boolean hasOpposite = false;
                    int endx = -1, endy = -1;
                    while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                        if (chessboard[x][y] == color) {
                            if (hasOpposite) {
                                found = true;
                                endx = x;
                                endy = y;
                            }
                            break;
                        } else if (chessboard[x][y] == 0) {
                            break;
                        } else if (chessboard[x][y] == -color) {
                            hasOpposite = true;
                        }
                        x += dir[0];
                        y += dir[1];
                    }
                    if (found) {
                        x = moveX + dir[0];
                        y = moveY + dir[1];
                        do {
                            chessboard[x][y] = color;
                            x += dir[0];
                            y += dir[1];
                        } while (x != endx || y != endy);
                    }
                }
                color = -color;
            } else {
                valid = false;
            }
        }

        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                System.out.printf("%3d", chessboard[j][k]);
            }
            System.out.println();
        }
        in.close();
    }

    public static boolean isValidMove(int[][] chessboard, int moveX, int moveY, int color) {
        if (chessboard[moveX][moveY] == 0) {
            for (int[] dir : DIRECTION) {
                int x = moveX + dir[0];
                int y = moveY + dir[1];
                boolean hasOpposite = false;
                while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                    if (chessboard[x][y] == color) {
                        if (hasOpposite) {
                            return true;
                        }
                        break;
                    } else if (chessboard[x][y] == 0) {
                        break;
                    } else if (chessboard[x][y] == -color) {
                        hasOpposite = true;
                    }
                    x += dir[0];
                    y += dir[1];
                }
            }
        }
        return false;
    }
}
