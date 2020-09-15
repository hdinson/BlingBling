package com.dinson.blingbase.kotlin

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat


/**
 * Context的扩展方法
 */

/** 获取状态栏高度  */
fun Context.getStatusBarHeight(): Int {
    val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resId > 0) resources.getDimensionPixelSize(resId)
    else dip(24)
}

@Suppress("unused")
fun Context.isLandscape() =
    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

@Suppress("unused")
fun Context.isPortrait() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

@Suppress("unused")
fun Context.getCompatDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

@Suppress("unused")
fun Context.getCompatColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

infix fun Context.getDimensFloat(@DimenRes id: Int) = resources.getDimension(id)

/**
 * 获取App名称
 *
 * @return App名称
 */
fun Context.getAppName(): String {
    val pi = packageManager.getPackageInfo(this.packageName, 0)
    return pi.applicationInfo.loadLabel(packageManager).toString()
}


@Suppress("unused")
fun Context.screenHeight(): Int {
    val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    manager.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.heightPixels
}

@Suppress("unused")
fun Context.screenWidth(): Int {
    val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    manager.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.widthPixels
}


/**
 * 关闭软键盘
 */
@Suppress("unused")
fun Context.closeKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    if (imm?.isActive == true) imm.hideSoftInputFromWindow(
        view.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}

/**
 * 打开软键盘
 */
@Suppress("unused")
fun Context.openKeyboard(view: View) {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

