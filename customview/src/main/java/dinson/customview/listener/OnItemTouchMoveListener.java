package dinson.customview.listener;

import android.support.v7.widget.RecyclerView;

/**
 * 当条目移动时的监听
 */
public interface OnItemTouchMoveListener {
    /**
     * 当viewholder中触摸时回调
     *
     * @param viewHolder 需要移动的viewholder
     */
    void onItemTouchMove(RecyclerView.ViewHolder viewHolder);
}
