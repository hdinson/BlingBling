package com.dinson.blingbase.retrofit.log

import android.util.Log

/**
 * Logger 接口，方便拓展
 */
interface Logger {
    fun log(level: Int, tag: String, msg: String)

    companion object {
        val DEFAULT: Logger = object : Logger {
            override fun log(level: Int, tag: String, msg: String) {
                when (level) {
                    LogLevel.VERBOSE -> Log.v(tag, msg)
                    LogLevel.DEBUG -> Log.d(tag, msg)
                    LogLevel.INFO -> Log.i(tag, msg)
                    LogLevel.WARN -> Log.w(tag, msg)
                    LogLevel.ERROR -> Log.e(tag, msg)
                }
            }
        }
    }
}