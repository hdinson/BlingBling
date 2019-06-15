package dinson.customview.weight.recycleview.multitype


import android.support.annotation.LayoutRes
import android.view.View

interface TypeFactory {
    @LayoutRes
    fun createViewType(bean: MultiType): Int

    fun createViewHolder(type: Int, itemView: View): MultiTypeViewHolder<MultiType>
}

