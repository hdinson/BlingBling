package com.dinson.blingbase.kotlin

import android.content.res.Resources

/**
 * 所有类的扩展方法
 */


/**
 * dp转px
 */
@Suppress("unused")
fun dip(dip: Int): Int = (Resources.getSystem().displayMetrics.density * dip).toInt()

/**
 * dp转px
 */
@Suppress("unused")
fun dip(dip: Float): Int = (Resources.getSystem().displayMetrics.density * dip).toInt()

/**
 * sp转px
 */
fun sp(spValue: Float): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

/**
 * sp转px
 */
fun sp(spValue: Int): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}