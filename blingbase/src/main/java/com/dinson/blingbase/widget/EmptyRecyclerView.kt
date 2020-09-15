package com.dinson.blingbase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

@Suppress("unused")
class EmptyRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val mViewContainer = FrameLayout(context, attrs, defStyleAttr)
    private val mRvContent = InnerRecycleView(mViewContainer, context)

    init {
        mViewContainer.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mRvContent.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addView(mViewContainer)
        addView(mRvContent)
    }

    fun setEmptyView(v: View) {
        mViewContainer.addView(v)
        mRvContent.checkIfEmpty()
    }

    fun setEmptyViewRes(@LayoutRes id: Int) {
        LayoutInflater.from(context).inflate(id, mViewContainer)
        mRvContent.checkIfEmpty()
    }

    fun getInnerRecycleView() = mRvContent

    @SuppressLint("ViewConstructor")
    class InnerRecycleView(
        private val fl: FrameLayout,
        context: Context
    ) : RecyclerView(context) {

        private val observer: AdapterDataObserver =
            object : AdapterDataObserver() {
                override fun onChanged() {
                    checkIfEmpty()
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    checkIfEmpty()
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    checkIfEmpty()
                }
            }


        override fun setAdapter(adapter: Adapter<*>?) {
            val oldAdapter = getAdapter()
            oldAdapter?.unregisterAdapterDataObserver(observer)
            super.setAdapter(adapter)
            adapter?.registerAdapterDataObserver(observer)
            checkIfEmpty()
        }

        fun checkIfEmpty() {
            if (adapter != null) {
                val emptyViewVisible = adapter!!.itemCount == 0
                fl.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
                visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
            }
        }
    }
}