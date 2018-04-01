package dinson.customview.listener

import dinson.customview.weight.swipelayout.SwipeItemLayout

/**
 *   @author Dinson - 2017/10/16
 */
interface OnItemSwipeOpen {
    fun onOpen(view: SwipeItemLayout, position: Int)
}