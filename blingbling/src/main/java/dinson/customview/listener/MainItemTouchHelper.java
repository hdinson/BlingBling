package dinson.customview.listener;

import android.graphics.Canvas;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 实现了，拖拽移动列表数据，侧滑删除数据，侧滑条目透明
 */
public class MainItemTouchHelper extends ItemTouchHelper.Callback {

    private final OnItemMoveListener mOnItemMoveListener;

    public MainItemTouchHelper(OnItemMoveListener listener) {
        mOnItemMoveListener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//        int swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int flags = makeMovementFlags(dragFlag, 0);
        return flags;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        boolean result = mOnItemMoveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return result;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mOnItemMoveListener.onItemSwipe2Remove(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float percent = 1 - Math.abs(dX / 2) / viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(percent);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.itemView.getAlpha() != 1) viewHolder.itemView.setAlpha(1);
        super.clearView(recyclerView, viewHolder);
    }
}
