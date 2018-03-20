package dinson.customview.weight.dialog

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import com.jakewharton.rxbinding2.view.RxView
import dinson.customview.R
import dinson.customview.kotlin.*
import dinson.customview.utils.SoftHideKeyBoardUtil
import kotlinx.android.synthetic.main.dialog_001_login.*
import kotlinx.android.synthetic.main.dialog_001_register.*
import java.util.concurrent.TimeUnit


/**
 *  玩安卓登录框
 */
class _001DialogLogin(context: Context, theme: Int = R.style.BaseDialogTheme) : Dialog(context, theme) {

    init {
        init()
    }

    private fun init() {
        //设置是否可取消
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        setContentView(R.layout.dialog_001_login)
        initList()
    }

    private fun initList() {
        RxView.clicks(fabButton).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            context.closeKeybord(this@_001DialogLogin.currentFocus)
            if (llRegister?.visibility == View.VISIBLE) {
                animateRevealClose()
                return@subscribe
            }
            vsContent?.let { vsContent.inflate() }
            val offsetX = llLogin.x + llLogin.halfWidth() - fabButton.x - fabButton.halfWidth()
            val offsetY = llLogin.y + 20 - fabButton.y - fabButton.halfHeight()
            val path = fabButton.createArcPath(offsetX, offsetY)

            ValueAnimator.ofFloat(0f, 1f).apply {
                addUpdateListener(fabButton.getArcListener(path))
                onEnd {
                    fabButton.setImageResource(R.drawable.plus_x)
                    animateRevealShow()
                }
            }.start()
        }
    }

    /**
     * 水波纹开启
     */
    private fun animateRevealShow() {
        llRegister.show()
        val endRadius = Math.hypot(llRegister.halfWidth().toDouble(), llRegister.height.toDouble()).toFloat()
        ViewAnimationUtils.createCircularReveal(llRegister,
            llRegister.halfWidth().toInt(), 0, 0f, endRadius).apply {
            duration = 500
            interpolator = AccelerateInterpolator()
        }.start()
    }

    /**
     * 关闭水波纹
     */
    private fun animateRevealClose() {
        val startRadius = Math.hypot(llRegister.halfWidth().toDouble(), llRegister.height.toDouble()).toFloat()
        ViewAnimationUtils.createCircularReveal(llRegister, llRegister.halfWidth().toInt(), 0,
            startRadius, 0f).onEnd {
            llRegister.hide()
            resetFabBtn()
        }.animator().apply {
            duration = 500
            interpolator = AccelerateInterpolator()
        }.start()
    }

    /**
     * 重置fab按钮
     */
    private fun resetFabBtn() {
        fabButton.setImageResource(R.drawable.plus)
        val path = fabButton.createArcPath(0f, 0f)
        ValueAnimator.ofFloat(0f, 1f).apply {
            startDelay = 100L
            addUpdateListener(fabButton.getArcListener(path))
        }.start()
    }
}