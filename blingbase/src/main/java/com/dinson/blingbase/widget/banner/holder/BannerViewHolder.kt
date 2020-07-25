package com.dinson.blingbase.widget.banner.holder

import android.content.Context
import android.view.View

interface BannerViewHolder<T> {
    /**
     * 创建View
     */
    fun createView(context: Context): View

    /**
     * 绑定数据
     */
    fun onBind(context: Context, position: Int, data: T)
}