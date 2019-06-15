package dinson.customview.weight._026fivechess;

import android.content.*;
import android.util.*;

import java.lang.reflect.*;

import android.widget.*;
import android.graphics.*;
import android.view.*;

import dinson.customview.R;
import dinson.customview.utils.LogUtils;

public class FiveChessView2 extends View implements View.OnTouchListener {
    public static final int BLACK_CHESS = 1;
    public static final int BLACK_WIN = 102;
    public static int DO_CGO = 6;
    public static int DO_DOSTONE = 3;
    public static int DO_INITION = 1;
    public static int DO_NEWGAME = 2;
    public static int DO_STEPTIME = 7;
    public static int DO_STOP = 5;
    public static int DO_UNDO = 4;
    public static int FIVE_DEBUG = 0;
    public static int GRID_NUMBER = 15;
    public static final int NOT_OVER = 104;
    public static final int NO_CHESS = 0;
    public static final int NO_WIN = 103;
    public static final int WHITE_CHESS = 2;
    public static final int WHITE_WIN = 101;
    public static int bestset = 0;
    public static int bestx = 0;
    public static int besty = 0;
    public static Bitmap blackChess;
    public static int[][] chessArray;
    public static int[] chessList;
    public static int[] chessListFar;
    public static boolean isGameOver = false;
    public static boolean isThinking = false;
    public static int stepFarNum = 0;
    public static int stepNum = 0;
    public static int turnWho = 1;
    public static Bitmap whiteChess;
    private float DD;
    private GameCallBack callBack;
    float downX;
    float downY;
    int dx;
    int dy;
    private float len;
    private float offset;
    private Paint paint;
    private float preWidth;
    private Rect rect;
    float upX;
    float upY;
    int ux;
    int uy;

    public FiveChessView2(final Context context) {
        this(context, null);
    }

    public FiveChessView2(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }

    public FiveChessView2(final Context context, final AttributeSet set, int i) {
        super(context, set, i);
        (this.paint = new Paint()).setAntiAlias(true);
        this.paint.setColor(Color.BLACK);
        FiveChessView2.chessArray = (int[][]) Array.newInstance(Integer.TYPE, FiveChessView2.GRID_NUMBER, FiveChessView2.GRID_NUMBER);
        FiveChessView2.chessList = new int[FiveChessView2.GRID_NUMBER * FiveChessView2.GRID_NUMBER + 2];
        FiveChessView2.stepNum = 0;
        FiveChessView2.chessListFar = new int[FiveChessView2.GRID_NUMBER * FiveChessView2.GRID_NUMBER + 2];
        FiveChessView2.stepFarNum = 0;
        FiveChessView2.whiteChess = BitmapFactory.decodeResource(context.getResources(), R.drawable._026_white_chess);
        FiveChessView2.blackChess = BitmapFactory.decodeResource(context.getResources(), R.drawable._026_black_chess);
        this.rect = new Rect();
        this.setOnTouchListener(this);
        int j;
        for (i = 0; i < FiveChessView2.GRID_NUMBER; ++i) {
            for (j = 0; j < FiveChessView2.GRID_NUMBER; ++j) {
                FiveChessView2.chessArray[i][j] = 0;
            }
        }
    }

