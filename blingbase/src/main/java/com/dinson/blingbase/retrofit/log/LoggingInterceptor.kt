package com.dinson.blingbase.retrofit.log

import okhttp3.*
import okhttp3.MediaType.parse
import okhttp3.internal.platform.Platform.INFO
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * @author ihsan on 09/02/2017.
 */
class LoggingInterceptor private constructor(private val builder: Builder) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = addQueryAndHeaders(chain.request())

        printlnRequestLog(request)

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = proceedResponse(chain, request)
        } catch (e: Exception) {
            Printer.printFailed(builder.getTag(false), builder)
            throw e
        }
        val receivedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        printlnResponseLog(receivedMs, response, request)
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
        builder.headers.keys.forEach { key ->
            builder.headers[key]?.let {
                requestBuilder.addHeader(key, it)
            }
        }
        val httpUrlBuilder: HttpUrl.Builder? = request.url().newBuilder(request.url().toString())
        httpUrlBuilder?.let {
            builder.httpUrl.keys.forEach { key ->
                httpUrlBuilder.addQueryParameter(key, builder.httpUrl[key])
            }
        }
        return requestBuilder.url(httpUrlBuilder?.build()!!).build()
    }

    @Suppress("unused")
    class Builder {
        val headers: HashMap<String, String> = HashMap()
        val httpUrl: HashMap<String, String> = HashMap()

        var level: Int = INFO
            private set
        private var requestTag: String = TAG
        private var responseTag: String = TAG

        var logger: Logger = Logger.DEFAULT
            private set

        //虚拟接口发送
        var isMockEnabled = false
        var isNeedFormatJson = false
        var sleepMs: Long = 0
        var listener: BufferListener? = null

        fun getTag(isRequest: Boolean): String {
            return when (isRequest) {
                true -> if (requestTag.isEmpty()) TAG else requestTag
                false -> if (responseTag.isEmpty()) TAG else responseTag
            }
        }

        /**
         * @param name  Filed
         * @param value Value
         * @return Builder
         * Add a field with the specified value
         */
        fun addHeader(name: String, value: String): Builder {
            headers[name] = value
            return this
        }

        /**
         * @param name  Filed
         * @param value Value
         * @return Builder
         * Add a field with the specified value
         */
        fun addQueryParam(name: String, value: String): Builder {
            httpUrl[name] = value
            return this
        }

        /**
         * Set request and response each log tag
         *
         * @param tag general log tag
         * @return Builder
         */
        fun tag(tag: String): Builder {
            TAG = tag
            return this
        }

        /**
         * Set request log tag
         *
         * @param tag request log tag
         * @return Builder
         */
        fun requestTag(tag: String): Builder {
            requestTag = tag
            return this
        }

        /**
         * Set response log tag
         *
         * @param tag response log tag
         * @return Builder
         */
        fun responseTag(tag: String): Builder {
            responseTag = tag
            return this
        }


        /**
         * @param level set sending log output type
         * @return Builder
         */
        fun log(level: Int): Builder {
            this.level = level
            return this
        }

        /**
         * @param logger manuel logging interface
         * @return Builder
         * @see Logger
         */
        fun logger(logger: Logger): Builder {
            this.logger = logger
            return this
        }

        fun formatJson(needFormat: Boolean): Builder {
            this.isNeedFormatJson = needFormat
            return this
        }


        /**
         * @param useMock let you use json file from asset
         * @param sleep   let you see progress dialog when you request
         * @return Builder
         * @see LoggingInterceptor
         */
        fun enableMock(useMock: Boolean, sleep: Long, listener: BufferListener?): Builder {
            isMockEnabled = useMock
            sleepMs = sleep
            this.listener = listener
            return this
        }


        fun build(): LoggingInterceptor {
            return LoggingInterceptor(this)
        }

        companion object {
            private var TAG = "LogHttpInfo"
        }
    }
}