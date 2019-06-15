package dinson.customview.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bf92.ai.AI;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.utils.LogUtils;
import dinson.customview.weight._026fivechess.AICallBack;
import dinson.customview.weight._026fivechess.FiveChessView2;
import dinson.customview.weight._026fivechess.GameCallBack;
import dinson.customview.weight._026fivechess.GomokuResult;

public class test extends BaseActivity implements GameCallBack, AICallBack, View.OnClickListener {
    public static int DO_CGO = 6;
    public static int DO_DEBUG = 8;
    public static int DO_DOSTONE = 3;
    public static int DO_INITION = 1;
    public static int DO_NEWGAME = 2;
    public static int DO_STEPTIME = 7;
    public static int DO_STOP = 5;
    public static int DO_UNDO = 4;
    public static int stepTime = 5; 
    private AI ai;
    private PopupWindow chooseChess;
    private FiveChessView2 fiveChessView;
    private ImageButton mBtnShowMenu;


    private void initPop(int do_DEBUG, final int n) {
        if (this.chooseChess == null) {
            final View inflate = View.inflate((Context) this, R.layout.pop_choose_chess, (ViewGroup) null);
            final ImageButton imageButton = (ImageButton) inflate.findViewById(R.id.choose_black);
            final ImageButton imageButton2 = (ImageButton) inflate.findViewById(R.id.choose_white);
            final Button button = (Button) inflate.findViewById(R.id.choose_cancel);
            imageButton.setOnClickListener((View.OnClickListener) this);
            imageButton2.setOnClickListener((View.OnClickListener) this);
            button.setOnClickListener((View.OnClickListener) this);
            (this.chooseChess = new PopupWindow(inflate, do_DEBUG, n)).setOutsideTouchable(false);
            (this.ai = new AI(this.fiveChessView.getChessArray(), this.fiveChessView.getChessList(), this)).book(this.getAssets(), "bg.png");
            FiveChessView2.FIVE_DEBUG = 0;
            do_DEBUG = test.DO_DEBUG;
            final FiveChessView2 fiveChessView = this.fiveChessView;
            this.aiTEFACES(do_DEBUG, FiveChessView2.turnWho, 0, 0, FiveChessView2.FIVE_DEBUG);
            this.GameNew();
            this.aiRun(FiveChessView2.turnWho);
        }
    }


    private void initViews() {
        (this.fiveChessView =   this.findViewById(R.id.five_chess_view)).setCallBack(this);
        this.fiveChessView.setClickable(true);
        this.findViewById(R.id.restart_game).setOnClickListener(this);
        this.findViewById(R.id.btn_back).setOnClickListener(this);
        this.findViewById(R.id.btn_farw).setOnClickListener(this);
        this.findViewById(R.id.btn_stop).setOnClickListener(this);
        this.findViewById(R.id.btn_cgo).setOnClickListener(this);
        (this.mBtnShowMenu = this.findViewById(R.id.btn_set)).setOnClickListener(view ->
                test.this.mBtnShowMenu.showContextMenu());
        this.mBtnShowMenu.setOnCreateContextMenuListener((contextMenu, view, contextMenu$ContextMenuInfo) -> {
            String s;
            if (test.stepTime == 2) {
                s = "单步思考时间2秒  已选中";
            } else {
                s = "单步思考时间2秒";
            }
            contextMenu.add(0, 1, 0, (CharSequence) s);
            String s2;
            if (test.stepTime == 5) {
                s2 = "单步思考时间5秒  已选中";
            } else {
                s2 = "单步思考时间5秒";
            }
            contextMenu.add(0, 2, 0, (CharSequence) s2);
            String s3;
            if (test.stepTime == 10) {
                s3 = "单步思考时间10秒 已选中";
            } else {
                s3 = "单步思考时间10秒";
            }
            contextMenu.add(0, 3, 0, (CharSequence) s3);
            String s4;
            if (test.stepTime == 60) {
                s4 = "单步思考时间60秒 已选中";
            } else {
                s4 = "单步思考时间60秒";
            }
            contextMenu.add(0, 4, 0, (CharSequence) s4);
        });
    }

