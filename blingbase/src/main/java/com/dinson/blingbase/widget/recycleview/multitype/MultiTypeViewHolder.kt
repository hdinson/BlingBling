package com.dinson.blingbase.widget.recycleview.multitype

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 单一布局通用viewholder
 */
abstract class MultiTypeViewHolder<out T>(itemView: View) : RecyclerView.ViewHolder(itemView)  {

    //更新itemView视图(由子类负责完成)
    abstract fun convert(holder: RecyclerView.ViewHolder, t: @UnsafeVariance T, position: Int)
}
