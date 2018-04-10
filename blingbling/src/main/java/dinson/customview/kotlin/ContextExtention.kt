package dinson.customview.kotlin

import android.content.Context
import android.content.res.Configuration
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * Context的扩展方法
 */

/** 获取状态栏高度  */
fun Context.getStatusBarHeight(): Int {
    val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resId > 0) resources.getDimensionPixelSize(resId)
    else dip(24)
}

fun Context.isLandscape() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

fun Context.isPortrait() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

fun Context.getCompatDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Context.getCompatColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

infix fun Context.getDimensFloat(@DimenRes id: Int) = resources.getDimension(id)

fun Context.screenHeight(): Int {
    val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    manager.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.heightPixels
}

fun Context.screenWidth(): Int {
    val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    manager.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.widthPixels
}


/**
 * 关闭软键盘
 *
 */
fun Context.closeKeybord(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    if (imm?.isActive == true) imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}