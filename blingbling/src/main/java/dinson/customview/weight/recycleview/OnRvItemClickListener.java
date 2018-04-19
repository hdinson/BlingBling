package dinson.customview.weight.recycleview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * recycleView点击事件
 */
public interface OnRvItemClickListener {
    void onItemClicked(@NonNull RecyclerView recyclerView, @NonNull View view, @NonNull int position);
}
