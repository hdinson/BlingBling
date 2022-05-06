package com.dinson.blingbase.retrofit.log

import okhttp3.*
import okhttp3.MediaType.parse
import java.util.*
import java.util.concurrent.TimeUnit

class LoggingInterceptor(val builder: Builder) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = addQueryAndHeaders(chain.request())

        if (builder.isLoggerEnabled) printlnRequestLog(request)

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = proceedResponse(chain, request)
        } catch (e: Exception) {
            Printer.printFailed(builder, e)
            throw e
        }
        if (builder.isLoggerEnabled) {
            val receivedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
            printlnResponseLog(receivedMs, response, request)
        }
        return response
    }

    private fun printlnResponseLog(receivedMs: Long, response: Response, request: Request) {
        Printer.printJsonResponse(
            builder,
            receivedMs,
            response.code(),
            response.headers(),
            response,
            response.message(),
            request.url().toString(),
            request.url().isHttps)
    }

    private fun printlnRequestLog(request: Request) {
        Printer.printJsonRequest(
            builder,
            request.body(),
            request.url().toString(),
            request.headers(),
            request.method())
    }

    private fun proceedResponse(chain: Interceptor.Chain, request: Request): Response {
        return if (builder.isMockEnabled && builder.listener != null) {
            TimeUnit.MILLISECONDS.sleep(builder.sleepMs)

            Response.Builder()
                .body(ResponseBody.create(parse("application/json"), builder.listener!!.getJsonResponse(request)))
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .message("Mock data from LoggingInterceptor")
                .code(200)
                .build()
        } else chain.proceed(request)
    }

    private fun addQueryAndHeaders(request: Request): Request {
        val requestBuilder = request.newBuilder()
        builder.headers.forEach {
            requestBuilder.addHeader(it.key, it.value)
        }
        builder.dyHeaders.forEach {
            val function = it.value
            val value = function()
            if (value.isNotEmpty())
                requestBuilder.addHeader(it.key, value)
        }
        val httpUrlBuilder: HttpUrl.Builder? = request.url().newBuilder(request.url().toString())
        httpUrlBuilder?.let {
            builder.queryParam.keys.forEach { key ->
                httpUrlBuilder.addQueryParameter(key, builder.queryParam[key])
            }
            builder.dyQueryParam.keys.forEach { key ->
                val function = builder.dyQueryParam[key]
                if (function != null) {
                    val value = function()
                    if (value.isNotEmpty())
                        httpUrlBuilder.addQueryParameter(key, value)
                }
            }
        }
        return requestBuilder.url(httpUrlBuilder?.build()!!).build()
    }

    @Suppress("unused")
    class Builder {

        val headers: HashMap<String, String> = HashMap()
        val dyHeaders: HashMap<String, () -> String> = HashMap()
        val queryParam: HashMap<String, String> = HashMap()
        val dyQueryParam: HashMap<String, () -> String> = HashMap()

        //Log
        var logger: Logger = Logger.DEFAULT

        @LogLevel
        var logLevel: Int = LogLevel.WARN
        var isLoggerEnabled = false
        var isNeedFormatJson = false
        var requestTag: String = "LogHttpInfo"
        var responseTag: String = "LogHttpInfo"

        //虚拟接口发送
        var isMockEnabled = false
        var sleepMs: Long = 0
        var listener: BufferListener? = null


        fun addHeader(name: String, value: String): Builder {
            headers[name] = value
            return this
        }

        fun addDynamicHeader(name: String, func: () -> String): Builder {
            dyHeaders[name] = func
            return this
        }

        fun addQueryParam(name: String, value: String): Builder {
            queryParam[name] = value
            return this
        }

        fun addDynamicQueryParam(name: String, func: () -> String): Builder {
            dyQueryParam[name] = func
            return this
        }

        fun setRequestTag(tag: String): Builder {
            requestTag = tag
            return this
        }

        fun setResponseTag(tag: String): Builder {
            responseTag = tag
            return this
        }

        fun setEnableLogger(showLogger: Boolean): Builder {
            isLoggerEnabled = showLogger
            return this
        }

        fun setLogLevel(@LogLevel level: Int): Builder {
            isLoggerEnabled = true
            logLevel = level
            return this
        }

        fun setLogger(logger: Logger): Builder {
            isLoggerEnabled = true
            this.logger = logger
            return this
        }

        fun setFormatJson(needFormat: Boolean): Builder {
            isNeedFormatJson = needFormat
            return this
        }

        fun setMock(useMock: Boolean, sleep: Long, listener: BufferListener): Builder {
            isMockEnabled = useMock
            sleepMs = sleep
            this.listener = listener
            return this
        }

        fun build(): LoggingInterceptor {
            return LoggingInterceptor(this)
        }
    }
}