package com.dinson.blingbase.widget.catpcha

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.dinson.blingbase.widget.catpcha.DragCaptchaView.MODE
import kotlin.math.abs

class PictureVerifyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {


    private var mState = STATE_IDEL
    private var shadowInfo //缺块阴影的位置
            : PositionInfo? = null
    private var blockInfo //缺块图片的位置
            : PositionInfo? = null
    private var verfityBlock //缺块图片
            : Bitmap? = null
    private var blockShape //缺块形状
            : Path? = null
    private val bitmapPaint //绘制缺块图片的画笔
            : Paint
    private val shadowPaint //绘制缺块阴影的画笔
            : Paint
    private var startTouchTime: Long = 0
    private var looseTime: Long = 0
    private var blockSize = 50
    private var mTouchEnable = true //是否可触动
    private var callback: Callback? = null
    private var mStrategy: CaptchaStrategy
    private var mMode = 0

    interface Callback {
        fun onSuccess(time: Long)
        fun onFailed()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (shadowInfo == null) {
            shadowInfo = mStrategy.getBlockPositionInfo(width, height, blockSize)
            blockInfo = if (mMode == MODE.WITH_BAR) {
                PositionInfo(0, shadowInfo!!.top)
            } else {
                mStrategy.getPositionInfoForSwipeBlock(width, height, blockSize)
            }
        }
        if (blockShape == null) {
            blockShape = mStrategy.getBlockShape(blockSize)
            blockShape!!.offset(shadowInfo!!.left.toFloat(), shadowInfo!!.top.toFloat())
        }
        if (verfityBlock == null) {
            verfityBlock = createBlockBitmap()
        }
        if (mState != STATE_ACCESS) {
            canvas.drawPath(blockShape!!, shadowPaint)
        }
        if (mState == STATE_MOVE || mState == STATE_IDEL || mState == STATE_DOWN || mState == STATE_UNACCESS) {
            canvas.drawBitmap(
                verfityBlock!!,
                blockInfo!!.left.toFloat(),
                blockInfo!!.top.toFloat(),
                bitmapPaint
            )
        }
    }

    fun down(progress: Int) {
        startTouchTime = System.currentTimeMillis()
        mState = STATE_DOWN
        blockInfo!!.left = (progress / 100f * (width - blockSize)).toInt()
        invalidate()
    }

    fun downByTouch(x: Float, y: Float) {
        mState = STATE_DOWN
        blockInfo!!.left = (x - blockSize / 2f).toInt()
        blockInfo!!.top = (y - blockSize / 2f).toInt()
        startTouchTime = System.currentTimeMillis()
        invalidate()
    }

    fun move(progress: Int) {
        mState = STATE_MOVE
        blockInfo!!.left = (progress / 100f * (width - blockSize)).toInt()
        invalidate()
    }

    fun moveByTouch(offsetX: Float, offsetY: Float) {
        mState = STATE_MOVE
        blockInfo!!.left += offsetX.toInt()
        blockInfo!!.top += offsetY.toInt()
        invalidate()
    }

    fun loose() {
        mState = STATE_LOOSEN
        looseTime = System.currentTimeMillis()
        checkAccess()
        invalidate()
    }

    fun reset() {
        mState = STATE_IDEL
        verfityBlock = null
        shadowInfo = null
        blockShape = null
        invalidate()
    }

    fun unAccess() {
        mState = STATE_UNACCESS
        invalidate()
    }

    fun access() {
        mState = STATE_ACCESS
        invalidate()
    }

    fun callback(callback: Callback) {
        this.callback = callback
    }

    fun setCaptchaStrategy(strategy: CaptchaStrategy) {
        mStrategy = strategy
    }

    fun setBlockSize(size: Int) {
        blockSize = size
        blockShape = null
        blockInfo = null
        shadowInfo = null
        verfityBlock = null
        invalidate()
    }

    fun setBitmap(bitmap: Bitmap?) {
        blockShape = null
        blockInfo = null
        shadowInfo = null
        verfityBlock!!.recycle()
        verfityBlock = null
        setImageBitmap(bitmap)
    }

    fun setMode(@MODE mode: Int) {
        mMode = mode
        blockShape = null
        blockInfo = null
        shadowInfo = null
        verfityBlock = null
        invalidate()
    }

    fun setTouchEnable(enable: Boolean) {
        mTouchEnable = enable
    }

    private fun createBlockBitmap(): Bitmap? {
        val tempBitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(tempBitmap)
        drawable.setBounds(0, 0, width, height)
        canvas.clipPath(blockShape!!)
        drawable.draw(canvas)
        mStrategy.decorationSwipeBlockBitmap(canvas, blockShape!!)
        return cropBitmap(tempBitmap)
    }

    private fun cropBitmap(bmp: Bitmap): Bitmap? {
        var result: Bitmap? = null
        result = Bitmap.createBitmap(
            bmp,
            shadowInfo!!.left,
            shadowInfo!!.top,
            blockSize,
            blockSize
        )
        bmp.recycle()
        return result
    }

    /**
     * 检测是否通过
     */
    private fun checkAccess() {
        if (abs(blockInfo!!.left - shadowInfo!!.left) < TOLERANCE && abs(
                blockInfo!!.top - shadowInfo!!.top
            ) < TOLERANCE
        ) {
            access()
            if (callback != null) {
                val deltaTime = looseTime - startTouchTime
                callback!!.onSuccess(deltaTime)
            }
        } else {
            unAccess()
            if (callback != null) {
                callback!!.onFailed()
            }
        }
    }

    private var tempX = 0f
    private var tempY = 0f
    private var downX = 0f
    private var downY = 0f
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && mMode == MODE.WITHOUT_BAR) {
            if (event.x < blockInfo!!.left || event.x > blockInfo!!.left + blockSize || event.y < blockInfo!!.top || event.y > blockInfo!!.top + blockSize) {
                return false
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mMode == MODE.WITHOUT_BAR && verfityBlock != null && mTouchEnable) {
            val x = event.x
            val y = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = x
                    downY = y
                    downByTouch(x, y)
                }
                MotionEvent.ACTION_UP -> loose()
                MotionEvent.ACTION_MOVE -> {
                    val offsetX = x - tempX
                    val offsetY = y - tempY
                    moveByTouch(offsetX, offsetY)
                }
            }
            tempX = x
            tempY = y
        }
        return true
    }

    companion object {
        private const val STATE_DOWN = 1
        private const val STATE_MOVE = 2
        private const val STATE_LOOSEN = 3
        private const val STATE_IDEL = 4
        private const val STATE_ACCESS = 5
        private const val STATE_UNACCESS = 6
        private const val TOLERANCE = 10
    }

    init {
        mStrategy = DefaultCaptchaStrategy()
        shadowPaint = mStrategy.blockShadowPaint()
        bitmapPaint = mStrategy.blockBitmapPaint()
        setLayerType(View.LAYER_TYPE_SOFTWARE, bitmapPaint)
    }
}