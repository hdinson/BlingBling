package com.dinson.blingbase.widget.recycleview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class StaggeredItemDecoration extends RecyclerView.ItemDecoration {


    private int left, top, right, bottom;

    public StaggeredItemDecoration(int space) {
        this.left = space / 2;
        this.top = space / 2;
        this.right = space / 2;
        this.bottom = space / 2;
    }

    public StaggeredItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        if (parent.getChildViewHolder(view) instanceof HomeAdapter.FormalThreeVH) {}
        outRect.top = top;
        outRect.bottom = bottom;
        //瀑布流专属分割线
        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        /*
         * 根据params.getSpanIndex()来判断左右边确定分割线
         * 第一列设置左边距为space，右边距为space/2  （第二列反之）
         */
        if (params.getSpanIndex() % 2 == 0) {
            outRect.left = left * 2;
            outRect.right = right;
        } else {
            outRect.left = left;
            outRect.right = right * 2;
        }
    }
}
