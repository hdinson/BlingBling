package com.dinson.blingbase.widget.recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 单一布局通用viewholder
 */
class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        /**
         * 加载layoutId视图并用LGViewHolder保存
         */
        fun getViewHolder(parent: ViewGroup, layoutId: Int): CommonViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            return CommonViewHolder(itemView)
        }
    }
}