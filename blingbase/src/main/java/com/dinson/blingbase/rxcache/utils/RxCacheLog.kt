package com.dinson.blingbase.rxcache.utils

import android.util.Log

//kotlin实现
class RxCacheLog private constructor() {

    companion object {
        val instance: RxCacheLog by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RxCacheLog()
        }
    }

    var SHOW_LOG = false

    fun logv(message: Any) {
        if (SHOW_LOG.not()) return
        val element = Throwable().stackTrace[1]
        Log.v("[RxCache]", "${toString(message)}${getTag(element)}")
    }

    fun logv(message: Any, error: Throwable?) {
        if (SHOW_LOG.not()) return
        val element = Throwable().stackTrace[1]
        Log.v("[RxCache]", "${toString(message)}${getTag(element)}", error)
    }


    fun loge(message: Any) {
        if (SHOW_LOG.not()) return
        val element = Throwable().stackTrace[1]
        Log.e("[RxCache]", "${toString(message)}${getTag(element)}")
    }

    fun loge(message: Any, error: Throwable?) {
        if (SHOW_LOG.not()) return
        val element = Throwable().stackTrace[1]
        Log.e("[RxCache]", "${toString(message)}${getTag(element)}", error)
    }

    private fun getTag(element: StackTraceElement): String {
        return '(' + element.fileName + ':' + element.lineNumber + ')'
    }

    private fun toString(message: Any?): String {
        if (message == null) {
            return "[null]"
        }
        if (message is Throwable) {
            return Log.getStackTraceString(message as Throwable?)
        }
        return if (message is Collection<*>) {
            toString(message)
        } else message.toString()
    }

    private fun toString(message: Collection<*>): String {
        val it = message.iterator()
        if (!it.hasNext()) return "[]"
        val sb = StringBuilder()
        sb.append('[')
        while (true) {
            val e = it.next()!!
            sb.append(e)
            if (!it.hasNext()) return sb.append(']').toString()
            sb.append(",\n ")
        }
    }
}
