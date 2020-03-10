package com.bf92.ai;

import androidx.annotation.Keep;

import dinson.customview.weight._026fivechess.AICallBack;

public class AI implements Runnable {

    private static int DO_INITION = 1;  //初始化
    public static int DO_NEWGAME = 2;   //重新开始游戏
    public static int DO_DOSTONE = 3;   //落子
    public static int DO_UNDO = 4;      //悔棋
    public static int DO_STOP = 5;      //停止查找深度
    private static int DO_CGO = 6;      //ai算步
    public static int DO_STEPTIME = 7;  //设置单步思考时间

    private AICallBack callBack;
    private int[] chessList;

    static {
        System.loadLibrary("mmai");
    }

    public AI(final int[] chessList, final AICallBack callBack) {
        this.callBack = callBack;
        this.chessList = chessList;
        this.TEFACES(AI.DO_INITION, 0, 0, 0, chessList);
        this.TEFACES(AI.DO_NEWGAME, 0, 0, 0, chessList);
    }

    private native int TEFACES(final int code, final int who, final int point, final int step, final int[] chessList);

    /**
     * ai线程开始执行
     */
    public void aiBout() {
        new Thread(this).start();
    }

    public void aiTEFACES(final int code, final int who, final int x, final int y, final int step) {
        this.TEFACES(code, who, x * 15 + y, step, chessList);
    }

    @Override
    public void run() {
        final int tefaces = this.TEFACES(AI.DO_CGO, 1, 0, 0, chessList);
        this.callBack.aiCompleted(tefaces / 15, tefaces % 15);
    }

    @Keep
    public int cpp_put_bestpos(final int n) {
        this.callBack.aiThinkPoint(n);
        return 1;
    }

    @Keep
    public void cpp_put_str(final byte[] array, final int n) {
    }
}
