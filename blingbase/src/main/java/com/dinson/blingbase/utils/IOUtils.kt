package com.dinson.blingbase.utils

import java.io.Closeable
import java.io.IOException

object IOUtils {
    /**
     * 关闭IO
     *
     * @param closeables closeables
     */
    @JvmStatic
    fun close(vararg closeables: Closeable) {
        closeables.forEach {
            try {
                it.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
