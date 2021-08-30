package dinson.customview.listener

import dinson.customview.widget.swipelayout.SwipeItemLayout

/**
 *  货币滑动监听
 */
interface OnItemSwipeOpen {
    fun onOpen(view: SwipeItemLayout, position: Int)
}