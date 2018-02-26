package dinson.customview.utils

import android.support.annotation.FloatRange

/**
 *  颜色相关的工具类
 */
object ColorUtils {

    fun calculateColorAlpht(color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        val a = if (color and -0x1000000 == 0) 0xff else color.ushr(24)
        return color and 0x00ffffff or ((a * alpha).toInt() shl 24)
    }
}