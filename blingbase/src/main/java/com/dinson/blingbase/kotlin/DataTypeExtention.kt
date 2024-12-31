package com.dinson.blingbase.kotlin

import android.view.View
import java.text.DecimalFormat
import kotlin.math.roundToInt

/******************************************************************************************************/
/**                             String                                                               **/
/******************************************************************************************************/

/**
 *   字符串相关的扩展方法
 */
fun String.times(count: Int): String {
    return (1..count).fold(StringBuilder()) { acc, _ ->
        acc.append(this)
    }.toString()
}

/**
 * 首字母大写
 *
 * @return 首字母大写字符串
 */
fun String.upperFirstLetter(): String {
    return if (Character.isLowerCase(this[0])) this
    else "${(this[0].toInt() - 32).toChar()} ${this.substring(1)}"
}

/**
 * 首字母小写
 *
 * @return 首字母小写字符串
 */
fun String.lowerFirstLetter(): String {
    return if (Character.isUpperCase(this[0])) this
    else "${(this[0].toInt() + 32).toChar()} ${this.substring(1)}"
}

/******************************************************************************************************/
/**                             Double                                                               **/
/******************************************************************************************************/

/**
 * 格式化成货币（保留两位小数）
 * @return str
 */
fun Double.formatMoney(): String {
    val df = DecimalFormat("###,##0.00")
    return df.format(this)
}

/**
 * 格式化double，如果为整数显示整数
 */
fun Double.doubleTrans(): String {
    val roundToInt = this.roundToInt()
    return if (roundToInt - this == 0.0)  roundToInt.toString() else this.toString()
}


/******************************************************************************************************/
/**                             Long                                                                 **/
/******************************************************************************************************/

/**
 * 格式化数据大小
 * @return 例: 5B, 10KB, 10.4MB, 10.9GB
 */
fun Long.formatFileSize(): String {
    //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
    var size = this
    size = if (size < 1024) {
        return size.toString() + "B"
    } else {
        size / 1024
    }
    //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
    size = if (size < 1024) {
        return size.toString() + "KB"
    } else {
        size / 1024
    }
    return if (size < 1024) {
        //因为如果以MB为单位的话，要保留最后1位小数，
        //因此，把此数乘以100之后再取余
        size *= 100
        ((size / 100).toString() + "."
                + (size % 100).toString() + "MB")
    } else {
        //否则如果要以GB为单位的，先除于1024再作同样的处理
        size = size * 100 / 1024
        ((size / 100).toString() + "."
                + (size % 100).toString() + "GB")
    }
}

/******************************************************************************************************/
/**                             Max Min                                                              **/
/******************************************************************************************************/
infix fun Int.max(other: Int) = if (this > other) this else other
infix fun Float.max(other: Float) = if (this > other) this else other
infix fun Double.max(other: Double) = if (this > other) this else other
infix fun Number.max(other: Number) = if (this.toFloat() > other.toFloat()) this else other

infix fun Int.min(other: Int) = if (this < other) this else other
infix fun Float.min(other: Float) = if (this < other) this else other
infix fun Double.min(other: Double) = if (this < other) this else other
infix fun Number.min(other: Number) = if (this.toFloat() < other.toFloat()) this else other
