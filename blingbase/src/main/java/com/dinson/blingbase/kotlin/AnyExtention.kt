package com.dinson.blingbase.kotlin

import android.content.res.Resources

/**
 * 所有类的扩展方法
 */
@Suppress("unused")
fun dip(dip: Int): Int = (Resources.getSystem().displayMetrics.density * dip).toInt()

@Suppress("unused")
fun dip(dip: Float): Int = (Resources.getSystem().displayMetrics.density * dip).toInt()