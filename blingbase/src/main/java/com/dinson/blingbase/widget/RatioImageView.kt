package com.dinson.blingbase.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.dinson.blingbase.R

/**
 * 自带比例的ImageView
 */
class RatioImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private var mRatio: Float
    private var mOriginalWidth = -1
    private var mOriginalHeight = -1

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView)
        mRatio = attributes.getFloat(R.styleable.RatioImageView_rivImgRatio, 1f)
        attributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mOriginalWidth > 0 && mOriginalHeight > 0) {
            val ratio = mOriginalWidth.toFloat() / mOriginalHeight
            var width = MeasureSpec.getSize(widthMeasureSpec)
            var height = MeasureSpec.getSize(heightMeasureSpec)
            if (width > 0) {
                height = (width.toFloat() / ratio).toInt()
            } else if (height > 0) {
                width = (height.toFloat() * ratio).toInt()
            }
            setMeasuredDimension(width, height)
        } else {
            val widthSize = MeasureSpec.getSize(widthMeasureSpec)
            setMeasuredDimension(widthSize, (widthSize * mRatio).toInt())
        }
    }

    /**
     * 设置比例
     * @param ratio 宽高比
     */
    fun setRatio(ratio: Float) {
        mRatio = ratio
        requestLayout()
    }

    /**
     * 设置原始尺寸比例
     * @param originalHeight 原始尺寸高度
     * @param originalWidth 原始尺寸宽度
     */
    fun setOriginalSize(originalWidth: Int, originalHeight: Int) {
        mOriginalWidth = originalWidth
        mOriginalHeight = originalHeight
    }
}