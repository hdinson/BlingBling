package dinson.customview.kotlin

import android.util.Log
import dinson.customview.BuildConfig
import kotlin.reflect.KProperty0

/**
 *  日志工具类
 */
private const val filterTag = "│ --> "
private const val TAG = "RxBling"
private val isDebug = BuildConfig.DEBUG


@JvmSuppressWildcards
fun logv(showLine: Boolean = true, func: () -> String) {
    if (isDebug) {
        Log.v(TAG, "$filterTag${if (showLine) getLineNumber("logv") else ""}${func()}")
    }
}

@JvmSuppressWildcards
fun logv(func: () -> String) {
    if (isDebug) {
        Log.v(TAG, "$filterTag${func()}")
    }
}

@JvmSuppressWildcards
fun logd(showLine: Boolean = true, func: () -> String) {
    if (isDebug) {
        Log.d(TAG, "$filterTag${if (showLine) getLineNumber("logd") else ""}${func()}")
    }
}

@JvmSuppressWildcards
fun logd(func: () -> String) {
    if (isDebug) {
        Log.d(TAG, "$filterTag${func()}")
    }
}

@JvmSuppressWildcards
fun logi(showLine: Boolean = true, func: () -> String) {
    if (isDebug) {
        Log.i(TAG, "$filterTag${if (showLine) getLineNumber("logi") else ""}${func()}")
    }
}

@JvmSuppressWildcards
fun logi(func: () -> String) {
    if (isDebug) {
        Log.i(TAG, "$filterTag${func()}")
    }
}

@JvmSuppressWildcards
fun logw(showLine: Boolean = true, func: () -> String) {
    if (isDebug) {
        Log.w(TAG, "$filterTag${if (showLine) getLineNumber("logw") else ""}${func()}")
    }
}

@JvmSuppressWildcards
fun logw(func: () -> String) {
    if (isDebug) {
        Log.w(TAG, "$filterTag${func()}")
    }
}

@JvmSuppressWildcards
fun loge(showLine: Boolean = true, func: () -> String) {
    if (isDebug) {
        Log.e(TAG, "$filterTag${if (showLine) getLineNumber("loge") else ""}${func()}")
    }
}

@JvmSuppressWildcards
fun loge(func: () -> String) {
    if (isDebug) {
        Log.e(TAG, "$filterTag${func()}")
    }
}

@JvmSuppressWildcards
fun logwtf(showLine: Boolean = true, func: () -> String) {
    if (isDebug) {
        Log.wtf(TAG, "$filterTag${if (showLine) getLineNumber("logwtf") else ""}${func()}")
    }
}

@JvmSuppressWildcards
fun logwtf(func: () -> String) {
    if (isDebug) {
        Log.wtf(TAG, "$filterTag${func()}")
    }
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
