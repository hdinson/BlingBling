package dinson.customview.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewAnimationUtils
import android.widget.ImageView
import android.widget.TextView
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.kotlin.dip
import dinson.customview.kotlin.screenHeight
import dinson.customview.kotlin.screenWidth
import dinson.customview.utils.TypefaceUtils
import dinson.customview.weight._006floatingview.Floating
import dinson.customview.weight._006floatingview.FloatingBuilder
import dinson.customview.weight._006floatingview.effect.EarthFloatingTransition
import dinson.customview.weight._006floatingview.effect.PlaneFloatingTransition
import dinson.customview.weight._006floatingview.effect.ScaleFloatingTransition
import dinson.customview.weight._006floatingview.effect.TranslateFloatingTransition
import kotlinx.android.synthetic.main.activity__006_floating_view.*
import java.util.*

class _006FloatingViewActivity : BaseActivity() {

    private lateinit var mFloating: Floating
    private var mTypeface: Typeface? = null

    private var mCount = 0//点赞计数
    private var toggle = true//背景转变
    private val mBgColor = arrayOf(Color.parseColor("#6d5f88"),
        Color.parseColor("#62a465"))
    //星星的中心点坐标
    private val mStartCenter by lazy {
        intArrayOf((ivStar.right - ivStar.left) / 2 + ivStar.left,
            (ivStar.bottom - ivStar.top) / 2 + ivStar.top)
    }
    //背景变换半径
    private val mBgChangeRadio by lazy {
        val maxWidth = maxOf(screenWidth() - mStartCenter[0], mStartCenter[0])
        val maxHeight = maxOf(screenHeight() - mStartCenter[1], mStartCenter[1])
        Math.hypot(maxWidth.toDouble(), maxHeight.toDouble()).toFloat()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__006_floating_view)

        mTypeface = TypefaceUtils.getAppleFont(this)
        mFloating = Floating(this)

        initUI()
    }

    private fun initUI() {
        //点赞
        ivLike.setOnClickListener {
            val layout = LayoutInflater.from(this).inflate(R.layout.layout_006_like, null, false)
            val tvNum = layout.findViewById<TextView>(R.id.tvNum)
            tvNum.text = String.format(Locale.CHINA, "+%d", ++mCount)
            val floatingElement = FloatingBuilder().anchorView(ivLike).targetView(layout)
                .floatingTransition(TranslateFloatingTransition()).build()
            mFloating.startFloating(floatingElement)
        }

        //星星
        ivStar.setOnClickListener {
            ivStar.isEnabled = false
            toggle = !toggle
            rootBg.setBackgroundColor(if (toggle) mBgColor[0] else mBgColor[1])
            layoutRoot.setBackgroundColor(if (toggle) mBgColor[1] else mBgColor[0])
            val mAnimator = ViewAnimationUtils
                .createCircularReveal(rootBg, mStartCenter[0], mStartCenter[1], 0f, mBgChangeRadio)
            mAnimator.duration = 700
            mAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    ivStar.isEnabled = true
                }
            })
            mAnimator.start()
        }

        //黑板
        ivSchool.setOnClickListener {
            val textView = TextView(this).apply {
                text = "4"
                setPadding(dip(20), dip(10), dip(20), dip(10))
                setBackgroundResource(R.drawable.shadow_ret_r2)
                typeface = mTypeface
            }
            val floatingSchool = FloatingBuilder().anchorView(ivSchool).targetView(textView)
                .offsetY(-ivSchool.measuredHeight).floatingTransition(ScaleFloatingTransition(2000))
                .build()
            mFloating.startFloating(floatingSchool)
        }

        //地球
        ivEarth.setOnClickListener {
            val imageView = ImageView(this)
            imageView.setImageResource(R.drawable._006_moon)
            val floatingEarth = FloatingBuilder().anchorView(ivEarth).targetView(imageView)
                .floatingTransition(EarthFloatingTransition()).build()
            mFloating.startFloating(floatingEarth)
        }

        //飞机
        ivPlane.setOnClickListener {
            val plane = ImageView(this)
            plane.setImageResource(R.drawable._006_plane)
            val floatingPlane = FloatingBuilder().anchorView(ivPlane).targetView(plane)
                .floatingTransition(PlaneFloatingTransition()).build()
            mFloating.startFloating(floatingPlane)
        }
    }
}