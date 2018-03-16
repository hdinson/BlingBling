package dinson.customview.kotlin

import android.content.Context
import android.content.res.Resources
import dinson.customview._global.GlobalApplication

/**
 * 所有类的扩展方法
 */
fun dip(dip: Int): Int = (Resources.getSystem().displayMetrics.density * dip).toInt()

fun dip(dip: Float): Int = (Resources.getSystem().displayMetrics.density * dip).toInt()

fun getContext(): Context = GlobalApplication.getContext()