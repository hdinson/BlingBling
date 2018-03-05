package dinson.customview.weight._017tetris

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import dinson.customview.kotlin.debug
import dinson.customview.kotlin.dip
import dinson.customview.kotlin.info
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 俄罗斯方块
 */
class TetrisGameView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : SurfaceHolder.Callback, Runnable, SurfaceView(context, attrs, defStyleAttr) {

    companion object {
        private const val FPS = 30  //游戏帧数
        val STROKE_WIDTH = dip(2).toFloat()  //游戏帧数
    }

    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/

    /**向左移动*/
    fun moveLeft() {
        mBoxModel.move(-1, 0)
    }

    /**向右移动*/
    fun moveRight() {
        mBoxModel.move(1, 0)
    }


    /**向下移动*/
    fun smoothMoveDown() {
        if (!mBoxModel.moveDown(false)) {
            if (mMapModel.checkOver(mBoxModel.mBoxes)) reSetGame()
        }
    }

    /**向下移动*/
    fun fastMoveDown() {
        if (!mBoxModel.moveDown(true)) {
            if (mMapModel.checkOver(mBoxModel.mBoxes)) reSetGame()
        }
    }

    /**旋转*/
    fun rotate() {
        mBoxModel.rotate()
    }


    /**
     * 开始游戏
     */
    fun startGame() {
        mIsDrawing = true
        Thread(this).start()
        mMoveDownInterval = Observable.interval(500, TimeUnit.MILLISECONDS).subscribe {
            mBoxModel.moveDown(false)
        }
    }

    /**
     * 暂停游戏
     */
    fun pauseGame() {
        mIsDrawing = false
        mMoveDownInterval.dispose()
    }

    /**
     * 重启游戏
     */
    fun reSetGame() {
        mMoveDownInterval.dispose()
        Observable.timer(100, TimeUnit.MILLISECONDS).subscribe {

            mMapModel.reFreshUp()
            mBoxModel.createBox()
            mMapModel.reFreshDown()

            mMoveDownInterval = Observable.interval(500, TimeUnit.MILLISECONDS).subscribe {
                mBoxModel.moveDown(false)
            }
        }
    }

    /******************************************************************************************************/
    /**                             内部实现                                                              **/
    /******************************************************************************************************/

    private var mIsDrawing = false

    private   val mMapModel= MapModel()
    private lateinit var mBoxModel: BoxModel


    private lateinit var mMoveDownInterval: Disposable


    init {
        setZOrderOnTop(true)    // 这句不能少
        holder.setFormat(PixelFormat.TRANSPARENT)
        holder.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)   //获取宽的模式
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)   //获取宽的尺寸

        //如果match_parent或者具体的值，直接赋值
        //如果是wrap_content，我们要得到控件需要多大的尺寸
        val width = if (widthMode == View.MeasureSpec.EXACTLY) widthSize / mMapModel.maps.size * mMapModel.maps.size
        else dip(200) / mMapModel.maps.size * mMapModel.maps.size
        val height = width.toFloat() / mMapModel.maps.size * mMapModel.maps[0].size

        //保存测量宽度和测量高度
        setMeasuredDimension(width, height.toInt())
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mIsDrawing = true
        val boxSize = width / mMapModel.maps.size
        mBoxModel = BoxModel(mMapModel, boxSize)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        info("surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mIsDrawing = false
        mMoveDownInterval.dispose()
        info("surfaceDestroyed")
    }

    /**
     * 绘制游戏画面
     */
    private fun onGameViewDraw(canvas: Canvas) {
        debug("onGameViewDraw")
        mMapModel.drawMap(canvas, mBoxModel.mBoxSize)
        mBoxModel.drawBox(canvas)
    }


    private var mStartTime = 0L
    override fun run() {
        while (mIsDrawing) {
            val canvas = holder.lockCanvas()
            mStartTime = System.currentTimeMillis()
            canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            try {
                onGameViewDraw(canvas)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                canvas?.let { holder.unlockCanvasAndPost(it) }
            }
            val endTime = System.currentTimeMillis()
            if (endTime - mStartTime < 1000 / FPS)
                Thread.sleep(endTime - mStartTime)
            mStartTime = endTime
        }
    }
}