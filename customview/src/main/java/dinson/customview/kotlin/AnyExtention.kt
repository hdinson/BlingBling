package dinson.customview.kotlin

import android.content.res.Resources

/**
 * 所有类的扩展方法
 */
fun dip(dip: Int): Int = (Resources.getSystem().displayMetrics.density * dip).toInt()

fun dip(dip: Float): Int = (Resources.getSystem().displayMetrics.density * dip).toInt()
