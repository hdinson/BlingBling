package dinson.customview.weight.recycleview

import android.support.v7.widget.RecyclerView
import android.view.View
import dinson.customview.R
import dinson.customview.kotlin.click
import dinson.customview.kotlin.longClick

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

    fun setOnItemClickListener(listener: OnRvItemClickListener): RvItemClickSupport {
        mOnItemClickListener = listener
        return this
    }

    fun setOnItemLongClickListener(listener: OnRvItemLongClickListener): RvItemClickSupport {
        mOnItemLongClickListener = listener
        return this
    }


    /******************************************************************************************************/
    /**                             内部实现                                                               **/
    /******************************************************************************************************/

    private var mOnItemClickListener: OnRvItemClickListener? = null
    private var mOnItemLongClickListener: OnRvItemLongClickListener? = null


    private val mAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            if (mOnItemClickListener != null) {
                view.click {
                    mOnItemClickListener?.apply {
                        val pos = mRecyclerView.getChildViewHolder(it).adapterPosition
                        this.onItemClicked(mRecyclerView, it, pos)
                    }
                }
            }
            if (mOnItemLongClickListener != null) {
                view.longClick {
                    mOnItemLongClickListener?.apply {
                        val pos = mRecyclerView.getChildViewHolder(it).adapterPosition
                        return@longClick this.onItemLongClicked(mRecyclerView, it, pos)
                    }
                    false
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
}
