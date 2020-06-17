package com.dinson.blingbase.kotlin

import android.util.Log
import com.dinson.blingbase.utils.RxBling

/**
 *  日志工具类
 */

private val Any.tag get() = javaClass.simpleName
private const val filterTag = "│ --> "
private val isDebug = RxBling.isDebug()

fun Any.logd(message: Any?, showLine: Boolean = true) {
    if (isDebug) Log.d(tag, "$filterTag${if (showLine) getLineNumber("logd")
    else ""}${message.toString()}")
}

fun Any.loge(message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.e(tag, "$filterTag${if (showLine) getLineNumber("loge")
    else ""}${message.toString()}")
}

fun Any.logwtf(message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.wtf(tag, "$filterTag${if (showLine) getLineNumber("logwtf")
    else ""}${message.toString()}")
}

fun Any.logw(message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.w(tag, "$filterTag${if (showLine) getLineNumber("logw")
    else ""}${message.toString()}")
}

fun Any.logi(message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) {
        Log.i(tag, "$filterTag${if (showLine) getLineNumber("logi")
        else ""}${message.toString()}")
    }
}

fun Any.logv(message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.v(tag, "$filterTag${if (showLine) getLineNumber("logv")
    else ""}${message.toString()}")
}

fun Any.logd(tag: String, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.d(tag, "$filterTag${if (showLine) getLineNumber("logd")
    else ""}${message.toString()}")
}

fun Any.loge(tag: String, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.e(tag, "$filterTag${if (showLine) getLineNumber("loge")
    else ""}${message.toString()}")
}

fun Any.logwtf(tag: String, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.wtf(tag, "$filterTag${if (showLine) getLineNumber("logwtf")
    else ""}${message.toString()}")
}

fun Any.logw(tag: String, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.w(tag, "$filterTag${if (showLine) getLineNumber("logw")
    else ""}${message.toString()}")
}

fun Any.logi(tag: String, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.i(tag, "$filterTag${if (showLine) getLineNumber("logi")
    else ""}${message.toString()}")
}

fun Any.logv(tag: String, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.v(tag, "$filterTag${if (showLine) getLineNumber("logv")
    else ""}${message.toString()}")
}

fun Any.logd(context: Any, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.d(context.tag, "$filterTag${if (showLine) getLineNumber("logd")
    else ""}${message.toString()}")
}

fun Any.loge(context: Any, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.e(context.tag, "$filterTag${if (showLine) getLineNumber("loge")
    else ""}${message.toString()}")
}

fun Any.logwtf(context: Any, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.wtf(context.tag, "$filterTag${if (showLine) getLineNumber("logwtf")
    else ""}${message.toString()}")
}

fun Any.logw(context: Any, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.w(context.tag, "$filterTag${if (showLine) getLineNumber("logw")
    else ""}${message.toString()}")
}

fun Any.logi(context: Any, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.i(context.tag, "$filterTag${if (showLine) getLineNumber("logi")
    else ""}${message.toString()}")
}

fun Any.logv(context: Any, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.v(context.tag, "$filterTag${if (showLine) getLineNumber("logv")
    else ""}${message.toString()}")
}

fun String.logd(showLine: Boolean = true) {
    if (isDebug) Log.d(this, "$filterTag${if (showLine) getLineNumber("logd")
    else ""}${this}")
}

fun String.loge(showLine: Boolean = true) {
    if (isDebug) Log.e(this, "$filterTag${if (showLine) getLineNumber("loge")
    else ""}${this}")
}

fun String.logwtf(showLine: Boolean = true) {
    if (isDebug) Log.wtf(this, "$filterTag${if (showLine) getLineNumber("logwtf")
    else ""}${this}")
}

fun String.logw(showLine: Boolean = true) {
    if (isDebug) Log.w(this, "$filterTag${if (showLine) getLineNumber("logw")
    else ""}${this}")
}

fun String.logi(showLine: Boolean = true) {
    if (isDebug) Log.i(this, "$filterTag${if (showLine) getLineNumber("logi")
    else ""}${this}")
}

fun String.logv(showLine: Boolean = true) {
    if (isDebug) Log.v(this, "$filterTag${if (showLine) getLineNumber("logv")
    else ""}${this}")
}

private fun getLineNumber(methodName: String): String {
    val stackTraceElement = Thread.currentThread().stackTrace
    val currentIndex = stackTraceElement.indices
        .firstOrNull { stackTraceElement[it].methodName.compareTo(methodName) == 0 }
        ?.let { it + 2 }
        ?: -1
    val fileName = stackTraceElement[currentIndex].fileName
    val lineNumber = stackTraceElement[currentIndex].lineNumber.toString()
    return "($fileName:$lineNumber)"
}
