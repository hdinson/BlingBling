package com.dinson.blingbase.widget.catpcha

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar
import com.dinson.blingbase.kotlin.dip

internal class TextSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatSeekBar(context, attrs, defStyleAttr) {

    private var textPaint: Paint = Paint()

    init {
        textPaint.textAlign = Paint.Align.CENTER
        val textSize = dip(14f)
        textPaint.textSize = textSize.toFloat()
        textPaint.isAntiAlias = true
        textPaint.color = Color.parseColor("#333333")
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val fontMetrics = textPaint.fontMetrics
        val top = fontMetrics.top //为基线到字体上边框的距离,即上图中的top
        val bottom = fontMetrics.bottom //为基线到字体下边框的距离,即上图中的bottom
        val baseLineY = (height / 2 - top / 2 - bottom / 2).toInt() //基线中间点的y轴计算公式
        canvas.drawText("向右滑动滑块完成拼图", width / 2.toFloat(), baseLineY.toFloat(), textPaint)
        thumb.draw(canvas)
    }
}