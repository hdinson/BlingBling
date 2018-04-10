package dinson.customview.kotlin

import android.util.Log
import dinson.customview.BuildConfig

/**
 *  日志工具类
 */

private val Any.tag get() = javaClass.simpleName
private const val filterTag = "│ --> "
private val isDebug = BuildConfig.DEBUG

fun Any.debug(message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.d(tag, "$filterTag${if (showLine) getLineNumber("debug")
    else ""}${message.toString()}")
}

fun Any.error(message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.e(tag, "$filterTag${if (showLine) getLineNumber("error")
    else ""}${message.toString()}")
}

fun Any.wtf(message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.wtf(tag, "$filterTag${if (showLine) getLineNumber("wtf")
    else ""}${message.toString()}")
}

fun Any.warning(message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.w(tag, "$filterTag${if (showLine) getLineNumber("warning")
    else ""}${message.toString()}")
}

fun Any.info(message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) {
        Log.i(tag, "$filterTag${if (showLine) getLineNumber("info")
        else ""}${message.toString()}")
    }
}

fun Any.verbose(message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.v(tag, "$filterTag${if (showLine) getLineNumber("verbose")
    else ""}${message.toString()}")
}

fun Any.debug(tag: String, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.d(tag, "$filterTag${if (showLine) getLineNumber("debug")
    else ""}${message.toString()}")
}

fun Any.error(tag: String, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.e(tag, "$filterTag${if (showLine) getLineNumber("error")
    else ""}${message.toString()}")
}

fun Any.wtf(tag: String, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.wtf(tag, "$filterTag${if (showLine) getLineNumber("wtf")
    else ""}${message.toString()}")
}

fun Any.warning(tag: String, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.w(tag, "$filterTag${if (showLine) getLineNumber("warning")
    else ""}${message.toString()}")
}

fun Any.info(tag: String, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.i(tag, "$filterTag${if (showLine) getLineNumber("info")
    else ""}${message.toString()}")
}

fun Any.verbose(tag: String, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.v(tag, "$filterTag${if (showLine) getLineNumber("verbose")
    else ""}${message.toString()}")
}

fun Any.debug(context: Any, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.d(context.tag, "$filterTag${if (showLine) getLineNumber("debug")
    else ""}${message.toString()}")
}

fun Any.error(context: Any, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.e(context.tag, "$filterTag${if (showLine) getLineNumber("error")
    else ""}${message.toString()}")
}

fun Any.wtf(context: Any, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.wtf(context.tag, "$filterTag${if (showLine) getLineNumber("wtf")
    else ""}${message.toString()}")
}

fun Any.warning(context: Any, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.w(context.tag, "$filterTag${if (showLine) getLineNumber("warning")
    else ""}${message.toString()}")
}

fun Any.info(context: Any, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.i(context.tag, "$filterTag${if (showLine) getLineNumber("info")
    else ""}${message.toString()}")
}

fun Any.verbose(context: Any, message: Any?, showLine: Boolean = true) = apply {
    if (isDebug) Log.v(context.tag, "$filterTag${if (showLine) getLineNumber("verbose")
    else ""}${message.toString()}")
}

inline fun Any.debug(message: () -> Any?) = debug(message())

inline fun Any.error(message: () -> Any?) = error(message())

inline fun Any.wtf(message: () -> Any?) = wtf(message())

inline fun Any.warning(message: () -> Any?) = warning(message())

inline fun Any.info(message: () -> Any?) = info(message())

inline fun Any.verbose(message: () -> Any?) = verbose(message())

inline fun Any.debug(tag: String, message: () -> Any?) = debug(tag, message())

inline fun Any.error(tag: String, message: () -> Any?) = error(tag, message())

inline fun Any.wtf(tag: String, message: () -> Any?) = wtf(tag, message())

inline fun Any.warning(tag: String, message: () -> Any?) = warning(tag, message())

inline fun Any.info(tag: String, message: () -> Any?) = info(tag, message())

inline fun Any.verbose(tag: String, message: () -> Any?) = verbose(tag, message())

fun Any.debug(context: Any, message: () -> Any?) = debug(context.tag, message())

fun Any.error(context: Any, message: () -> Any?) = error(context.tag, message())

fun Any.wtf(context: Any, message: () -> Any?) = wtf(context.tag, message())

fun Any.warning(context: Any, message: () -> Any?) = warning(context.tag, message())

fun Any.info(context: Any, message: () -> Any?) = info(context.tag, message())

fun Any.verbose(context: Any, message: () -> Any?) = verbose(context.tag, message())


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
