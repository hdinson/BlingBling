package com.dinson.blingbase.widget.recycleview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * recycleView长点击事件
 */
public interface OnRvItemLongClickListener {
    boolean onItemLongClicked(@NonNull RecyclerView recyclerView, @NonNull View view, @NonNull int position);
}
