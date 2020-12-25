package com.dinson.blingbase.widget.catpcha

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import java.util.*

class DefaultCaptchaStrategy : CaptchaStrategy() {

    override fun getBlockShape(blockSize: Int): Path {
        val path = Path()
        val gap = blockSize / 16f
        path.moveTo(0f, gap * 5)
        path.quadTo(0f, 3 * gap, 2 * gap, 3 * gap)
        path.rLineTo(2 * gap, 0f)
        path.rLineTo(0f, -gap)
        path.quadTo(4 * gap, 0f, 6 * gap, 0f)
        path.quadTo(8 * gap, 0f, 8 * gap, 2 * gap)
        path.rLineTo(0f, gap)
        path.rLineTo(3 * gap, 0f)
        path.quadTo(13 * gap, 3 * gap, 13 * gap, 5 * gap)
        path.rLineTo(0f, 2 * gap)
        path.rLineTo(gap, 0f)
        path.quadTo(16 * gap, 7 * gap, 16 * gap, 9 * gap)
        path.quadTo(16 * gap, 11 * gap, 14 * gap, 11 * gap)
        path.rLineTo(-gap, 0f)
        path.rLineTo(0f, 3 * gap)
        path.quadTo(13 * gap, 16 * gap, 11 * gap, 16 * gap)
        path.rLineTo(-3 * gap, 0f)
        path.rLineTo(0f, -gap)
        path.quadTo(8 * gap, 13 * gap, 6 * gap, 13 * gap)
        path.quadTo(4 * gap, 13 * gap, 4 * gap, 15 * gap)
        path.rLineTo(0f, gap)
        path.rLineTo(-2 * gap, 0f)
        path.quadTo(0f, 16 * gap, 0f, 14 * gap)
        path.close()
        return path
    }

    override fun getBlockPositionInfo(width: Int, height: Int, blockSize: Int): PositionInfo {
        val random = Random()
        var left = random.nextInt(width - blockSize + 1)
        if (left < blockSize) {
            left = blockSize
        }
        var top = random.nextInt(height - blockSize + 1)
        if (top < 0) {
            top = 0
        }
        return PositionInfo(left, top)
    }

    override fun getPositionInfoForSwipeBlock(
        width: Int,
        height: Int,
        blockSize: Int
    ): PositionInfo {
        val random = Random()
        val left = random.nextInt(width - blockSize + 1)
        var top = random.nextInt(height - blockSize + 1)
        if (top < 0) {
            top = 0
        }
        return PositionInfo(left, top)
    }

    override fun blockShadowPaint(): Paint {
        val shadowPaint = Paint()
        shadowPaint.color = Color.parseColor("#FFFFFF")
        return shadowPaint
    }

    override fun blockBitmapPaint(): Paint {
        return Paint()
    }

    override fun decorationSwipeBlockBitmap(canvas: Canvas, shape: Path) {
        val paint = Paint()
        paint.color = Color.parseColor("#FFFFFF")
        paint.alpha = 165
        paint.style = Paint.Style.FILL
        val path = Path(shape)
        canvas.drawPath(path, paint)
    }
}