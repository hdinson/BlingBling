package dinson.customview.activity

import android.os.Bundle
import android.view.MotionEvent
import dinson.customview._global.BaseActivity
import dinson.customview.R
import dinson.customview.weight._026fivechess.AICallBack
import dinson.customview.weight._026fivechess.GameCallBack
import dinson.customview.weight._026fivechess.GomokuResult

class _026FiveChessActivity : BaseActivity(), GameCallBack, AICallBack {
    override fun onGameOver(result: GomokuResult?) {

    }

    override fun FaceMode(p0: Boolean) {
    }

    override fun aiRun(p0: Int) {
    }

    override fun aiTEFACES(p0: Int, p1: Int, p2: Int, p3: Int, p4: Int) {
    }

    override fun aiCompleted(p0: Int, p1: Int, p2: Int, p3: Int) {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__026_five_chess)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }
}
