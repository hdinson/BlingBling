package dinson.customview.weight._014honorclock

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import dinson.customview.utils.TypefaceUtils
import java.util.*

/**
 * 仿华为时钟
 */
class HonorClockView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private val mSettings = HonorClockViewSettings(context, attrs)
    private val mXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    private val mMaskWidth = 20f      //遮罩层的宽度
    private var mClockAnimator: ValueAnimator? = null   //时钟的动画
    //时钟文字的起始点坐标
    private var mDigitalTimeTextStartX = 0f
    private var mDigitalTimeTextStartY = 0f

    private lateinit var mBitmap: Bitmap //外环的背景
    private lateinit var mClockMaskPath: Path  //外环的背景

    //外环的画笔
    private val mPaint by lazy {
        Paint().apply {
            color = mSettings.timeScaleColor
            strokeWidth = 4f
            isAntiAlias = true // 抗锯齿
            isDither = true // 防抖动
            textSize = mSettings.timeTextSize
            typeface = TypefaceUtils.getAppleFont(getContext())
            strokeCap = Paint.Cap.ROUND//线帽
        }
    }

    init {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //将绘制操作保存到新的图层，因为图像合成是很昂贵的操作，将用到硬件加速，这里将图像合成的处理放到离屏缓存中进行
        val saveCount = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), mPaint)
        //背景不为空时，画外环背景
        canvas.drawBitmap(mBitmap, 0f, 0f, mPaint)
        //设置混合模式
        mPaint.xfermode = mXfermode
        canvas.rotate(mNowClockAngle, measuredWidth / 2f, measuredWidth / 2f)
        //绘制源图
        canvas.drawPath(mClockMaskPath, mPaint)
        //清除混合模式
        mPaint.xfermode = null
        mPaint.color = mSettings.pointColor
        canvas.drawCircle(width / 2f, mMaskWidth + mSettings.ringWidth + 25, 14f, mPaint)
        //还原画布
        canvas.restoreToCount(saveCount)
        updateTimeText(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)   //获取宽的模式
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)   //获取宽的尺寸

        //如果match_parent或者具体的值，直接赋值
        //如果是wrap_content，我们要得到控件需要多大的尺寸
        val width = if (widthMode == View.MeasureSpec.EXACTLY) widthSize else mSettings.viewWidth
        //保存测量宽度和测量高度一致
        setMeasuredDimension(width, width)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        val centerPoint = width / 2f        //中心点坐标
        //构建背景
        mBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        val mBitmapCanvas = Canvas(mBitmap)
        for (i in 0 until 120) {
            mBitmapCanvas.drawLine(centerPoint, 0f, centerPoint, mSettings.ringWidth + mMaskWidth, mPaint)
            mBitmapCanvas.rotate(3f, centerPoint, centerPoint)
        }
        //构建遮罩层路径
        val spacing = 80.0//跨度
        mClockMaskPath = Path().apply {
            val tangle = Math.toDegrees(Math.atan((spacing / centerPoint)))
            val rectF = RectF(mMaskWidth, mMaskWidth, width - mMaskWidth, width - mMaskWidth)
            arcTo(rectF, 270 + tangle.toFloat(), 360 - tangle.toFloat() * 2, false)
            val coords = floatArrayOf(0f, 0f)
            val pathMeasure = PathMeasure(this, false)
            pathMeasure.getPosTan(0f, coords, null)
            quadTo(centerPoint, -mMaskWidth, coords[0], coords[1])
            fillType = Path.FillType.INVERSE_EVEN_ODD       //反转
        }
        //测量TextView的起始点坐标
        val textRect = Rect()
        mPaint.getTextBounds("00:00", 0, "00:00".length, textRect)
        mDigitalTimeTextStartX = centerPoint - textRect.width() / 2
        mDigitalTimeTextStartY = centerPoint + textRect.height() / 2
    }

    private var mNowClockAngle = 0f

    fun performAnimation() {
        cancelAnimation()
        mClockAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 60000
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
            addUpdateListener {
                val value = it.animatedValue as Float
                mNowClockAngle = value + mInitStartAngle
                invalidate()
            }
        }
        mClockAnimator!!.start()
    }

    /**
     * 更新文字
     */
    private var mLastTimeMillis = 0L
    private var mLastDigitalTimeStr = ""
    private var mInitStartAngle = -1
    private fun updateTimeText(canvas: Canvas) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - mLastTimeMillis >= 1000) {
            Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                //当前分钟的开始时间戳
                mLastTimeMillis = timeInMillis - get(Calendar.SECOND) * 1000 - get(Calendar.MILLISECOND)
                mLastDigitalTimeStr = String.format("%02d:%02d", get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE))
                if (mInitStartAngle < 0) mInitStartAngle = get(Calendar.SECOND) * 6
            }
        }
        mPaint.color = mSettings.timeTextColor
        canvas.drawText(mLastDigitalTimeStr, mDigitalTimeTextStartX, mDigitalTimeTextStartY, mPaint)
    }

    fun cancelAnimation() {
        mClockAnimator?.let {
            it.removeAllUpdateListeners()
            it.removeAllListeners()
            it.cancel()
        }
        mInitStartAngle=-1
        mClockAnimator = null
    }
}
