package com.dinson.blingbase.retrofit.log

import com.dinson.blingbase.kotlin.*

/**
 * Logger 接口，方便拓展
 */
interface Logger {
    fun log(level: Int, tag: String, msg: String)

    companion object {
        val DEFAULT: Logger = object : Logger {
            override fun log(level: Int, tag: String, msg: String) {
                when (level) {
                    LogLevel.VERBOSE -> logv(tag, msg,false)
                    LogLevel.DEBUG -> logd(tag, msg,false)
                    LogLevel.INFO -> logi(tag, msg,false)
                    LogLevel.WARN -> logw(tag, msg,false)
                    LogLevel.ERROR -> loge(tag, msg,false)
                }
            }
        }
    }
}