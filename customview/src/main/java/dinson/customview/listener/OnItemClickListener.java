package dinson.customview.listener;

import android.view.View;

/**
 * 条目点击通信类
 */
public interface OnItemClickListener {
    /**
     * 当点击时回调
     *
     * @param v        view
     * @param position 到什么位置
     */
    void onItemClick(View v, int position);
}
