package dinson.customview.weight._002qqnaviview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import dinson.customview.R
import dinson.customview.utils.LogUtils

/**
 *   仿QQ底部菜单拖动效果
 */
class QQNaviView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private val mSettings = QQNaviViewSettings(context, attrs, defStyleAttr)

    private val mIvBig: ImageView
    private val mIvSmall: ImageView
    private val mImageContainer: FrameLayout

    /* 拖动幅度较大半径 */
    private var mBigRadius: Float = 0.toFloat()

    /* 拖动幅度小半径 */
    private var mSmallRadius: Float = 0.toFloat()

    init {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER

        mImageContainer = FrameLayout(context)
        val params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

        val paramsIcon = FrameLayout.LayoutParams(mSettings.iconWidth.toInt(), mSettings.iconHeight.toInt())
        mIvBig = ImageView(context)
        mIvBig.setImageResource(mSettings.bigIconSrc)
        mIvBig.setBackgroundResource(R.color.green)

        mIvSmall = ImageView(context)
        mIvSmall.setImageResource(mSettings.smallIconSrc)


        mImageContainer.addView(mIvBig, paramsIcon)
        mImageContainer.addView(mIvSmall, paramsIcon)
        addView(mImageContainer, params)

        if (mSettings.bottomText.isNotEmpty()) {
            //底部有文字
            val bottomText = TextView(context)

            bottomText.setBackgroundResource(R.color.gray)

            bottomText.text = mSettings.bottomText
            bottomText.textSize = mSettings.bottomTextSize
            val textParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            textParams.topMargin = mSettings.textPadding.toInt()
            addView(bottomText, textParams)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setupView()
    }


    /**
     * 确定view以及拖动相关参数
     */
    private fun setupView() {
        //根据view的宽高确定可拖动半径的大小
        mSmallRadius = 0.1f * Math.min(mImageContainer.measuredWidth, mImageContainer.measuredHeight).toFloat() * mSettings.range
        mBigRadius = 1.5f * mSmallRadius
        //设置imageView的padding，不然拖动时图片边缘部分会消失
        val padding = mBigRadius.toInt()
        mIvBig.setPadding(padding, padding, padding, padding)
        mIvSmall.setPadding(padding, padding, padding, padding)
    }

    private var lastX = 0f
    private var lastY = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastX
                val deltaY = y - lastY
                moveEvent(mIvBig, deltaX, deltaY, mSmallRadius)
                //因为可拖动大半径是小半径的1.5倍， 因此这里x,y也相应乘1.5
                moveEvent(mIvSmall, 1.5f * deltaX, 1.5f * deltaY, mBigRadius)
            }
            MotionEvent.ACTION_UP -> {
                //抬起时复位
                mIvBig.x = 0f
                mIvBig.y = 0f
                mIvSmall.x = 0f
                mIvSmall.y = 0f
            }
        }
        return true
    }

    /**
     * 拖动事件
     *
     * @param view   view
     * @param deltaX 轴距离
     * @param deltaY Y轴距离
     * @param radius 半径
     */
    private fun moveEvent(view: View, deltaX: Float, deltaY: Float, radius: Float) {
        //先计算拖动距离
        val distance = getDistance(deltaX, deltaY)
        //拖动的方位角，atan2出来的角度是带正负号的
        val degree = Math.atan2(deltaY.toDouble(), deltaX.toDouble())
        //如果大于临界半径就不能再往外拖了
        if (distance > radius) {
            view.x = view.left + (radius * Math.cos(degree)).toFloat()
            view.y = view.top + (radius * Math.sin(degree)).toFloat()
        } else {
            view.x = view.left + deltaX
            view.y = view.top + deltaY
        }
    }

    private fun getDistance(x: Float, y: Float) = Math.sqrt((x * x + y * y).toDouble()).toFloat()

    fun setChecked(checked: Boolean) {
        mIvBig.setImageResource(if (checked) mSettings.bigIconSrcCheck else mSettings.bigIconSrc)
        mIvSmall.setImageResource(if (checked) mSettings.smallIconSrcCheck else mSettings.smallIconSrc)
    }
}