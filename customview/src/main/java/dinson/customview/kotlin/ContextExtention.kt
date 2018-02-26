package dinson.customview.kotlin

import android.content.Context

/**
 * Context的扩展方法
 */

/** 获取状态栏高度  */
fun Context.getStatusBarHeight(): Int {
    val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resId > 0) resources.getDimensionPixelSize(resId)
    else dip(24).toInt()
}