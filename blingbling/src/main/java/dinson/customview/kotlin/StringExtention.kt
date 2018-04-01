package dinson.customview.kotlin

import dinson.customview.utils.ToastUtils

/**
 *   字符串相关的扩展方法
 */
fun String.times(count: Int): String {
    return (1..count).fold(StringBuilder()) { acc, _ ->
        acc.append(this)
    }.toString()
}

fun String.toast() {
    ToastUtils.showToast(this)
}