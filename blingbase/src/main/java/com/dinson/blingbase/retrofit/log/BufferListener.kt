package com.dinson.blingbase.retrofit.log

import okhttp3.Request

interface BufferListener {
    fun getJsonResponse(request: Request): String
}