package dinson.blingxposed.kotlin

import android.util.Log
import dinson.blingxposed.BuildConfig

/**
 *  日志工具类
 */

private val Any.tag get() = javaClass.simpleName
private const val filterTag = "│ --> "

fun Any.logd(message: Any?, showLine: Boolean = true) {
    Log.d(tag, "$filterTag${if (showLine) getLineNumber("logd")
    else ""}${message.toString()}")
}

fun Any.loge(message: Any?, showLine: Boolean = true) = apply {
    Log.e(tag, "$filterTag${if (showLine) getLineNumber("loge")
    else ""}${message.toString()}")
}

fun Any.logwtf(message: Any?, showLine: Boolean = true) = apply {
    Log.wtf(tag, "$filterTag${if (showLine) getLineNumber("logwtf")
    else ""}${message.toString()}")
}

fun Any.logw(message: Any?, showLine: Boolean = true) = apply {
    Log.w(tag, "$filterTag${if (showLine) getLineNumber("logw")
    else ""}${message.toString()}")
}

fun Any.logi(message: Any?, showLine: Boolean = true) = apply {

    Log.i(tag, "$filterTag${if (showLine) getLineNumber("logi")
    else ""}${message.toString()}")

}

fun Any.logv(message: Any?, showLine: Boolean = true) = apply {
    Log.v(tag, "$filterTag${if (showLine) getLineNumber("logv")
    else ""}${message.toString()}")
}

fun Any.logd(tag: String, message: Any?, showLine: Boolean = true) = apply {
    Log.d(tag, "$filterTag${if (showLine) getLineNumber("logd")
    else ""}${message.toString()}")
}

fun Any.loge(tag: String, message: Any?, showLine: Boolean = true) = apply {
    Log.e(tag, "$filterTag${if (showLine) getLineNumber("loge")
    else ""}${message.toString()}")
}

fun Any.logwtf(tag: String, message: Any?, showLine: Boolean = true) = apply {
    Log.wtf(tag, "$filterTag${if (showLine) getLineNumber("logwtf")
    else ""}${message.toString()}")
}

fun Any.logw(tag: String, message: Any?, showLine: Boolean = true) = apply {
    Log.w(tag, "$filterTag${if (showLine) getLineNumber("logw")
    else ""}${message.toString()}")
}

fun Any.logi(tag: String, message: Any?, showLine: Boolean = true) = apply {
    Log.i(tag, "$filterTag${if (showLine) getLineNumber("logi")
    else ""}${message.toString()}")
}

fun Any.logv(tag: String, message: Any?, showLine: Boolean = true) = apply {
    Log.v(tag, "$filterTag${if (showLine) getLineNumber("logv")
    else ""}${message.toString()}")
}

fun Any.logd(context: Any, message: Any?, showLine: Boolean = true) = apply {
    Log.d(context.tag, "$filterTag${if (showLine) getLineNumber("logd")
    else ""}${message.toString()}")
}

fun Any.loge(context: Any, message: Any?, showLine: Boolean = true) = apply {
    Log.e(context.tag, "$filterTag${if (showLine) getLineNumber("loge")
    else ""}${message.toString()}")
}

fun Any.logwtf(context: Any, message: Any?, showLine: Boolean = true) = apply {
    Log.wtf(context.tag, "$filterTag${if (showLine) getLineNumber("logwtf")
    else ""}${message.toString()}")
}

fun Any.logw(context: Any, message: Any?, showLine: Boolean = true) = apply {
    Log.w(context.tag, "$filterTag${if (showLine) getLineNumber("logw")
    else ""}${message.toString()}")
}

fun Any.logi(context: Any, message: Any?, showLine: Boolean = true) = apply {
    Log.i(context.tag, "$filterTag${if (showLine) getLineNumber("logi")
    else ""}${message.toString()}")
}

fun Any.logv(context: Any, message: Any?, showLine: Boolean = true) = apply {
    Log.v(context.tag, "$filterTag${if (showLine) getLineNumber("logv")
    else ""}${message.toString()}")
}

fun String.logd(showLine: Boolean = true) {
    Log.d(this, "$filterTag${if (showLine) getLineNumber("logd")
    else ""}${this}")
}

fun String.loge(showLine: Boolean = true) {
    Log.e(this, "$filterTag${if (showLine) getLineNumber("loge")
    else ""}${this}")
}

fun String.logwtf(showLine: Boolean = true) {
    Log.wtf(this, "$filterTag${if (showLine) getLineNumber("logwtf")
    else ""}${this}")
}

fun String.logw(showLine: Boolean = true) {
    Log.w(this, "$filterTag${if (showLine) getLineNumber("logw")
    else ""}${this}")
}

fun String.logi(showLine: Boolean = true) {
    Log.i(this, "$filterTag${if (showLine) getLineNumber("logi")
    else ""}${this}")
}

fun String.logv(showLine: Boolean = true) {
    Log.v(this, "$filterTag${if (showLine) getLineNumber("logv")
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
