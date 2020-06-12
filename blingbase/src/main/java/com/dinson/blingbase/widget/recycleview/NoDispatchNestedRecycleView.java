package com.dinson.blingbase.widget.recycleview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;


public class NoDispatchNestedRecycleView extends RecyclerView {
    public NoDispatchNestedRecycleView(@NonNull Context context) {
        super(context);
    }

    public NoDispatchNestedRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoDispatchNestedRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return super.startNestedScroll(axes, ViewCompat.TYPE_TOUCH);
    }
}
