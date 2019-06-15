package dinson.customview.listener

import dinson.customview.weight.swipelayout.SwipeItemLayout

/**
 *  货币滑动监听
 */
interface OnItemSwipeOpen {
    fun onOpen(view: SwipeItemLayout, position: Int)
}