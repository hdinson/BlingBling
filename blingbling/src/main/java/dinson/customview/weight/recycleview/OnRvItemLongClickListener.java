package dinson.customview.weight.recycleview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * recycleView长点击事件
 */
public interface OnRvItemLongClickListener {
    boolean onItemLongClicked(@NonNull RecyclerView recyclerView, @NonNull View view, @NonNull int position);
}
