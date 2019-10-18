package dinson.customview.weight.recycleview.multitype

import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * 单一布局通用viewholder
 */
abstract class MultiTypeViewHolder<out T>(itemView: View) : RecyclerView.ViewHolder(itemView)  {

    //更新itemView视图(由子类负责完成)
    abstract fun convert(holder: RecyclerView.ViewHolder, t: @UnsafeVariance T, position: Int)
}
