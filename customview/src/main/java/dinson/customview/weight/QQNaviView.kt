package dinson.customview.weight

import android.content.Context
import android.content.res.TypedArray
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout

import dinson.customview.R

/**
 * QQ导航随手指移动
 */
class QQNaviView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0) : FrameLayout(mContext, attrs, defStyleAttr) {

    /* 主view */
    private var mView: View? = null

    /* 外层icon/拖动幅度较小icon */
    private var mBigIcon: ImageView? = null

    /* 里层icon/拖动幅度较大icon */
    private var mSmallIcon: ImageView? = null

    /* 外层icon资源 */
    private val mBigIconSrc: Int
    private val mBigIconSrcCheck: Int
    /* 里面icon资源 */
    private val mSmallIconSrc: Int
    private val mSmallIconSrcCheck: Int
    /* icon高度宽度 */
    private var mIconWidth: Float = 0.toFloat()
    private var mIconHeight: Float = 0.toFloat()


    /* 拖动幅度较大半径 */
    private var mBigRadius: Float = 0.toFloat()

    /* 拖动幅度小半径 */
    private var mSmallRadius: Float = 0.toFloat()


    private var mRange: Float = 0.toFloat()

    private var lastX: Float = 0.toFloat()

    private var lastY: Float = 0.toFloat()

    init {

        val ta = mContext.obtainStyledAttributes(attrs, R.styleable.QQNaviView, defStyleAttr, 0)
        mBigIconSrc = ta.getResourceId(R.styleable.QQNaviView_bigIconSrc, R.drawable._002_bubble_big)
        mBigIconSrcCheck = ta.getResourceId(R.styleable.QQNaviView_bigIconSrcCheck, R.drawable._002_pre_bubble_big)
        mSmallIconSrc = ta.getResourceId(R.styleable.QQNaviView_smallIconSrc, 0)
        mSmallIconSrcCheck = ta.getResourceId(R.styleable.QQNaviView_smallIconSrcCheck, 0)
        mIconWidth = ta.getDimension(R.styleable.QQNaviView_iconWidth, dp2px(mContext, 40f).toFloat())
        mIconHeight = ta.getDimension(R.styleable.QQNaviView_iconHeight, dp2px(mContext, 40f).toFloat())


        mRange = ta.getFloat(R.styleable.QQNaviView_range, 1f)
        ta.recycle()


        init(mContext)
    }

    private fun init(context: Context) {
        mView = View.inflate(context, R.layout.view_002_icon, null)
        mBigIcon = mView!!.findViewById<View>(R.id.iv_big) as ImageView
        mSmallIcon = mView!!.findViewById<View>(R.id.iv_small) as ImageView

        mBigIcon!!.setImageResource(mBigIconSrc)
        mSmallIcon!!.setImageResource(mSmallIconSrc)

        setWidthAndHeight(mBigIcon)
        setWidthAndHeight(mSmallIcon)

        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER_HORIZONTAL
        mView!!.layoutParams = lp
        addView(mView)
    }

    /**
     * 设置icon宽高
     *
     * @param view view
     */
    private fun setWidthAndHeight(view: View?) {
        val lp = view!!.layoutParams as FrameLayout.LayoutParams
        lp.width = mIconWidth.toInt()
        lp.height = mIconHeight.toInt()
        view.layoutParams = lp
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setupView()
        measureDimension(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * 确定view以及拖动相关参数
     */
    private fun setupView() {
        //根据view的宽高确定可拖动半径的大小
        mSmallRadius = 0.1f * Math.min(mView!!.measuredWidth, mView!!.measuredHeight).toFloat() * mRange
        mBigRadius = 1.5f * mSmallRadius

        //设置imageview的padding，不然拖动时图片边缘部分会消失
        val padding = mBigRadius.toInt()
        mBigIcon!!.setPadding(padding, padding, padding, padding)
        mSmallIcon!!.setPadding(padding, padding, padding, padding)
    }

    private fun measureDimension(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val modeWidth = View.MeasureSpec.getMode(widthMeasureSpec)
        val modeHeight = View.MeasureSpec.getMode(heightMeasureSpec)
        var width = 0
        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
                val lp = child.layoutParams as FrameLayout.LayoutParams
                val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
                val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
                width += childWidth
                height += childHeight
            }
        }
        setMeasuredDimension(if (modeWidth == View.MeasureSpec.EXACTLY) sizeWidth else width,
            if (modeHeight == View.MeasureSpec.EXACTLY) sizeHeight else height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var childLeft: Int
        var childTop = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as FrameLayout.LayoutParams
            if (child.visibility != View.GONE) {
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight
                //水平居中显示
                childLeft = (width - childWidth) / 2
                //当前子view的top
                childTop += lp.topMargin
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
                //下一个view的top是当前子view的top + height + bottomMargin
                childTop += childHeight + lp.bottomMargin
            }
        }
    }

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

                moveEvent(mBigIcon, deltaX, deltaY, mSmallRadius)
                //因为可拖动大半径是小半径的1.5倍， 因此这里x,y也相应乘1.5
                moveEvent(mSmallIcon, 1.5f * deltaX, 1.5f * deltaY, mBigRadius)
            }
            MotionEvent.ACTION_UP -> {
                //抬起时复位
                mBigIcon!!.x = 0f
                mBigIcon!!.y = 0f
                mSmallIcon!!.x = 0f
                mSmallIcon!!.y = 0f
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 拖动事件
     *
     * @param view   view
     * @param deltaX 轴距离
     * @param deltaY Y轴距离
     * @param radius 半径
     */
    private fun moveEvent(view: View?, deltaX: Float, deltaY: Float, radius: Float) {

        //先计算拖动距离
        val distance = getDistance(deltaX, deltaY)

        //拖动的方位角，atan2出来的角度是带正负号的
        val degree = Math.atan2(deltaY.toDouble(), deltaX.toDouble())

        //如果大于临界半径就不能再往外拖了
        if (distance > radius) {
            view!!.x = view.left + (radius * Math.cos(degree)).toFloat()
            view.y = view.top + (radius * Math.sin(degree)).toFloat()
        } else {
            view!!.x = view.left + deltaX
            view.y = view.top + deltaY
        }

    }

    private fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics).toInt()
    }

    private fun getDistance(x: Float, y: Float): Float {
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    //    public void setBigIcon(int res) {
    //        mBigIcon.setImageResource(res);
    //    }
    //
    //    public void setSmallIcon(int res) {
    //        mSmallIcon.setImageResource(res);
    //    }

    fun setIconWidthAndHeight(width: Float, height: Float) {
        mIconWidth = dp2px(mContext, width).toFloat()
        mIconHeight = dp2px(mContext, height).toFloat()
        setWidthAndHeight(mBigIcon)
        setWidthAndHeight(mSmallIcon)
    }

    fun setRange(range: Float) {
        mRange = range
    }

    fun setChecked(checked: Boolean) {
        mBigIcon!!.setImageResource(if (checked) mBigIconSrcCheck else mBigIconSrc)

        mSmallIcon!!.setImageResource(if (checked) mSmallIconSrcCheck else mSmallIconSrc)
    }
}