package dinson.customview.widget._017tetris

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point

/**
 *  地图模型
 */
class MapModel {
    val maps = Array(10) { BooleanArray(20) }
    private var mBoxPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = TetrisGameView.STROKE_WIDTH
    }
    private val mMapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        alpha = 30
        strokeWidth = TetrisGameView.STROKE_WIDTH
    }


    /**
     * 消行
     */
    fun deleteLine() {
        var row = maps[0].size - 1
        while (row > 0) {
            if (checkDeleteLine(row)) {
                //执行消行
                for (i in row downTo 0) {
                    maps.forEachIndexed { index, _ ->
                        if (i == 0) {
                            maps[index][0] = false
                        } else {
                            maps[index][i] = maps[index][i - 1]
                        }
                    }
                }
                row++
            }
            row--
        }
    }

    /**
     * 消行检测
     * @param row 检测的当前行
     * @return true,当前行可以消行，false当前行不能消行
     */
    private fun checkDeleteLine(row: Int): Boolean {
        /* for (i in mMaps[0].size - 1 downTo 0) {
             mMaps.forEachIndexed { index, _ ->
                 mMaps[index][i] = true
             }
             Thread.sleep(50)
         }*/
        maps.forEachIndexed { index, _ ->
            if (!maps[index][row]) return false
        }
        return true
    }

    /**
     * 检查是否游戏结束
     * @return true 游戏结束，false 游戏未结束
     */
    fun checkOver(currentBox: Array<Point>): Boolean {
        currentBox.forEach {
            if (maps[it.x][it.y]) return true
        }
        return false
    }

    fun reFreshUp() {
        for (i in maps[0].size - 1 downTo 0) {
            maps.forEachIndexed { index, _ ->
                maps[index][i] = true
            }
            Thread.sleep(50)
        }
    }

    fun reFreshDown() {
        for (i in 0 until maps[0].size) {
            maps.forEachIndexed { index, _ ->
                maps[index][i] = false
            }
            Thread.sleep(50)
        }
    }


    /**
     * 画方块盒子
     */
    fun drawMap(canvas: Canvas, boxSize: Int) {

        //画地图和堆积
        maps.forEachIndexed { outerIndex, _ ->
            maps[outerIndex].forEachIndexed({ interIndex, b ->
                val x = outerIndex * boxSize.toFloat()
                val y = interIndex * boxSize.toFloat()
                drawBox(canvas, x, y, if (b) mBoxPaint else mMapPaint, boxSize)
            })
        }
    }

    /**
     * 画方块盒子
     */
    private fun drawBox(canvas: Canvas, startX: Float, startY: Float, paint: Paint, boxSize: Int) {
        paint.style = Paint.Style.STROKE

        canvas.drawRect(startX + TetrisGameView.STROKE_WIDTH / 2, startY + TetrisGameView.STROKE_WIDTH / 2,
            startX + boxSize - TetrisGameView.STROKE_WIDTH, startY + boxSize - TetrisGameView.STROKE_WIDTH, paint)
        paint.style = Paint.Style.FILL
        val px = TetrisGameView.STROKE_WIDTH + TetrisGameView.STROKE_WIDTH / 2
        canvas.drawRect(startX + px, startY + px,
            startX + boxSize - px, startY + boxSize - px, paint)
    }

}