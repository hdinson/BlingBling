package com.dinson.blingbase.retrofit.log

import okhttp3.Request

/**
 * @author ihsan on 8/12/18.
 */
interface BufferListener {
    fun getJsonResponse(request: Request): String
}