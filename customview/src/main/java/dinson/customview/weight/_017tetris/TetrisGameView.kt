package dinson.customview.weight._017tetris

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import dinson.customview.R
import dinson.customview.kotlin.debug
import dinson.customview.kotlin.dip
import dinson.customview.kotlin.then
import dinson.customview.utils.DateUtils

/**
 * 俄罗斯方块
 */
class TetrisGameView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : SurfaceHolder.Callback, Runnable, SurfaceView(context, attrs, defStyleAttr) {

    companion object {
        private const val FPS = 30  //游戏帧数
    }

    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/

    /**向左移动*/
    fun moveLeft() {
        mBoxes.forEach { it.x -= 1 }
    }

    /**向右移动*/
    fun moveRight() {
        mBoxes.forEach { it.x += 1 }
    }

    /**向上移动*/
    fun moveUp() {
        mBoxes.forEach { it.y -= 1 }
    }

    /**向下移动*/
    fun moveDown() {
        mBoxes.forEach { it.y += 1 }
    }

    /**旋转*/
    fun rotate() {
        mBoxes.forEach {
            val checkX = -it.y + mBoxes[0].y + mBoxes[0].x
            val checkY = it.x - mBoxes[0].x + mBoxes[0].y
            it.x = checkX
            it.y = checkY
        }
    }


    /******************************************************************************************************/
    /**                             内部实现                                                               **/
    /******************************************************************************************************/

    private var mIsDrawing = false
    private var mCanvas: Canvas? = null
    private var mBoxSize = 0
    private val mMaps = Array(10) { BooleanArray(20) }
    private var mBoxes = arrayOf(Point(1, 0), Point(0, 0), Point(2, 0), Point(3, 0))

    private val mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = dip(1.5f).toFloat()
    }
    private val mBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }

    init {
        setZOrderOnTop(true)    // 这句不能少
        holder.setFormat(PixelFormat.TRANSPARENT)
        holder.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true
        setBackgroundResource(R.color.yellow)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)   //获取宽的模式
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)   //获取宽的尺寸

        //如果match_parent或者具体的值，直接赋值
        //如果是wrap_content，我们要得到控件需要多大的尺寸
        val width = if (widthMode == View.MeasureSpec.EXACTLY) widthSize else dip(200)
        val height = width.toFloat() / mMaps.size * mMaps[0].size
        //保存测量宽度和测量高度
        setMeasuredDimension(width, height.toInt())
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mIsDrawing = true
        mBoxSize = width / mMaps.size
        Thread(this).start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mIsDrawing = false
    }

    /**
     * 绘制游戏画面
     */
    private fun onGameViewDraw(canvas: Canvas) {

        debug("游戏绘制${DateUtils.long2Str(mStartTime)}")

        mBoxes.forEach {
            val x = it.x * mBoxSize.toFloat()
            val y = it.y * mBoxSize.toFloat()
            canvas.drawRect(x, y, x + mBoxSize, y + mBoxSize, mBoxPaint)
        }

        (0..mMaps[0].size).forEach {
            val x = mBoxSize * it.toFloat()
            canvas.drawLine(x, 0f, x, height.toFloat(), mLinePaint)
        }
        (0..mMaps[1].size).forEach {
            val y = mBoxSize * it.toFloat()
            canvas.drawLine(0f, y, width.toFloat(), y, mLinePaint)
        }
    }

    /**
     * 边界判断
     * @return true出界 false未出界
     */
    private fun checkBoundCanMove(x: Int, y: Int) = x < 0 || y < 0 || x >= mMaps.size || y >= mMaps[0].size

    /**
     * 移动算法
     * @param x x方向的偏移量
     * @param y y方向的偏移量
     */
    private fun move(x: Int, y: Int): Boolean {
        mBoxes.forEach { if (checkBoundCanMove(it.x + x, it.y + y)) return false }
        mBoxes.forEach { it.x += x;it.y += y }
        return true
    }

    private var mStartTime = 0L
    override fun run() {
        while (mIsDrawing) {
            try {
                mCanvas = holder.lockCanvas()
                mStartTime = System.currentTimeMillis()
                mCanvas!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                onGameViewDraw(mCanvas!!)
                holder.unlockCanvasAndPost(mCanvas)
                val endTime = System.currentTimeMillis()
                if (endTime - mStartTime < 1000 / FPS)
                    Thread.sleep(endTime - mStartTime)
                mStartTime = endTime
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {

            }
        }
    }
}