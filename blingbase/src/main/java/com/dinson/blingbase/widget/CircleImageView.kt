package com.dinson.blingbase.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import com.dinson.blingbase.R

/**
 * 圆形头像
 */
@Suppress("unused")
class CircleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private var mWidth = 0f
    private var mHeight = 0f
    private val pressPaint: Paint = Paint()
    private val mSettings = CircleImageViewSettings(context, attrs)

    init {

        pressPaint.isAntiAlias = true
        pressPaint.style = Paint.Style.FILL
        pressPaint.color = mSettings.pressColor
        pressPaint.alpha = 0
        pressPaint.flags = Paint.ANTI_ALIAS_FLAG

        //isDrawingCacheEnabled = true
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        if (mSettings.shapeType == 0) {
            super.onDraw(canvas)
            return
        }
        val drawable = drawable ?: return
        if (width == 0 || height == 0) return
        val bitmap = getBitmapFromDrawable(drawable) ?: return

        drawDrawable(canvas, bitmap)

        if (isClickable) {
            drawPress(canvas)
        }
        drawBorder(canvas)

    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val width = drawable.intrinsicWidth.coerceAtLeast(2)
        val height = drawable.intrinsicHeight.coerceAtLeast(2)
        return try {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * draw Rounded Rectangle
     */
    private fun drawDrawable(canvas: Canvas, bitmap: Bitmap) {
        val paint = Paint()
        paint.color = -0x1
        paint.isAntiAlias = true
        val xMode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.saveLayer(0f, 0f, mWidth, mHeight, null)
        if (mSettings.shapeType == 1) {
            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - 1, paint)
        } else if (mSettings.shapeType == 2) {
            val rectF = RectF(1f, 1f, width - 1f, height - 1f)
            canvas.drawRoundRect(rectF, mSettings.radius + 1.toFloat(), mSettings.radius + 1.toFloat(), paint)
        }
        paint.xfermode = xMode
        val scaleWidth = width.toFloat() / bitmap.width
        val scaleHeight = height.toFloat() / bitmap.height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

        val bitmapTemp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        canvas.drawBitmap(bitmapTemp, 0f, 0f, paint)
        canvas.restore()
    }

    /**
     * draw the effect when pressed
     */
    private fun drawPress(canvas: Canvas) {
        if (mSettings.shapeType == 1) {
            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - 1, pressPaint)
        } else if (mSettings.shapeType == 2) {
            val rectF = RectF(1f, 1f, mWidth - 1f, mHeight - 1f)
            canvas.drawRoundRect(rectF, mSettings.radius + 1.toFloat(), mSettings.radius + 1.toFloat(), pressPaint)
        }
    }

    /**
     * draw customized border
     */
    private fun drawBorder(canvas: Canvas) {
        if (mSettings.borderWidth > 0) {
            val paint = Paint()
            paint.strokeWidth = mSettings.borderWidth.toFloat()
            paint.style = Paint.Style.STROKE
            paint.color = mSettings.borderColor
            paint.isAntiAlias = true
            // // check is rectangle or circle
            if (mSettings.shapeType == 1) {
                canvas.drawCircle(mWidth / 2, mHeight / 2, (mWidth - mSettings.borderWidth) / 2f, paint)
            } else if (mSettings.shapeType == 2) {
                val rectF = RectF(mSettings.borderWidth / 2f, mSettings.borderWidth / 2f, width - mSettings.borderWidth / 2f,
                    height - mSettings.borderWidth / 2f)
                canvas.drawRoundRect(rectF, mSettings.radius.toFloat(), mSettings.radius.toFloat(), paint)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = h.toFloat()
        mWidth = w.toFloat()
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                //performClick()
                pressPaint.alpha = (mSettings.pressAlpha * 255).toInt()
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                pressPaint.alpha = 0
                invalidate()
            }
            else -> {
                performClick()
            }
        }
        return super.onTouchEvent(event)
    }

    class CircleImageViewSettings internal constructor(context: Context, attrs: AttributeSet?) {
        var borderWidth = 0
        var borderColor = Color.parseColor("#ddffffff")
        var pressAlpha = 0f
        var pressColor = Color.parseColor("#00000000")
        var radius = 16
        var shapeType = 0

        init {
            val array = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = array.getColor(R.styleable.CircleImageView_civBorderColor, borderColor)
            borderWidth = array.getDimensionPixelOffset(R.styleable.CircleImageView_civBorderWidth, borderWidth)
            pressAlpha = array.getFloat(R.styleable.CircleImageView_civPressAlpha, pressAlpha)
            pressColor = array.getColor(R.styleable.CircleImageView_civPressColor, pressColor)
            radius = array.getDimensionPixelOffset(R.styleable.CircleImageView_civRadius, radius)
            shapeType = array.getInteger(R.styleable.CircleImageView_civShapeType, shapeType)
            array.recycle()
        }
    }


    /**
     * 设置边框颜色
     */
    fun setBorderColor(borderColor: Int) {
        mSettings.borderColor = borderColor
        invalidate()
    }

    /**
     * 设置边框宽度
     */
    fun setBorderWidth(borderWidth: Int) {
        mSettings.borderWidth = borderWidth
        invalidate()
    }

    /**
     * 设置点击时透明度
     *
     * @param pressAlpha 0f - 1f
     */
    fun setPressAlpha(pressAlpha: Float) {
        mSettings.pressAlpha = pressAlpha
        invalidate()
    }

    /**
     * 设置点击时颜色
     */
    fun setPressColor(pressColor: Int) {
        mSettings.pressColor = pressColor
        invalidate()
    }

    /**
     * 设置圆角
     */
    fun setRadius(radius: Int) {
        mSettings.radius = radius
        invalidate()
    }

    /**
     * 设置圆角类型
     * 1 圆形, 2 正方形
     */
    fun setShapeType(shapeType: Int) {
        mSettings.shapeType = shapeType
        invalidate()
    }
}