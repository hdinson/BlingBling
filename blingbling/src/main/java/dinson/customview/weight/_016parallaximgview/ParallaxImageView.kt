package dinson.customview.weight._016parallaximgview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.dinson.blingbase.kotlin.dip

/**
 *  带有视差的图片层
 */
class ParallaxImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr) {


    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/

    fun setImageList(resourceId: ArrayList<Int>) {
        if (resourceId.isEmpty()) return
        val removeAt = resourceId.removeAt(0)
        mResourceList = resourceId
        setImageResource(removeAt) //取第一张图片作为背景，触发sizeChange
    }

    /**
     * 设置滑动监听
     */
    fun setOnPanoramaScrollListener(listener: OnParallaxScrollListener) {
        mOnParallaxScrollListener = listener
    }

    /**
     * 设置当前
     */
    fun setGyroscopeObserver(observer: GyroscopeObserver) {
        observer.addPanoramaImageView(this)
    }

    /**
     * 获取当前是否有滚动条
     */
    fun isShowScrollbar(): Boolean = mSetting.isShowScrollbar

    /**
     * 设置是否显示滚动条
     */
    fun setShowScrollbar(enable: Boolean) {
        if (mSetting.isShowScrollbar == enable) return
        mSetting.isShowScrollbar = enable

        if (enable) initScrollbarPaint()
        else mScrollbarPaint = null
    }

    /**
     * 获取当前的滚动方向
     */
    fun getOrientation(): ParallaxScrollOrientation = mScrollOrientation

    /**
     * 获取当前是否开启滚动模式
     */
    fun isPanoramaModeEnabled(): Boolean = mSetting.isEnableParallaxMode

    /**
     * 设置是否支持滚动模式
     */
    fun setEnablePanoramaMode(enable: Boolean) {
        mSetting.isEnableParallaxMode = enable
    }

    /**
     * 获取当前是否反向滚动
     */
    fun isInvertScrollDirection(): Boolean = mSetting.isInvertScrollDirection

    /**
     * 设置当前是否反向滚动
     */
    fun setInvertScrollDirection(invert: Boolean) {
        mSetting.isInvertScrollDirection = invert
    }

    fun clearBitmap() {
        mBitmapList.forEach { it.recycle() }
        mBitmapList.clear()
    }


    /******************************************************************************************************/
    /**                             内部实现                                                               **/
    /******************************************************************************************************/

    private val mSetting = ParallaxImageViewSettings(context, attrs)
    private var mOnParallaxScrollListener: OnParallaxScrollListener? = null
    private var mScrollOrientation = ParallaxScrollOrientation.HORIZONTAL //滚动的方向，默认为水平滚动
    private var mScrollbarPaint: Paint? = null      //滚动条画笔
    private var mProgress = 0f                      //记录当前的进度
    private var mCurrentOffsetX = 0                 //记录当前的滚动的偏移量
    private var mMaxOffset = 0f                     //在图像不变形的前提下，溢出的偏移值的二分之一，另外二分之一做为视差距离
    private var mBitmapList: ArrayList<Bitmap> = arrayListOf()      //存储需要绘制的图像
    private var mResourceList: ArrayList<Int> = arrayListOf()    //存储需要绘制的图像的资源id
    private var mDrawableWidth = 0
    private var mDrawableHeight = 0                 //背景图片的真实宽高

