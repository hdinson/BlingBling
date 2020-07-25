package com.dinson.blingbase.widget.recycleview

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

@Suppress("unused")
class GridSpaceItemDecoration(private val leftRight: Int, private val topBottom: Int) : ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as GridLayoutManager?
        //判断总的数量是否可以整除
        val totalCount = layoutManager!!.itemCount
        val surplusCount = totalCount % layoutManager.spanCount
        val childPosition = parent.getChildAdapterPosition(view)
        if (layoutManager.orientation == RecyclerView.VERTICAL) { //竖直方向的
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.spanCount - 1) {
                //后面几项需要bottom
                outRect.bottom = topBottom
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.bottom = topBottom
            }
            if ((childPosition + 1) % layoutManager.spanCount == 0) { //被整除的需要右边
                outRect.right = leftRight
            }
            outRect.top = topBottom
            outRect.left = leftRight
        } else {
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.spanCount - 1) {
                //后面几项需要右边
                outRect.right = leftRight
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.right = leftRight
            }
            if ((childPosition + 1) % layoutManager.spanCount == 0) { //被整除的需要下边
                outRect.bottom = topBottom
            }
            outRect.top = topBottom
            outRect.left = leftRight
        }
    }

}