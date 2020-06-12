package com.dinson.blingbase.widget.recycleview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * recycleView点击事件
 */
public interface OnRvItemClickListener {
    void onItemClicked(@NonNull RecyclerView recyclerView, @NonNull View view, @NonNull int position);
}
