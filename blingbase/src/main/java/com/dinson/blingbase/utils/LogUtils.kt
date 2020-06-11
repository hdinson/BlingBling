package com.dinson.blingbase.utils

import android.util.Log
import com.dinson.blingbase.utils.BlingBaseApplication.isDebug
import java.util.*

object LogUtils {
    /**
     * 日志输出时的TAG
     */
    private const val mTag = "LogUtils"

    /**
     * 方便过滤
     */
    private const val FLAG = "│ --> "
    private val IS_DEBUG = isDebug()
    private fun getLineNumber(methodName: String): String {
        val stackTraceElement = Thread.currentThread().stackTrace
        var currentIndex = -1
        for (i in stackTraceElement.indices) {
            if (stackTraceElement[i].methodName.compareTo(methodName) == 0) {
                currentIndex = i + 1
                break
            }
        }
        val fullClassName = stackTraceElement[currentIndex].className
        val className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
        val lineNumber = stackTraceElement[currentIndex].lineNumber.toString()
        return ".($className.java:$lineNumber)"
    }

    /**
     * 以级别为 v 的形式输出LOG
     */
    fun v(msg: String) {
        if (IS_DEBUG) {
            Log.v(mTag, FLAG + getLineNumber("v") + msg)
        }
    }

    /**
     * 以级别为 v 的形式输出LOG
     */
    fun v(msg: String, showLine: Boolean) {
        if (IS_DEBUG) {
            Log.v(mTag, String.format("%s%s", if (showLine) getLineNumber("v") else "", FLAG + msg))
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    fun d(msg: String) {
        if (IS_DEBUG) {
            Log.d(mTag, FLAG + getLineNumber("d") + msg)
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    fun d(msg: String, showLine: Boolean) {
        if (IS_DEBUG) {
            Log.d(mTag, String.format("%s%s", if (showLine) getLineNumber("d") else "", FLAG + msg))
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    fun i(msg: String) {
        if (IS_DEBUG) {
            Log.i(mTag, FLAG + getLineNumber("i") + msg)
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    fun i(msg: String, showLine: Boolean) {
        if (IS_DEBUG) {
            Log.i(mTag, String.format(Locale.CHINA, "%s%s", FLAG + msg
                , if (showLine) getLineNumber("i") else ""))
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    fun w(msg: String) {
        if (IS_DEBUG) {
            Log.w(mTag, FLAG + getLineNumber("w") + msg)
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    fun w(msg: String, showLine: Boolean) {
        if (IS_DEBUG) {
            Log.w(mTag, String.format(Locale.CHINA, "%s%s", FLAG + msg
                , if (showLine) getLineNumber("w") else ""))
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */
    fun e(msg: String) {
        if (IS_DEBUG) {
            Log.e(mTag, FLAG + getLineNumber("e") + msg)
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */
    fun e(msg: String, showLine: Boolean) {
        if (IS_DEBUG) {
            Log.e(mTag, String.format(Locale.CHINA, "%s%s", FLAG + msg
                , if (showLine) getLineNumber("e") else ""))
        }
    }
}