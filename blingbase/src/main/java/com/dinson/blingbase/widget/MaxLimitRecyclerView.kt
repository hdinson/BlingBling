package com.dinson.blingbase.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.dinson.blingbase.R

class MaxLimitRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {


    private var mMaxHeight: Int
    private var mMaxWidth: Int

    init {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.MaxLimitRecyclerView)
        mMaxHeight = arr.getLayoutDimension(R.styleable.MaxLimitRecyclerView_mlMaxHeight, -1)
        mMaxWidth = arr.getLayoutDimension(R.styleable.MaxLimitRecyclerView_mlMaxWidth, -1)
        arr.recycle()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        var needLimit = false
        if (mMaxHeight >= 0 || mMaxWidth >= 0) {
            needLimit = true
        }
        if (needLimit) {
            var limitHeight = measuredHeight
            var limitWith = measuredWidth
            if (measuredHeight > mMaxHeight) {
                limitHeight = mMaxHeight
            }
            if (measuredWidth > mMaxWidth) {
                limitWith = mMaxWidth
            }
            setMeasuredDimension(limitWith, limitHeight)
        }
    }

    fun setMaxHeight(maxHeight: Int) {
        mMaxHeight = maxHeight
    }

    fun setMaxWidth(maxWidth: Int) {
        mMaxWidth = maxWidth
    }
}