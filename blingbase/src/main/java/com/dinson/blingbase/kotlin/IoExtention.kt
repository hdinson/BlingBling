package com.dinson.blingbase.kotlin

import java.io.Closeable
import java.io.IOException

fun Closeable.defCatchClose() {
    try {
        this.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}