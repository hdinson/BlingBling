package dinson.customview.weight._017tetris

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import dinson.customview.kotlin.then
import java.util.*

/**
 * 方块模型
 */
class BoxModel(private val mapModel: MapModel,   val mBoxSize: Int) {

    private var mType = 0
    private val rowCount: Int = mapModel.maps.size
    private var mBoxPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = TetrisGameView.STROKE_WIDTH
    }

    lateinit var mBoxes: Array<Point>

    init {
        createBox()
    }

    /**
     * 移动算法
     * @param x x方向的偏移量
     * @param y y方向的偏移量
     */
    fun move(x: Int, y: Int): Boolean {
        mBoxes.forEach { if (checkBoundOutSide(it.x + x, it.y + y)) return false }
        mBoxes.forEach { it.x += x;it.y += y }
        return true
    }

    /**
     * 向下移动
     * @param isFast 是否快速下移
     */
    fun moveDown(isFast: Boolean): Boolean {
        if (isFast) {
            while (move(0, 1)) {
            }
        } else {
            if (move(0, 1)) return true
        }
        //堆积
        mBoxes.forEach { mapModel.maps[it.x][it.y] = true }
        //消行检测
        mapModel.deleteLine()
        //创建新的方块
        createBox()
        return false
    }

    /**旋转*/
    fun rotate() {
        if (mType == 0) return          //田不支持旋转
        var offsetX = 0
        var offsetY = 0
        mBoxes.forEach {
            val checkX = -it.y + mBoxes[0].y + mBoxes[0].x
            val checkY = it.x - mBoxes[0].x + mBoxes[0].y
            if (checkBoundOutSide(checkX, checkY)) {
                if (!checkBoundOutSide(checkX - 1, 0)) offsetX = -1
                else if (!checkBoundOutSide(checkX + 1, 0)) offsetX = 1
                else if (!checkBoundOutSide(checkX, checkY + 1)) offsetY = 1
                else return
            }
        }
        mBoxes.forEach {
            val checkX = -it.y + mBoxes[0].y + mBoxes[0].x
            val checkY = it.x - mBoxes[0].x + mBoxes[0].y
            it.x = checkX
            it.y = checkY
        }
        (offsetX != 0) then move(offsetX, 0)
        (offsetY != 0) then move(0, offsetY)
    }


    /**
     * 边界判断
     * @return true出界 false未出界
     */
    private fun checkBoundOutSide(x: Int, y: Int) = x < 0 || y < 0 || x >= mapModel.maps.size
        || y >= mapModel.maps[0].size || mapModel.maps[x][y]


    fun createBox() {
        val centerX = rowCount / 2
        mType = Random().nextInt(7)
        mBoxes = when (mType) {
            0 ->//田
                arrayOf(Point(centerX - 1, 0), Point(centerX, 0),
                    Point(centerX - 1, 1), Point(centerX, 1))
            1 ->//一
                arrayOf(Point(centerX - 1, 0), Point(centerX - 2, 0),
                    Point(centerX, 0), Point(centerX + 1, 0))
            2 ->//土
                arrayOf(Point(centerX, 1), Point(centerX, 0),
                    Point(centerX - 1, 1), Point(centerX + 1, 1))
            3 ->//L
                arrayOf(Point(centerX, 1), Point(centerX - 1, 0),
                    Point(centerX - 1, 1), Point(centerX + 1, 1))
            4 -> //反L
                arrayOf(Point(centerX, 1), Point(centerX + 1, 0),
                    Point(centerX - 1, 1), Point(centerX + 1, 1))
            5 ->//Z
                arrayOf(Point(centerX, 0), Point(centerX - 1, 0),
                    Point(centerX, 1), Point(centerX + 1, 1))
            else ->//反Z
                arrayOf(Point(centerX, 0), Point(centerX + 1, 0),
                    Point(centerX - 1, 1), Point(centerX, 1))
        }
    }

    /**
     * 画方块盒子
     */
    fun drawBox(canvas: Canvas) {
        //画当前下落的方块
        mBoxes.forEach {
            val x = it.x * mBoxSize.toFloat()
            val y = it.y * mBoxSize.toFloat()
            drawBox(canvas, x, y, mBoxPaint)
        }
    }

    /**
     * 画方块盒子
     */
  private  fun drawBox(canvas: Canvas, startX: Float, startY: Float, paint: Paint) {
        paint.style = Paint.Style.STROKE

        canvas.drawRect(startX + TetrisGameView.STROKE_WIDTH / 2, startY + TetrisGameView.STROKE_WIDTH / 2,
            startX + mBoxSize - TetrisGameView.STROKE_WIDTH, startY + mBoxSize - TetrisGameView.STROKE_WIDTH, paint)
        paint.style = Paint.Style.FILL
        val px = TetrisGameView.STROKE_WIDTH + TetrisGameView.STROKE_WIDTH / 2
        canvas.drawRect(startX + px, startY + px,
            startX + mBoxSize - px, startY + mBoxSize - px, paint)
    }

}