    init {
        scaleType = ImageView.ScaleType.CENTER_CROP
        //初始化滚动条
        if (mSetting.isShowScrollbar) initScrollbarPaint()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (drawable == null) return

        mDrawableWidth = drawable.intrinsicWidth
        mDrawableHeight = drawable.intrinsicHeight

        val mMatrix = Matrix()      //缩放原图
        if (mDrawableWidth * h > mDrawableHeight * w) {
            mScrollOrientation = ParallaxScrollOrientation.HORIZONTAL
            val imgScale = h.toFloat() / mDrawableHeight.toFloat()
            mMaxOffset = Math.abs((mDrawableWidth * imgScale - w) * 0.25f)
            mMatrix.postScale(imgScale, imgScale)
        } else if (mDrawableWidth * h < mDrawableHeight * w) {
            mScrollOrientation = ParallaxScrollOrientation.VERTICAL
            val imgScale = w.toFloat() / mDrawableWidth.toFloat()
            mMaxOffset = Math.abs((mDrawableHeight * imgScale - h) * 0.5f)
            mMatrix.postScale(imgScale, imgScale)
        }

        mResourceList.forEach {
            val element = BitmapFactory.decodeResource(resources, it)
            mBitmapList.add(Bitmap.createBitmap(element, 0, 0,
                element.width, element.height, mMatrix, true))
            element.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        //不支持滚动模式，直接画背景
        if (!mSetting.isEnableParallaxMode || drawable == null || isInEditMode) {
            super.onDraw(canvas)
            return
        }

        // Draw image
        when (mScrollOrientation) {
            ParallaxScrollOrientation.HORIZONTAL -> {
                val currentOffsetX = mMaxOffset * mProgress
                canvas.save()
                canvas.translate(-currentOffsetX, 0f)
                super.onDraw(canvas)
                canvas.restore()
                canvas.save()
                val dx = mMaxOffset /  mBitmapList.size
                mBitmapList.forEachIndexed { index, bitmap ->
                    canvas.drawBitmap(bitmap, -(mMaxOffset + (index+1) * dx) * (mProgress + 1), 0f, null)
                }
                canvas.restore()
            }
            ParallaxScrollOrientation.VERTICAL -> {
                val currentOffsetY = mMaxOffset * mProgress
                canvas.save()
                canvas.translate(0f, currentOffsetY)
                super.onDraw(canvas)
                canvas.restore()
            }
        }
        // Draw scrollbar
        drawScrollBar(canvas)
    }


    fun updateProgress(progress: Float) {
        if (!mSetting.isEnableParallaxMode) return
        mProgress = if (mSetting.isInvertScrollDirection) progress else -progress

        val offsetX = (mMaxOffset * mProgress).toInt()
        if (offsetX != mCurrentOffsetX) {
            mCurrentOffsetX = offsetX
            invalidate()
            mOnParallaxScrollListener?.onScrolled(this, mProgress)
        }
    }

    /**
     * 初始化画笔
     */
    private fun initScrollbarPaint() {
        mScrollbarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mScrollbarPaint!!.color = Color.WHITE
        mScrollbarPaint!!.strokeWidth = dip(1.5f).toFloat()
    }

    private fun drawScrollBar(canvas: Canvas) {
        if (!mSetting.isShowScrollbar || mScrollbarPaint == null) return

        when (mScrollOrientation) {
            ParallaxScrollOrientation.HORIZONTAL -> {
                val barBgWidth = width * 0.9f
                val barWidth = barBgWidth * width / mDrawableWidth

                val barBgStartX = (width - barBgWidth) / 2
                val barBgEndX = barBgStartX + barBgWidth
                val barStartX = barBgStartX + (barBgWidth - barWidth) / 2 * ( 1+ mProgress )
                val barEndX = barStartX + barWidth
                val barY = height * 0.95f

                mScrollbarPaint!!.alpha = 100
                canvas.drawLine(barBgStartX, barY, barBgEndX, barY, mScrollbarPaint)
                mScrollbarPaint!!.alpha = 255
                canvas.drawLine(barStartX, barY, barEndX, barY, mScrollbarPaint)
            }
            ParallaxScrollOrientation.VERTICAL -> {
                val barBgHeight = height * 0.9f
                val barHeight = barBgHeight * height / mDrawableHeight

                val barBgStartY = (height - barBgHeight) / 2
                val barBgEndY = barBgStartY + barBgHeight
                val barStartY = barBgStartY + (barBgHeight - barHeight) / 2 * (mProgress+1)
                val barEndY = barStartY + barHeight
                val barX = width * 0.95f

                mScrollbarPaint!!.alpha = 100
                canvas.drawLine(barX, barBgStartY, barX, barBgEndY, mScrollbarPaint)
                mScrollbarPaint!!.alpha = 255
                canvas.drawLine(barX, barStartY, barX, barEndY, mScrollbarPaint)
            }
        }

        /* override fun setScaleType(type: ImageView.ScaleType){

         }*/
    }

    /**
     * Do nothing because PanoramaImageView only
     * supports  scaleType.CENTER_CROP
     */
    override fun setScaleType(scaleType: ScaleType?) {
        super.setScaleType(ImageView.ScaleType.CENTER_CROP)
    }
}