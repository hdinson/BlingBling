package dinson.customview.weight._013stepview

import android.content.Context
import android.graphics.*
import android.view.View

/**
 * StepView的指示器
 */
class VerticalStepViewIndicator(context: Context,
                                private val mPos: Position,
                                private val mIndicatorState: State,
                                private val mPaddingTop: Int,//指示器距离top的距离
                                private val mSettings: StepViewSettings) : View(context) {


    private var mCenterX: Int = 0  //该View的X轴的中间位置

    private var mFullLinePathTop: Path? = null
    private var mFullLinePathBottom: Path? = null
    private var mDashedLinePathTop: Path? = null
    private var mDashedLinePathBottom: Path? = null

    private lateinit var mIndicatorRect: Rect

    private val mCompletedPaint by lazy {
        //完成paint
        Paint().apply {
            isAntiAlias = true
            color = mSettings.indicatorCompletedLineColor
            strokeWidth = 12f
            style = Paint.Style.FILL
        }
    }
    private val mUnCompletedPaint by lazy {
        //未完成Paint  definition mUnCompletedPaint
        Paint().apply {
            isAntiAlias = true
            color = mSettings.indicatorUnCompletedLineColor
            style = Paint.Style.STROKE
            strokeWidth = mSettings.indicatorLineWidth
            val intervals = mSettings.dashLineIntervals
            pathEffect = DashPathEffect(floatArrayOf(intervals, intervals, intervals, intervals), 0f)
        }
    }

    private fun getTopPaint(): Paint = when (mIndicatorState) {
        State.Completed -> mCompletedPaint
        State.UnCompleted -> mUnCompletedPaint
        State.Completing -> //倒序和非倒序的线要反过来
            if (mSettings.isReverseDraw) mUnCompletedPaint
            else mCompletedPaint

    }

    private fun getTopPath(): Path? = when (mIndicatorState) {
        State.Completed -> mFullLinePathTop
        State.UnCompleted -> mDashedLinePathTop
        State.Completing -> //倒序和非倒序的线要反过来
            if (mSettings.isReverseDraw) mDashedLinePathTop
            else mFullLinePathTop

    }

    private fun getBottomPaint(): Paint = when (mIndicatorState) {
        State.Completed -> mCompletedPaint
        State.UnCompleted -> mUnCompletedPaint
        State.Completing -> //倒序和非倒序的线要反过来
            if (mSettings.isReverseDraw) mCompletedPaint
            else mUnCompletedPaint
    }

    private fun getBottomPath(): Path? = when (mIndicatorState) {
        State.Completed -> mFullLinePathBottom
        State.UnCompleted -> mDashedLinePathBottom
        State.Completing -> //倒序和非倒序的线要反过来
            if (mSettings.isReverseDraw) mFullLinePathBottom
            else mDashedLinePathBottom
    }

    //图标的宽度
    private val mIconWidth by lazy {
        Math.min(mSettings.getIndicatorIcon(mIndicatorState).minimumWidth, mSettings.indicatorWidth.toInt())
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)   //获取宽的模式
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec) //获取高的模式
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)   //获取宽的尺寸
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec) //获取高的尺寸

        //如果match_parent或者具体的值，直接赋值
        //如果是wrap_content，我们要得到控件需要多大的尺寸
        val width = if (widthMode == View.MeasureSpec.EXACTLY) widthSize
        else (paddingLeft + mIconWidth + paddingRight)

        //高度跟宽度处理方式一样
        val height = if (heightMode == View.MeasureSpec.EXACTLY) heightSize
        else (paddingTop + height + paddingBottom)

        //保存测量宽度和测量高度
        setMeasuredDimension(width, height)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = (width / 2)

        val iconHeight = Math.min(mSettings.getIndicatorIcon(mIndicatorState).minimumHeight, mSettings.indicatorHeight.toInt())

        mIndicatorRect = Rect(mCenterX - mIconWidth / 2, mPaddingTop,
            mCenterX + mIconWidth / 2, mPaddingTop + iconHeight)

        //实线路径
        mFullLinePathTop = Path().apply {
            moveTo(mCenterX - mSettings.indicatorLineWidth / 2, 0f)
            rLineTo(mSettings.indicatorLineWidth, 0f)
            rLineTo(0f, mPaddingTop.toFloat())
            rLineTo(-mSettings.indicatorLineWidth, 0f)
            close()
            fillType = Path.FillType.WINDING
        }
        mFullLinePathBottom = Path().apply {
            moveTo(mCenterX - mSettings.indicatorLineWidth / 2, (iconHeight + mPaddingTop).toFloat())
            rLineTo(mSettings.indicatorLineWidth, 0f)
            rLineTo(0f, height - (iconHeight + mPaddingTop).toFloat())
            rLineTo(-mSettings.indicatorLineWidth, 0f)
            close()
        }
        //虚线路径
        mDashedLinePathTop = Path().apply {
            moveTo(mCenterX.toFloat()  , 0f)
            rLineTo(0f, mPaddingTop.toFloat())
        }
        //虚线路径
        mDashedLinePathBottom = Path().apply {
            moveTo(mCenterX .toFloat()  , (iconHeight + mPaddingTop).toFloat())
            rLineTo(0f, height - (iconHeight + mPaddingTop).toFloat())
        }

        //步骤起始点
        if (mPos == Position.Start) {
            if (mSettings.isReverseDraw) {
                //倒序
                mFullLinePathBottom = null
                mDashedLinePathBottom = null
            } else {
                //正序
                mFullLinePathTop = null
                mDashedLinePathTop = null
            }
        }
        //步骤终止点
        if (mPos == Position.End) {
            if (mSettings.isReverseDraw) {
                //倒序
                mDashedLinePathTop = null
                mFullLinePathTop = null
            } else {
                //正序
                mFullLinePathBottom = null
                mDashedLinePathBottom = null
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //-----------------------画图标----------------------------------------------------*/
        val indicatorIcon = mSettings.getIndicatorIcon(mIndicatorState)
        indicatorIcon.bounds = mIndicatorRect
        indicatorIcon.draw(canvas)
        //-----------------------画线------------------------------------------------------*/
        getTopPath()?.let { canvas.drawPath(it, getTopPaint()) }
        getBottomPath()?.let { canvas.drawPath(it, getBottomPaint()) }
    }
}