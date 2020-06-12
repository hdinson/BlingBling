package com.dinson.blingbase.widget.recycleview.multitype

import androidx.annotation.LayoutRes
import android.view.View

interface TypeFactory {
    @LayoutRes
    fun createViewType(bean: MultiType): Int

    fun createViewHolder(type: Int, itemView: View): MultiTypeViewHolder<MultiType>
}