    private boolean isFiveSame(final int n, final int n2) {
        final int n3 = n + 4;
        if (n3 < FiveChessView2.GRID_NUMBER && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n + 1][n2] && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n + 2][n2] && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n + 3][n2] && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n3][n2]) {
            return true;
        }
        final int n4 = n2 + 4;
        if (n4 < FiveChessView2.GRID_NUMBER && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n][n2 + 1] && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n][n2 + 2] && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n][n2 + 3] && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n][n4]) {
            return true;
        }
        if (n4 < FiveChessView2.GRID_NUMBER && n3 < FiveChessView2.GRID_NUMBER && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n + 1][n2 + 1] && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n + 2][n2 + 2] && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n + 3][n2 + 3] && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n3][n4]) {
            return true;
        }
        final int n5 = n2 - 4;
        return n5 >= 0 && n3 < FiveChessView2.GRID_NUMBER && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n + 1][n2 - 1] && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n + 2][n2 - 2] && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n + 3][n2 - 3] && FiveChessView2.chessArray[n][n2] == FiveChessView2.chessArray[n3][n5];
    }

    public static int oppTurn(final int n) {
        int n2 = 2;
        if (n == 2) {
            n2 = 1;
        }
        return n2;
    }


    public void DoStone(final int n, final int n2, final int n3, String format) {
        if (n2 < 0 || n3 < 0 || n2 > 14 || n3 > 14) {
            if (this.callBack != null) {
                LogUtils.i(String.format("DoStone %d %d out of range", n2, n3));
            }
            return;
        }
        if (FiveChessView2.chessArray[n2][n3] != 0) {
            if (this.callBack != null) {
                LogUtils.i(String.format("DoStone %d %d already set", n2, n3));
            }
            return;
        }
        if (format != "null") {
            String s;
            if (n == 1) {
                s = "黑";
            } else {
                s = "白";
            }
            format = String.format("%s%d@%c%c:%s\r\n", s, FiveChessView2.stepNum + 1, this.di_cover_1(n2), this.di_cover_2(n3), format);
            LogUtils.i(format);
        }
        FiveChessView2.chessList[FiveChessView2.stepNum] = FiveChessView2.GRID_NUMBER * n2 + n3;
        if (FiveChessView2.chessList[FiveChessView2.stepNum] != FiveChessView2.chessListFar[FiveChessView2.stepNum]) {
            FiveChessView2.chessListFar[FiveChessView2.stepNum] = FiveChessView2.chessList[FiveChessView2.stepNum];
            FiveChessView2.stepFarNum = FiveChessView2.stepNum;
            ++FiveChessView2.stepFarNum;
        }
        ++FiveChessView2.stepNum;
        FiveChessView2.chessArray[n2][n3] = n;
        FiveChessView2.turnWho = oppTurn(n);
        this.postInvalidate();
    }

    public GomokuResult FarW() {
        final int n = FiveChessView2.chessListFar[FiveChessView2.stepNum] / FiveChessView2.GRID_NUMBER;
        final int n2 = FiveChessView2.chessListFar[FiveChessView2.stepNum] % FiveChessView2.GRID_NUMBER;
        FiveChessView2.chessArray[n][n2] = FiveChessView2.turnWho;
        FiveChessView2.turnWho = oppTurn(FiveChessView2.turnWho);
        FiveChessView2.chessList[FiveChessView2.stepNum] = FiveChessView2.chessListFar[FiveChessView2.stepNum];
        ++FiveChessView2.stepNum;
        if (this.callBack != null) {
            this.callBack.aiTEFACES(FiveChessView2.DO_DOSTONE, oppTurn(FiveChessView2.turnWho), n, n2, FiveChessView2.stepNum);
        }
        final GomokuResult checkGameOver = this.checkGameOver();
        this.Print(checkGameOver);
        this.postInvalidate();
        return checkGameOver;
    }

    public void Print(final GomokuResult n) {
        switch (n) {
            case WHITE_WIN:LogUtils.i("白棋胜利!\r\n");
                break;
            case BLACK_WIN:LogUtils.i("黑棋胜利!\r\n");
                break;
            case DREW: LogUtils.i("盘满!平局!\r\n");
                break;
            case UNKNOWN:
                break;
        }
    }

    void PrintDoList() {
        String string = "棋谱输出:";
        for (int i = 0; i < FiveChessView2.stepNum; ++i) {
            final String format = String.format("%c%c ", this.di_cover_1(FiveChessView2.chessList[i] / FiveChessView2.GRID_NUMBER), this.di_cover_2(FiveChessView2.chessList[i] % FiveChessView2.GRID_NUMBER));
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(format);
            string = sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append("\r\n");
        LogUtils.i(sb2.toString());
    }

    public void ToastOver() {
        Toast.makeText(this.getContext(), (CharSequence) "游戏已经结束，请重新开始！", Toast.LENGTH_SHORT).show();
    }

    public void UnDoStone() {
        if (FiveChessView2.stepNum == 0) {
            return;
        }
        if (FiveChessView2.stepNum > 0) {
            FiveChessView2.turnWho = oppTurn(FiveChessView2.turnWho);
            FiveChessView2.chessArray[FiveChessView2.chessList[FiveChessView2.stepNum - 1] / FiveChessView2.GRID_NUMBER][FiveChessView2.chessList[FiveChessView2.stepNum - 1] % FiveChessView2.GRID_NUMBER] = 0;
            --FiveChessView2.stepNum;
            FiveChessView2.isGameOver = false;
            this.postInvalidate();
        }
    }

    public GomokuResult checkGameOver() {
        int i = 0;
        int n = 1;
        while (i < FiveChessView2.GRID_NUMBER) {
            int j = 0;
            while (j < FiveChessView2.GRID_NUMBER) {
                int n2 = n;
                if (FiveChessView2.chessArray[i][j] != 1) {
                    n2 = n;
                    if (FiveChessView2.chessArray[i][j] != 2) {
                        n2 = 0;
                    }
                }
                if ((FiveChessView2.chessArray[i][j] == 2 || FiveChessView2.chessArray[i][j] == 1) && this.isFiveSame(i, j)) {
                    GomokuResult n3;
                    if (FiveChessView2.chessArray[i][j] == 2) {
                        n3 = GomokuResult.WHITE_WIN;
                    } else {
                        n3 = GomokuResult.BLACK_WIN;
                    }
                    FiveChessView2.isGameOver = true;
                    if (this.callBack != null) {
                        this.callBack.onGameOver(n3);
                        return n3;
                    }
                    return n3;
                } else {
                    ++j;
                    n = n2;
                }
            }
            ++i;
        }
        if (n != 0) {
            FiveChessView2.isGameOver = true;
            if (this.callBack != null) {
                this.callBack.onGameOver(GomokuResult.DREW);
                return GomokuResult.DREW;
            }
        }
        return GomokuResult.UNKNOWN;
    }

    char di_cover_1(final int n) {
        switch (n) {
            default: {
                return '?';
            }
            case 14: {
                return 'O';
            }
            case 13: {
                return 'N';
            }
            case 12: {
                return 'M';
            }
            case 11: {
                return 'L';
            }
            case 10: {
                return 'K';
            }
            case 9: {
                return 'J';
            }
            case 8: {
                return 'I';
            }
            case 7: {
                return 'H';
            }
            case 6: {
                return 'G';
            }
            case 5: {
                return 'F';
            }
            case 4: {
                return 'E';
            }
            case 3: {
                return 'D';
            }
            case 2: {
                return 'C';
            }
            case 1: {
                return 'B';
            }
            case 0: {
                return 'A';
            }
        }
    }

    char di_cover_2(final int n) {
        switch (n) {
            default: {
                return '?';
            }
            case 14: {
                return '1';
            }
            case 13: {
                return '2';
            }
            case 12: {
                return '3';
            }
            case 11: {
                return '4';
            }
            case 10: {
                return '5';
            }
            case 9: {
                return '6';
            }
            case 8: {
                return '7';
            }
            case 7: {
                return '8';
            }
            case 6: {
                return '9';
            }
            case 5: {
                return 'a';
            }
            case 4: {
                return 'b';
            }
            case 3: {
                return 'c';
            }
            case 2: {
                return 'd';
            }
            case 1: {
                return 'e';
            }
            case 0: {
                return 'f';
            }
        }
    }

    protected void do_chess_number(final Canvas canvas, int n, final int n2, final int n3, final int color) {
        ++n;
        final String format = String.format("%d", n);
        final float n4 = n2 * this.preWidth + this.offset;
        final float n5 = n3 * this.preWidth + this.offset;
        if (n == FiveChessView2.stepNum) {
            this.paint.setARGB(255, 190, 190, 0);
            canvas.drawRect(n4 - this.DD * 3.0f, n5 - this.DD * 2.0f, n4 + this.DD * 3.0f, n5 + this.DD * 2.0f, this.paint);
        }
        this.paint.setColor(color);
        if (n < 10) {
            canvas.drawText(format, n4 - this.DD, n5 + this.DD, this.paint);
            return;
        }
        if (n < 100) {
            canvas.drawText(format, n4 - this.DD * 2.0f, n5 + this.DD, this.paint);
            return;
        }
        canvas.drawText(format, n4 - this.DD * 3.0f, n5 + this.DD, this.paint);
    }

    public int[][] getChessArray() {
        return FiveChessView2.chessArray;
    }

    public int[] getChessList() {
        return FiveChessView2.chessList;
    }

    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        int n;
        if (this.getWidth() > this.getHeight()) {
            n = this.getHeight();
        } else {
            n = this.getWidth();
        }
        this.len = n;
        this.preWidth = this.len / FiveChessView2.GRID_NUMBER;
        this.paint.setTextSize(this.preWidth / 3.0f);
        this.paint.setColor(Color.BLACK);
        this.offset = this.preWidth / 2.0f;
        this.DD = this.preWidth / 10.0f;
        for (int i = 0; i < FiveChessView2.GRID_NUMBER; ++i) {
            final float n2 = i * this.preWidth + this.offset;
            canvas.drawLine(this.offset, n2, this.len - this.offset, n2, this.paint);
            canvas.drawLine(n2, this.offset, n2, this.len - this.offset, this.paint);
            canvas.drawText(String.format("%c", i + 65), n2 - this.preWidth / 5.0f, this.len - this.offset + this.preWidth / 2.0f, this.paint);
            final int n3 = 15 - i;
            String s;
            if (n3 < 10) {
                s = String.format("%d", n3);
            } else {
                s = String.format("%c", n3 + 97 - 10);
            }
            canvas.drawText(s, this.offset - this.preWidth / 2.0f, n2 + this.preWidth / 5.0f, this.paint);
        }
        this.paint.setColor(Color.BLACK);
        canvas.drawLine(this.offset - this.DD, this.offset - this.DD, this.len - this.offset + this.DD, this.offset - this.DD, this.paint);
        canvas.drawLine(this.offset - this.DD, this.len - this.offset + this.DD, this.len - this.offset + this.DD, this.len - this.offset + this.DD, this.paint);
        canvas.drawLine(this.offset - this.DD, this.offset - this.DD, this.offset - this.DD, this.len - this.offset + this.DD, this.paint);
        canvas.drawLine(this.len - this.offset + this.DD, this.offset - this.DD, this.len - this.offset + this.DD, this.len - this.offset + this.DD, this.paint);
        this.paint.setColor(Color.BLACK);
        canvas.drawRect(this.preWidth * 3.0f + this.offset - this.DD, this.preWidth * 3.0f + this.offset - this.DD, this.preWidth * 3.0f + this.offset + this.DD, this.preWidth * 3.0f + this.offset + this.DD, this.paint);
        canvas.drawRect(this.preWidth * 3.0f + this.offset - this.DD, this.preWidth * 11.0f + this.offset - this.DD, this.preWidth * 3.0f + this.offset + this.DD, this.preWidth * 11.0f + this.offset + this.DD, this.paint);
        canvas.drawRect(this.preWidth * 11.0f + this.offset - this.DD, this.preWidth * 11.0f + this.offset - this.DD, this.preWidth * 11.0f + this.offset + this.DD, this.preWidth * 11.0f + this.offset + this.DD, this.paint);
        canvas.drawRect(this.preWidth * 7.0f + this.offset - this.DD, this.preWidth * 7.0f + this.offset - this.DD, this.preWidth * 7.0f + this.offset + this.DD, this.preWidth * 7.0f + this.offset + this.DD, this.paint);
        canvas.drawRect(this.preWidth * 11.0f + this.offset - this.DD, this.preWidth * 3.0f + this.offset - this.DD, this.preWidth * 11.0f + this.offset + this.DD, this.preWidth * 3.0f + this.offset + this.DD, this.paint);
        this.paint.setColor(Color.BLACK);
        for (int j = 0; j < FiveChessView2.GRID_NUMBER; ++j) {
            for (int k = 0; k < FiveChessView2.GRID_NUMBER; ++k) {
                int n4;
                for (n4 = 0; n4 <=  stepNum && ( chessList[n4] / 15 != j ||  chessList[n4] % 15 != k); ++n4) {
                }
                final float n5 = this.offset + j * this.preWidth;
                final float n6 = this.offset + k * this.preWidth;
                this.rect.set((int) (n5 - this.offset), (int) (n6 - this.offset), (int) (n5 + this.offset), (int) (n6 + this.offset));
                switch (FiveChessView2.chessArray[j][k]) {
                    case 2: {
                        canvas.drawBitmap(FiveChessView2.whiteChess, (Rect) null, this.rect, this.paint);
                        this.do_chess_number(canvas, n4, j, k, Color.BLACK);
                        break;
                    }
                    case 1: {
                        canvas.drawBitmap(FiveChessView2.blackChess, (Rect) null, this.rect, this.paint);
                        this.do_chess_number(canvas, n4, j, k, -1);
                        break;
                    }
                }
            }
        }
        if (FiveChessView2.bestset > 0) {
            final int bestx = FiveChessView2.bestx;
            final int besty = FiveChessView2.besty;
            this.paint.setColor(Color.BLUE);
            final float n7 = bestx;
            final float preWidth = this.preWidth;
            final float offset = this.offset;
            final float dd = this.DD;
            final float n8 = besty;
            canvas.drawRect(preWidth * n7 + offset - dd * 2.0f, this.preWidth * n8 + this.offset - this.DD * 2.0f, n7 * this.preWidth + this.offset + this.DD * 2.0f, n8 * this.preWidth + this.offset + this.DD * 2.0f, this.paint);
        }
    }

    protected void onMeasure(int n, int size) {
        super.onMeasure(n, size);
        final int size2 = View.MeasureSpec.getSize(n);
        size = View.MeasureSpec.getSize(size);
        int n2;
        if (size2 > size) {
            n2 = size2;
        } else {
            n2 = size;
        }
        n = size2;
        if (size2 > size) {
            n = size;
        }
        final int n3 = n2 * 80 / 100;
        if ((size = n) > n3) {
            size = n3;
        }
        this.setMeasuredDimension(size, size + 20);
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            default: {
                return false;
            }
            case 1: {
                if (FiveChessView2.FIVE_DEBUG > 0) {
                    LogUtils.i("ACTION_UP:0 \r\n");
                }
                if (!FiveChessView2.isGameOver && !FiveChessView2.isThinking) {
                    if (FiveChessView2.FIVE_DEBUG > 0) {
                        LogUtils.i("ACTION_UP:a \r\n");
                    }
                    this.upX = motionEvent.getX();
                    this.upY = motionEvent.getY();
                    if (this.dx < 0 || this.dy < 0) {
                        break;
                    }
                    if (FiveChessView2.FIVE_DEBUG > 0) {
                        LogUtils.i("ACTION_UP:b \r\n");
                    }
                    this.ux = (int) (this.upX / this.preWidth);
                    this.uy = (int) (this.upY / this.preWidth);
                    final String format = String.format("ACTION_UP:%c%c \r\n", this.di_cover_1(this.ux), this.di_cover_2(this.uy));
                    if (FiveChessView2.FIVE_DEBUG > 0) {
                        LogUtils.i(format);
                    }
                    final String format2 = String.format("ACTION_UP:%d  %d \r\n", this.ux, this.uy);
                    if (FiveChessView2.FIVE_DEBUG > 0) {
                        LogUtils.i(format2);
                    }
                    if (FiveChessView2.chessArray[this.dx][this.dy] == 2 || FiveChessView2.chessArray[this.dx][this.dy] == 1) {
                        break;
                    }
                    final int turnWho = FiveChessView2.turnWho;
                    if (Math.abs(this.ux - this.dx) <= 1 && Math.abs(this.uy - this.dy) <= 1) {

                        this.DoStone(FiveChessView2.turnWho, this.dx, this.dy, "Player");
                    } else {

                        this.DoStone(FiveChessView2.turnWho, this.dx, this.dy, "Player--滑动操作--布局落子");
                    }
                    if (this.callBack != null) {
                        this.callBack.aiTEFACES(FiveChessView2.DO_DOSTONE, oppTurn(FiveChessView2.turnWho), this.dx, this.dy, FiveChessView2.stepNum);
                    }
                    final GomokuResult checkGameOver = this.checkGameOver();
                    this.Print(checkGameOver);
                    if (!FiveChessView2.isGameOver) {
                        if (Math.abs(this.ux - this.dx) <= 1 && Math.abs(this.uy - this.dy) <= 1 && this.callBack != null) {
                            this.dx = -1;
                            this.dy = -1;
                            this.callBack.FaceMode(true);
                            this.callBack.aiRun(FiveChessView2.turnWho);
                            return false;
                        }
                        break;
                    } else {
                        if (checkGameOver == GomokuResult.WHITE_WIN || checkGameOver == GomokuResult.BLACK_WIN) {
                            if (turnWho == 2) {
                                LogUtils.i("玩家执白棋赢!player take white win!\r\n");
                            }
                            if (turnWho == 1) {
                                LogUtils.i("玩家执黑棋赢!player take black win!\r\n");
                            }
                            this.PrintDoList();
                            return false;
                        }
                        break;
                    }
                } else {
                    if (FiveChessView2.isGameOver) {
                        this.ToastOver();
                        return false;
                    }
                    break;
                }
            }
            case 0: {
                if (FiveChessView2.FIVE_DEBUG > 0) {
                    LogUtils.i("ACTION_DOWN:0 \r\n");
                }
                this.downX = motionEvent.getX();
                this.downY = motionEvent.getY();
                this.dx = (int) (this.downX / this.preWidth);
                this.dy = (int) (this.downY / this.preWidth);
                if (this.downX < this.offset / 2.0f || this.downX > this.len - this.offset / 2.0f || this.downY < this.offset / 2.0f || this.downY > this.len - this.offset / 2.0f || FiveChessView2.chessArray[this.dx][this.dy] == 2 || FiveChessView2.chessArray[this.dx][this.dy] == 1) {
                    if (FiveChessView2.FIVE_DEBUG > 0) {
                        LogUtils.i("ACTION_DOWN:b \r\n");
                        LogUtils.i(String.format("ACTION_DOWN:%d %d \r\n", this.dx, this.dy));
                    }
                    this.dx = -1;
                    this.dy = -1;
                    break;
                }
                if (FiveChessView2.FIVE_DEBUG > 0) {
                    LogUtils.i("ACTION_DOWN:a \r\n");
                }
                final String format3 = String.format("ACTION_DOWN:%c%c \r\n", this.di_cover_1(this.dx), this.di_cover_2(this.dy));
                if (FiveChessView2.FIVE_DEBUG > 0) {
                    LogUtils.i(format3);
                }
                final String format4 = String.format("ACTION_DOWN:%d  %d \r\n", this.dx, this.dy);
                if (FiveChessView2.FIVE_DEBUG > 0) {
                    LogUtils.i(format4);
                    return false;
                }
                break;
            }
        }
        return false;
    }

    public void resetGame() {
        for (int i = 0; i < FiveChessView2.GRID_NUMBER; ++i) {
            for (int j = 0; j < FiveChessView2.GRID_NUMBER; ++j) {
                FiveChessView2.chessArray[i][j] = 0;
            }
        }
        FiveChessView2.isGameOver = false;
        FiveChessView2.stepNum = 0;
        FiveChessView2.stepFarNum = 0;
        FiveChessView2.isThinking = false;
        FiveChessView2.turnWho = 1;
        FiveChessView2.bestset = 0;
        this.postInvalidate();
    }

    public void setCallBack(final GameCallBack callBack) {
        this.callBack = callBack;
    }
}
