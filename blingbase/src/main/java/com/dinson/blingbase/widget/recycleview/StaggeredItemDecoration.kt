package com.dinson.blingbase.widget.recycleview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager

@Suppress("unused")
class StaggeredItemDecoration : ItemDecoration {
    private var left: Int
    private var top: Int
    private var right: Int
    private var bottom: Int

    constructor(space: Int) {
        left = space / 2
        top = space / 2
        right = space / 2
        bottom = space / 2
    }

    constructor(left: Int, top: Int, right: Int, bottom: Int) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //if (parent.getChildViewHolder(view) instanceof HomeAdapter.FormalThreeVH) {}
        outRect.top = top
        outRect.bottom = bottom
        //瀑布流专属分割线
        val params = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        /*
         * 根据params.getSpanIndex()来判断左右边确定分割线
         * 第一列设置左边距为space，右边距为space/2  （第二列反之）
         */if (params.spanIndex % 2 == 0) {
            outRect.left = left * 2
            outRect.right = right
        } else {
            outRect.left = left
            outRect.right = right * 2
        }
    }
}