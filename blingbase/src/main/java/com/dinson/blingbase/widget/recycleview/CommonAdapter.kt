package com.dinson.blingbase.widget.recycleview

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 单一布局通用数据适配器
 */
abstract class CommonAdapter<T>(var mDataList: MutableList<T>) : RecyclerView.Adapter<CommonViewHolder>() {
    private val mViewHolder = SparseArray<CommonViewHolder>()

    /**
     * 获取列表控件的视图id(由子类负责完成)
     */
    abstract fun getLayoutId(viewType: Int): Int

    //更新itemView视图(由子类负责完成)
    abstract fun convert(holder: CommonViewHolder, bean: T, position: Int)

    private fun getItem(position: Int): T {
        return mDataList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val layoutId = getLayoutId(viewType)
        return CommonViewHolder.getViewHolder(parent, layoutId)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        val itemModel = getItem(position)
        mViewHolder.put(position, holder)
        convert(holder, itemModel, position) //更新itemView视图
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun getCommonViewHolder(position: Int): CommonViewHolder {
        return mViewHolder[position]
    }
}