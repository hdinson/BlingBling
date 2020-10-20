package dinson.customview.activity

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import dinson.customview.R
import dinson.customview._global.BaseActivity
import com.dinson.blingbase.kotlin.click
import dinson.customview.weight._026fivechess.GameCallBack
import dinson.customview.weight._026fivechess.GomokuGameState
import kotlinx.android.synthetic.main.activity__026_five_chess.*

class _026FiveChessActivity : BaseActivity(), GameCallBack {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__026_five_chess)

        initUI()
        initClick()
    }

    private fun initUI() {
        gomokuView.setGameCallBack(this)
    }

    private val mChooseChessDialog by lazy {
        val view =
            View.inflate(this as Context, R.layout.dialog_026_choose_chess, null as ViewGroup?)
        val dialog = AlertDialog.Builder(this, R.style.BaseDialogTheme)
            .setView(view)
            .create()
        view.findViewById<View>(R.id.choose_black).click {
            dialog.dismiss()
            gomokuView.newGame()
        }
        view.findViewById<View>(R.id.choose_white).click {
            dialog.dismiss()
            gomokuView.newGame()
            gomokuView.aiRun()
        }
        view.findViewById<View>(R.id.choose_cancel).click {
            dialog.dismiss()
        }
        dialog
    }

    private fun initClick() {
        actionRefresh.click { mChooseChessDialog.show() }
        actionBack.click { gomokuView.goBack() }
        actionForward.click { gomokuView.goForward() }
        actionTips.click { }
        actionStop.click { gomokuView.stopAiRun() }
        actionSetting.click { }
    }


    override fun onAiRunState(isStart: Boolean) {
        val alpha = if (isStart) 0.5f else 1f
        val stopAlpha = if (isStart) 1f else 0.5f
        actionRefresh.alpha = alpha
        actionRefresh.isEnabled = !isStart
        actionBack.alpha = alpha
        actionBack.isEnabled = !isStart
        actionForward.alpha = alpha
        actionForward.isEnabled = !isStart
        actionTips.alpha = alpha
        actionTips.isEnabled = !isStart
        actionSetting.alpha = alpha
        actionSetting.isEnabled = !isStart
        actionStop.alpha = stopAlpha
        actionStop.isEnabled = isStart
    }

    override fun onChessChange() {
        if (gomokuView.isCanGoBack()) {
            actionBack.alpha = 1f
            actionBack.isEnabled = true
        } else {
            actionBack.alpha = 0.5f
            actionBack.isEnabled = false
        }
        if (gomokuView.isCanGoForward()) {
            actionForward.alpha = 1f
            actionForward.isEnabled = true
        } else {
            actionForward.alpha = 0.5f
            actionForward.isEnabled = false
        }
    }


    override fun onGameOver(state: GomokuGameState) {

    }

}
