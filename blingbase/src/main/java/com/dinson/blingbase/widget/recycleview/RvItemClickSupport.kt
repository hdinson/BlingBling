package com.dinson.blingbase.widget.recycleview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dinson.blingbase.R
import com.dinson.blingbase.kotlin.click
import com.dinson.blingbase.kotlin.longClick

/**
 * RecycleView点击支持类
 */
class RvItemClickSupport private constructor(private val mRecyclerView: RecyclerView) {

    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/
    companion object {

        fun addTo(view: RecyclerView): RvItemClickSupport {
            val support = view.getTag(R.id.itemClickSupport) as? RvItemClickSupport
            return support ?: RvItemClickSupport(view)
        }

        fun removeFrom(view: RecyclerView): RvItemClickSupport? {
            val support = view.getTag(R.id.itemClickSupport) as? RvItemClickSupport
            support?.detach(view)
            return support
        }

    }


    private var mItemLongClick: ((recyclerView: RecyclerView, view: View, position: Int) -> Boolean)? = null


    fun setOnItemLongClickListener(func: (recyclerView: RecyclerView, view: View, position: Int) -> Boolean): RvItemClickSupport {
        mItemLongClick = func
        return this
    }

    private var mItemClick: ((recyclerView: RecyclerView, view: View, position: Int) -> Unit)? = null

    fun setOnItemClickListener(func: (recyclerView: RecyclerView, view: View, position: Int) -> Unit): RvItemClickSupport {
        mItemClick = func
        return this
    }


    /******************************************************************************************************/
    /**                             内部实现                                                               **/
    /******************************************************************************************************/


    private val mAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            mItemClick?.apply {
                view.click {
                    val pos = mRecyclerView.getChildViewHolder(it).adapterPosition
                    this(mRecyclerView, it, pos)
                }
            }
            mItemLongClick?.apply {
                view.longClick {
                    val pos = mRecyclerView.getChildViewHolder(it).adapterPosition
                    this(mRecyclerView, it, pos)
                }
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {

        }
    }

    init {
        mRecyclerView.setTag(R.id.itemClickSupport, this)
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener)
    }


    private fun detach(view: RecyclerView) {
        view.removeOnChildAttachStateChangeListener(mAttachListener)
        view.setTag(R.id.itemClickSupport, null)
    }

    /**
     * recycleView点击事件
     */
    interface OnRvItemClickListener {
        fun onItemClicked(recyclerView: RecyclerView, view: View, position: Int)
    }

    /**
     * recycleView长点击事件
     */
    interface OnRvItemLongClickListener {
        fun onItemLongClicked(recyclerView: RecyclerView, view: View, position: Int): Boolean
    }
/*
    interface OnItemClick {
        fun onClicked(recyclerView: RecyclerView, view: View, position: Int)
        fun onLongClicked(recyclerView: RecyclerView, view: View, position: Int): Boolean
    }

    class OnItemClickImp(private val onClick: (recyclerView: RecyclerView, view: View, position: Int) -> Unit,
                         private val onLongClick: (recyclerView: RecyclerView, view: View, position: Int) -> Boolean) : OnItemClick {
        override fun onClicked(recyclerView: RecyclerView, view: View, position: Int) {
            onClick(recyclerView, view, position)
        }

        override fun onLongClicked(recyclerView: RecyclerView, view: View, position: Int): Boolean {
            return onLongClick(recyclerView, view, position)
        }

    }

    private var mCallBack: OnItemClickImp? = null
    fun setOnItemClickListener(callback: OnItemClickImp) {
        mCallBack = callback
    }
 */
}
