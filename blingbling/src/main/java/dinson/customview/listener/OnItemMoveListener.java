package dinson.customview.listener;

/**
 * 条目移动更新的监听
 */
public interface OnItemMoveListener {
    /**
     * 当拖拽时回调
     *
     * @param fromPosition 从什么位置
     * @param toPosition   到什么位置
     * @return 是否移动成功
     */
    boolean onItemMove(int fromPosition, int toPosition);


    /**
     * 当条目侧滑时回调
     *
     * @param position 侧滑的条目
     */
    void onItemSwipe2Remove(int position);

}
