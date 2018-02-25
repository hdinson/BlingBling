package dinson.customview.kotlin

import android.content.res.Resources

/**
 * 所有类的扩展方法
 */
fun Any.dip(dip: Int) = Resources.getSystem().displayMetrics.density * dip + 0.5f
fun Any.dip(dip: Float) = Resources.getSystem().displayMetrics.density * dip + 0.5f
