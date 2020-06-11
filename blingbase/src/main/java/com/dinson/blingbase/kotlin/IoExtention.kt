package com.dinson.blingbase.kotlin

import java.io.Closeable
import java.io.IOException

fun Closeable.close() {
    try {
        this.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}