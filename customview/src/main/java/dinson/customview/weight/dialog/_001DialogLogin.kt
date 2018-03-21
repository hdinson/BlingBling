package dinson.customview.weight.dialog

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import com.jakewharton.rxbinding2.view.RxView
import dinson.customview.R
import dinson.customview.api.WanAndroidApi
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.*
import kotlinx.android.synthetic.main.dialog_001_login.*
import kotlinx.android.synthetic.main.dialog_001_register.*
import java.util.concurrent.TimeUnit


/**
 *  玩安卓登录框
 */
class _001DialogLogin(context: Context, theme: Int = R.style.BaseDialogTheme) : Dialog(context, theme) {

    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/

    /**
     * 设置登陆是否成功的监听
     */
    fun setOnLoginSuccessListener(listener: OnLoginSuccessListener) {
        mSuccessListener = listener
    }

    /******************************************************************************************************/
    /**                             内部实现                                                              **/
    /******************************************************************************************************/

    private val mWanAndroidApi by lazy {
        HttpHelper.create(WanAndroidApi::class.java)
    }

    private var mSuccessListener: OnLoginSuccessListener? = null

    init {
        //设置是否可取消
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        setContentView(R.layout.dialog_001_login)
        initClick()
    }

    /**
     * 初始化各按钮的点击事件
     */
    private fun initClick() {
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

        btnDoLogin.click {
            if (etUsername.text.isEmpty()) {
                "Username must not null".toast()
                return@click
            }
            if (etPassword.text.isEmpty()) {
                "Password must not null".toast()
                return@click
            }
            btnDoLogin.isEnabled = false
            mWanAndroidApi.login(etUsername.text.toString(), etPassword.text.toString())
                .compose(RxSchedulers.io_main())
                .subscribe({
                    if (it.errorCode == 0) {
                        this.dismiss()
                        mSuccessListener?.onSuccess(true, "登录成功")
                    } else {
                        mSuccessListener?.onSuccess(true, it.errorMsg)
                    }
                }, {
                    mSuccessListener?.onSuccess(true, it.toString())
                })
        }
    }

    /**
     * 注册界面水波纹开启
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
     * 注册界面关闭水波纹
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