    private void showToast(final String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void BtmSet(final int n, final boolean b) {
        final ImageButton imageButton = this.findViewById(n);
        if (b) {
            imageButton.setEnabled(true);
            imageButton.setClickable(true);
            imageButton.getBackground().clearColorFilter();
            return;
        }
        imageButton.setEnabled(false);
        imageButton.setClickable(false);
        imageButton.getBackground().clearColorFilter();
        imageButton.getBackground().setColorFilter(-7829368, PorterDuff.Mode.SRC_ATOP);
    }


    /**
     * 退出程序
     */
    public void CloseWin() {
        final int do_STOP = test.DO_STOP;
        final int turnWho = FiveChessView2.turnWho;
        this.aiTEFACES(do_STOP, turnWho, 0, 0, FiveChessView2.stepNum);
        FiveChessView2.FIVE_DEBUG = 0;
        final int do_DEBUG = test.DO_DEBUG;
        this.aiTEFACES(do_DEBUG, FiveChessView2.turnWho, 0, 0, 0);
        this.finish();
    }

    @Override
    public void FaceMode(final boolean b) {
        if (!b) {
            this.BtmSet(R.id.restart_game, true);
            this.BtmSet(R.id.btn_back, true);
            this.BtmSet(R.id.btn_farw, true);
            this.BtmSet(R.id.btn_stop, false);
            this.BtmSet(R.id.btn_set, true);
            this.BtmSet(R.id.btn_cgo, true);
            return;
        }
        this.BtmSet(R.id.restart_game, false);
        this.BtmSet(R.id.btn_back, false);
        this.BtmSet(R.id.btn_farw, false);
        this.BtmSet(R.id.btn_stop, true);
        this.BtmSet(R.id.btn_set, false);
        this.BtmSet(R.id.btn_cgo, false);
    }

    public void GameNew() {
        LogUtils.i("Main:GameNew\r\n");
        this.fiveChessView.resetGame();
        final int do_NEWGAME = test.DO_NEWGAME;
        final FiveChessView2 fiveChessView = this.fiveChessView;
        this.aiTEFACES(do_NEWGAME, 1, 0, 0, FiveChessView2.stepNum);
        final int do_STEPTIME = test.DO_STEPTIME;
        final FiveChessView2 fiveChessView2 = this.fiveChessView;
        this.aiTEFACES(do_STEPTIME, FiveChessView2.turnWho, 0, 0, test.stepTime);
        final int do_DEBUG = test.DO_DEBUG;
        final FiveChessView2 fiveChessView3 = this.fiveChessView;
        this.aiTEFACES(do_DEBUG, FiveChessView2.turnWho, 0, 0, FiveChessView2.FIVE_DEBUG);
    }

    @Override
    public void onGameOver(final GomokuResult result) {
        switch (result) {
            case WHITE_WIN:
                this.showToast("白棋胜利！");
                break;
            case BLACK_WIN:
                this.showToast("黑棋胜利！");
                break;
            case DREW:
                this.showToast("平局！");
                break;
            case UNKNOWN:
                this.showToast("未知！");
                break;
        }
    }



    public void SoundNotificatong() {
        RingtoneManager.getRingtone(this.getApplicationContext(), RingtoneManager.getDefaultUri(2)).play();
    }

    @Override
    public void aiCompleted(final int n, final int n2, final int n3, final int n4) {
        this.runOnUiThread(() -> {
            if (n == 0) {
                if (n2 >= 0 && n3 >= 0 && n2 < FiveChessView2.GRID_NUMBER && n3 < FiveChessView2.GRID_NUMBER) {
                    test.this.fiveChessView.DoStone(n4, n2, n3, "ai");
                    test.this.fiveChessView.Print(test.this.fiveChessView.checkGameOver());
                    FiveChessView2.isThinking = false;
                } else {
                    FiveChessView2.isThinking = false;
                     LogUtils.i("mm:无法落子\r\n");
                    test.this.fiveChessView.ToastOver();
                }
                FiveChessView2.bestset = 0;
                test.this.FaceMode(false);
                test.this.fiveChessView.postInvalidate();
                return;
            }
             LogUtils.i("mm:type error【注意】\r\n");
        });
    }


    @Override
    public void aiRun(final int aiChess) {
        FiveChessView2.isThinking = true;
        this.ai.setAiChess(aiChess);
        this.ai.aiBout();
    }

    @Override
    public void aiTEFACES(final int n, final int n2, final int n3, final int n4, final int n5) {
        this.ai.aiTEFACES(n, n2, n3, n4, n5);
    }

    public void onClick(final View view) {
        final int id = view.getId();
        if (id != R.id.restart_game) {
            switch (id) {
                default: {
                    switch (id) {
                        default: {
                            LogUtils.i("onClick:default\r\n");
                            return;
                        }
                        case R.id.choose_white: {
                            this.GameNew();
                            this.chooseChess.dismiss();
                            this.aiRun(FiveChessView2.turnWho);
                            return;
                        }
                        case R.id.choose_cancel: {
                            this.chooseChess.dismiss();
                            return;
                        }
                        case R.id.choose_black: {
                            this.GameNew();
                            this.chooseChess.dismiss();
                            return;
                        }
                    }
                }
                case R.id.btn_stop: {
                    LogUtils.i("Cmd:stop\r\n");
                    final int do_STOP = test.DO_STOP;
                    final int turnWho = FiveChessView2.turnWho;
                    this.aiTEFACES(do_STOP, turnWho, 0, 0, FiveChessView2.stepNum);
                }
                case R.id.btn_set: {
                    this.fiveChessView.checkGameOver();
                    if (FiveChessView2.isThinking) {
                        return;
                    }
                    LogUtils.i("Cmd:set\r\n");
                }
                case R.id.btn_farw: {
                    if (FiveChessView2.isGameOver) {
                        LogUtils.i("Cmd:无法前进 游戏结束\r\n");
                        return;
                    }
                    if (FiveChessView2.isThinking) {
                        LogUtils.i("Cmd:无法前进 机器思考中\r\n");
                        return;
                    }
                    final int stepFarNum = FiveChessView2.stepFarNum;
                    if (stepFarNum > FiveChessView2.stepNum) {
                        LogUtils.i("Cmd:前进 Go Forward\r\n");
                        this.fiveChessView.FarW();
                        return;
                    }
                    LogUtils.i("Cmd:无法前进 前面无子了Forward is none\r\n");
                }
                case R.id.btn_cgo: {
                    if (FiveChessView2.isThinking) {
                        return;
                    }
                    LogUtils.i("Cmd:cgo\r\n");
                    this.FaceMode(true);
                    this.aiRun(FiveChessView2.turnWho);
                }
                case R.id.btn_back: {
                    if (FiveChessView2.isThinking) {
                        return;
                    }
                    if (FiveChessView2.stepNum > 0) {
                        LogUtils.i("Cmd:后退 Go Back \r\n");
                        final int do_UNDO = test.DO_UNDO;
                        final int turnWho2 = FiveChessView2.turnWho;
                        this.aiTEFACES(do_UNDO, turnWho2, 0, 0, FiveChessView2.stepNum);
                        this.fiveChessView.UnDoStone();
                        return;
                    }
                    LogUtils.i("Cmd:没子可退 Back is none\r\n");
                }
            }
        } else {
            if (FiveChessView2.isThinking) {
                return;
            }
            this.chooseChess.showAtLocation( this.fiveChessView, 17, 0, 0);
        }
    }

    public boolean onContextItemSelected(final MenuItem menuItem) {
        if (menuItem.getGroupId() != 0) {
            return true;
        }
        switch (menuItem.getItemId()) {
            default: {
                return true;
            }
            case 10: {
                this.CloseWin();
                return true;
            }
            case 6: {
               LogUtils.i(  "Cmd:清屏\r\n");
                return true;
            }

            case 4: {
                LogUtils.i("Cmd:设置单步思考时间为60秒\r\n");
                test.stepTime = 60;
                final int do_STEPTIME = test.DO_STEPTIME;
                final FiveChessView2 fiveChessView = this.fiveChessView;
                this.aiTEFACES(do_STEPTIME, FiveChessView2.turnWho, 0, 0, test.stepTime);
                return true;
            }
            case 3: {
                LogUtils.i("Cmd:设置单步思考时间为10秒\r\n");
                test.stepTime = 10;
                final int do_STEPTIME2 = test.DO_STEPTIME;
                final FiveChessView2 fiveChessView2 = this.fiveChessView;
                this.aiTEFACES(do_STEPTIME2, FiveChessView2.turnWho, 0, 0, test.stepTime);
                return true;
            }
            case 2: {
                LogUtils.i("Cmd:设置单步思考时间为5秒\r\n");
                test.stepTime = 5;
                final int do_STEPTIME3 = test.DO_STEPTIME;
                final FiveChessView2 fiveChessView3 = this.fiveChessView;
                this.aiTEFACES(do_STEPTIME3, FiveChessView2.turnWho, 0, 0, test.stepTime);
                return true;
            }
            case 1: {
                LogUtils.i("Cmd:设置单步思考时间为2秒\r\n");
                test.stepTime = 2;
                final int do_STEPTIME4 = test.DO_STEPTIME;
                final FiveChessView2 fiveChessView4 = this.fiveChessView;
                this.aiTEFACES(do_STEPTIME4, FiveChessView2.turnWho, 0, 0, test.stepTime);
                return true;
            }
        }
    }

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_test2);
        this.initViews(); 
        LogUtils.i("\r\n");
        this.fiveChessView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            final WindowManager windowManager = (WindowManager) test.this.getSystemService(Context.WINDOW_SERVICE);
            test.this.initPop(windowManager.getDefaultDisplay().getWidth(), windowManager.getDefaultDisplay().getHeight());
        });
        // this.initSound();
    }


    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        if (n == 4) {
            new AlertDialog.Builder((Context) this).setTitle((CharSequence) "注意").setMessage((CharSequence) "确定要退出吗？").setPositiveButton((CharSequence) "确定", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    test.this.CloseWin();
                }
            }).setNegativeButton((CharSequence) "取消", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                }
            }).show();
        }
        return false;
    }

    public void playSound(final int n) {
        if (FiveChessView2.FIVE_DEBUG != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Main:playSound");
            sb.append(String.format("[%d]", n));
            sb.append("\r\n");
            LogUtils.i(sb.toString());
        }
        //GomokuView.soundPool.play(n, 0.1f, 0.5f, 0, 0, 1.0f);
    }
}
