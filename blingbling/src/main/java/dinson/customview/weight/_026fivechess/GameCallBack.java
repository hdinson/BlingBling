package dinson.customview.weight._026fivechess;

public interface GameCallBack {


    void FaceMode(final boolean p0);

    void onGameOver(final GomokuResult result);

    void aiRun(final int p0);

    void aiTEFACES(final int p0, final int p1, final int p2, final int p3, final int p4);

}
