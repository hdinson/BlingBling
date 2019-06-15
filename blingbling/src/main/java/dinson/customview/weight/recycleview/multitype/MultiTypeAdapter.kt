package dinson.customview.weight.recycleview.multitype

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup


/**
 * 多重布局的适配器
 */
class MultiTypeAdapter(private val modelList: List<MultiType>, private val typeFactory: TypeFactory)
    : RecyclerView.Adapter<MultiTypeViewHolder<MultiType>>() {

    override fun onBindViewHolder(holder: MultiTypeViewHolder<MultiType>, position: Int) {
        holder.convert(holder, modelList[position], position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiTypeViewHolder<MultiType> {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(viewType, itemView)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    override fun getItemViewType(position: Int): Int {
        return typeFactory.createViewType(modelList[position])
    }
}