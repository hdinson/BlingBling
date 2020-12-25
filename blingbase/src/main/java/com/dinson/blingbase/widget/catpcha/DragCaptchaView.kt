package com.dinson.blingbase.widget.catpcha

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dinson.blingbase.R
import com.dinson.blingbase.kotlin.click
import com.dinson.blingbase.kotlin.dip
import com.dinson.blingbase.kotlin.setCompoundDrawablesRes
import com.dinson.blingbase.kotlin.show
import com.dinson.blingbase.widget.catpcha.DragCaptchaView.MODE.Companion.WITHOUT_BAR
import com.dinson.blingbase.widget.catpcha.DragCaptchaView.MODE.Companion.WITH_BAR

@Suppress("unused")
class DragCaptchaView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val mSettings = DragCaptchaViewSettings(context, attrs)
    private var tvCaptchaMsg: TextView
    private var pictureVerifyView: PictureVerifyView

    private var textSeekBar: TextSeekBar

    private var failCount = 0

    //处理滑动条逻辑
    private var isResponse = false
    private var isDown = false
    private var mListener: CaptchaListener? = null


    init {
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutParams = lp
        val v = LayoutInflater.from(context).inflate(R.layout.view_captcha_container, this, true)
        v.layoutParams = lp
        tvCaptchaMsg = v.findViewById(R.id.tvCaptchaMsg)
        v.findViewById<View>(R.id.vRefreshCaptchaPic).click {
            startRefresh(it)
        }
        pictureVerifyView = v.findViewById(R.id.vertifyView)
        textSeekBar = v.findViewById(R.id.textSeekBar)
        setDragCaptchaMode(mSettings.mMode)
        pictureVerifyView.setImageResource(mSettings.drawableId)
        setBlockSize(mSettings.blockSize)
        pictureVerifyView.callback(object :
            PictureVerifyView.Callback {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(time: Long) {
                tvCaptchaMsg.text = if (mListener == null) "验证通过,耗时${time}毫秒"
                else mListener?.onAccess(time)
                tvCaptchaMsg.setCompoundDrawablesRes(R.drawable.view_captcha_right)
                tvCaptchaMsg.show()
            }

            @SuppressLint("SetTextI18n")
            override fun onFailed() {
                textSeekBar.isEnabled = false
                pictureVerifyView.setTouchEnable(false)
                failCount =
                    if (failCount > mSettings.maxFailedCount) mSettings.maxFailedCount else failCount + 1

                if (mSettings.maxFailedCount in 1..failCount) {
                    tvCaptchaMsg.text = if (mListener == null) "验证失败,已达到最大错误次数"
                    else mListener?.onMaxFailed()
                } else {
                    tvCaptchaMsg.text = if (mListener != null) mListener!!.onFailed(failCount)
                    else {
                        if (mSettings.maxFailedCount > 0) "验证失败,还剩${mSettings.maxFailedCount - failCount}次机会"
                        else "验证失败"
                    }
                }
                tvCaptchaMsg.setCompoundDrawablesRes(R.drawable.view_captcha_wrong)
                tvCaptchaMsg.show()
            }
        })
        setSeekBarStyle(mSettings.progressDrawableId, mSettings.thumbDrawableId)
        textSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (isDown) {  //手指按下
                    isDown = false
                    if (progress > mSettings.thumbDrawableWidth.toFloat() / seekBar.measuredWidth * 100) { //按下位置不正确
                        isResponse = false
                    } else {
                        isResponse = true
                        tvCaptchaMsg.visibility = View.GONE
                        pictureVerifyView.down(0)
                    }
                }
                if (isResponse) {
                    pictureVerifyView.move(progress)
                } else {
                    seekBar.progress = 0
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isDown = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (isResponse) {
                    pictureVerifyView.loose()
                }
            }
        })
    }


    private fun startRefresh(v: View) {
        v.animate().rotationBy(360f).setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    reset(false)
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
    }

    fun setCaptchaListener(listener: CaptchaListener) {
        mListener = listener
    }

    fun setCaptchaStrategy(strategy: CaptchaStrategy) {
        pictureVerifyView.setCaptchaStrategy(strategy)
    }

    fun setSeekBarStyle(@DrawableRes progressDrawable: Int, @DrawableRes thumbDrawable: Int) {
        textSeekBar.progressDrawable = ContextCompat.getDrawable(context, progressDrawable)
        textSeekBar.thumb = ContextCompat.getDrawable(context, thumbDrawable)
        textSeekBar.thumbOffset = 0
    }

    /**
     * 设置滑块图片大小，单位px
     */
    fun setBlockSize(blockSize: Int) {
        pictureVerifyView.setBlockSize(blockSize)
    }

    /**
     * 设置滑块验证模式
     */
    fun setDragCaptchaMode(@MODE mode: Int) {
        mSettings.mMode = mode
        pictureVerifyView.setMode(mode)
        if (mode == MODE.WITHOUT_BAR) {
            textSeekBar.visibility = View.GONE
            pictureVerifyView.setTouchEnable(true)
        } else {
            textSeekBar.visibility = View.VISIBLE
            textSeekBar.isEnabled = true
        }
        tvCaptchaMsg.visibility = View.GONE
    }


    fun setBitmap(drawableId: Int) {
        val bitmap =
            BitmapFactory.decodeResource(resources, drawableId)
        setBitmap(bitmap)
    }

    fun setBitmap(bitmap: Bitmap) {
        pictureVerifyView.setImageBitmap(bitmap)
        reset(false)
    }

    fun setBitmap(url: String) {
        Glide.with(context).load(url).into(pictureVerifyView)
    }

    /**
     * 复位
     */
    fun reset(clearFailed: Boolean) {
        tvCaptchaMsg.visibility = View.GONE
        pictureVerifyView.reset()
        if (clearFailed) {
            failCount = 0
        }
        if (mSettings.mMode == MODE.WITH_BAR) {
            textSeekBar.isEnabled = true
            textSeekBar.progress = 0
        } else {
            pictureVerifyView.setTouchEnable(true)
        }
    }


    class DragCaptchaViewSettings internal constructor(context: Context, attrs: AttributeSet?) {

        var drawableId = 0
        var progressDrawableId = 0
        var thumbDrawableId = 0

        var thumbDrawableWidth: Int = 0

        @MODE
        var mMode: Int = WITH_BAR
        var maxFailedCount: Int
        var blockSize = 0

        init {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.DragCaptchaView)
            drawableId = ta.getResourceId(
                R.styleable.DragCaptchaView_dcvSrc,
                R.color.design_default_color_primary
            )
            progressDrawableId = ta.getResourceId(
                R.styleable.DragCaptchaView_dcvProgressDrawable, R.drawable.view_captcha_seekbar_bg
            )
            thumbDrawableId = ta.getResourceId(
                R.styleable.DragCaptchaView_dcvThumbDrawable, R.drawable.view_captcha_seekbar_thumb
            )
            ContextCompat.getDrawable(context, thumbDrawableId)?.let {
                thumbDrawableWidth = it.intrinsicWidth
            }
            mMode = ta.getInteger(R.styleable.DragCaptchaView_dcvMode, MODE.WITH_BAR)
            maxFailedCount = ta.getInteger(R.styleable.DragCaptchaView_dcvMaxFailCount, -1)
            blockSize = ta.getDimensionPixelSize(R.styleable.DragCaptchaView_dcvBlockSize, dip(50f))
            ta.recycle()
        }
    }

    @IntDef(WITH_BAR, WITHOUT_BAR)
    annotation class MODE {
        companion object {
            const val WITH_BAR = 1      //带滑动条验证模式
            const val WITHOUT_BAR = 2   //不带滑动条验证，手触模式
        }
    }
}