package com.bf92.ai;

import android.content.res.*;

import dinson.customview.weight._026fivechess.AICallBack;
import dinson.customview.weight._026fivechess.FiveChessView2;

public class AI implements Runnable {
    public static int DO_CGO = 6;
    public static int DO_DOSTONE = 3;
    public static int DO_INITION = 1;
    public static int DO_NEWGAME = 2;
    public static int DO_STEPTIME = 7;
    public static int DO_STOP = 5;
    public static int DO_UNDO = 4;
    private int aiTurn;
    private AICallBack callBack;
    public String ccc;
    private int[][] chessArray;
    private int panelLength;

    static {
        System.loadLibrary("mmai");
    }

    public AI(final int[][] chessArray, final int[] chessList, final AICallBack callBack) {
        this.aiTurn = 1;
        this.chessArray = chessArray;
        this.callBack = callBack;
        this.panelLength = chessArray.length;
        this.ccc = "5";
        this.TEFACES(AI.DO_INITION, 0, 0, 0, chessList );
        this.TEFACES(AI.DO_NEWGAME, 0, 0, 0, chessList );
    }

    public native int TEFACES(final int p0, final int p1, final int p2, final int p3, final int[] p4);

    public void aiBout() {
        new Thread(this).start();
    }

    public void aiTEFACES(final int n, final int n2, final int n3, final int n4, final int n5) {
        this.TEFACES(n, n2, n3 * 15 + n4, n5, FiveChessView2.chessList);
    }

    public native void book(final AssetManager p0, final String p1);

    @Override
    public void run() {
        final int tefaces = this.TEFACES(AI.DO_CGO, this.aiTurn, 0, FiveChessView2.stepNum, FiveChessView2.chessList);
        this.callBack.aiCompleted(0, tefaces / FiveChessView2.GRID_NUMBER, tefaces % FiveChessView2.GRID_NUMBER, this.aiTurn);
    }

    public void setAiChess(final int aiTurn) {
        this.aiTurn = aiTurn;
    }
    public int cpp_put_bestpos(final int n) {
        return 1;
    }

    public void cpp_put_str(final byte[] array, final int n) {
    }
}
