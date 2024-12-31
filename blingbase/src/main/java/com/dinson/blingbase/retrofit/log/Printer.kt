package com.dinson.blingbase.retrofit.log

import com.dinson.blingbase.kotlin.times
import okhttp3.Headers
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import okio.GzipSource
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.EOFException
import java.io.IOException
import java.lang.Exception
import java.nio.charset.Charset

class Printer private constructor() {
    companion object {
        private const val JSON_INDENT = 3
        private val LINE_SEPARATOR = System.getProperty("line.separator") ?: "\n"
        private val UP_LINE = "┌${"─".times(100)}"
        private val END_LINE = "└${"─".times(100)}"

        private const val DEFAULT_LINE = "│ "
        private val OOM_OMITTED = LINE_SEPARATOR + "Output omitted because of Object size."
        private fun isEmpty(line: String): Boolean {
            return line.isEmpty() || "\n" == line || "\t" == line || line.trim { it <= ' ' }.isEmpty()
        }

        fun printJsonRequest(builder: LoggingInterceptor.Builder, body: RequestBody?, url: String, headers: Headers, method: String) {
            val tag = builder.requestTag
            if (!isEmpty(headers.toString()) || body != null) {
                builder.logger.log(builder.logLevel, tag, UP_LINE)////上线
                builder.logger.log(builder.logLevel, tag, "$DEFAULT_LINE--> $method $url")  //url行
                if (!isEmpty(headers.toString())) {
                    //打印headers
                    val headersArr = dotHeaders(headers).split(LINE_SEPARATOR).toTypedArray()
                    logLines(builder.logger, builder.logLevel, tag, headersArr)
                }
                if (body != null) {
                    val requestBody = if (builder.isNeedFormatJson) bodyToString(body, headers) else {
                        Buffer().let {
                            body.writeTo(it)
                            it.readUtf8()
                        }
                    }

                    builder.logger.log(builder.logLevel, tag, LINE_SEPARATOR)//换行
                    logLines(builder.logger, builder.logLevel, tag, requestBody.split(LINE_SEPARATOR).toTypedArray())
                    val bodySize = "(${body.contentLength()}-byte body)"
                    builder.logger.log(builder.logLevel, tag, "$DEFAULT_LINE--> END $method $bodySize")
                }
                builder.logger.log(builder.logLevel, tag, END_LINE)
            } else {
                builder.logger.log(builder.logLevel, tag, "$DEFAULT_LINE--> $method $url")  //url行
            }
        }

        fun printJsonResponse(builder: LoggingInterceptor.Builder, chainMs: Long,
                              code: Int, headers: Headers, response: Response,
                              message: String, responseUrl: String, isHttps: Boolean) {

            val tag = builder.responseTag

            builder.logger.log(builder.logLevel, tag, UP_LINE)////上线
            //  <-- 200 OK http://op.juhe.cn/onebox/movie/video (760ms)
            builder.logger.log(builder.logLevel, tag, "$DEFAULT_LINE<-- $code $message $responseUrl (${chainMs}ms)")//url行

            if (!isEmpty(headers.toString())) {
                //打印headers
                val headersArr = dotHeaders(headers).split(LINE_SEPARATOR).toTypedArray()
                logLines(builder.logger, builder.logLevel, tag, headersArr)
            }

            if (response.body() != null) {
                val responseBody = LINE_SEPARATOR + getResponseBody(builder, response)
                logLines(builder.logger, builder.logLevel, tag, responseBody.split(LINE_SEPARATOR).toTypedArray())
            }

            builder.logger.log(builder.logLevel, tag, "$DEFAULT_LINE<-- END ${if (isHttps) "HTTPS" else "HTTP"}")
            builder.logger.log(builder.logLevel, tag, END_LINE)

        }

        private fun getResponseBody(builder: LoggingInterceptor.Builder, response: Response): String {
            val responseBody = response.body()
            val headers = response.headers()
            val contentLength = responseBody?.contentLength()
            when {
                bodyHasUnknownEncoding(response.headers()) -> {
                    return "encoded body omitted"
                }
                responseBody != null -> {

                    val source = responseBody.source()

                    source.request(Long.MAX_VALUE) // Buffer the entire body.
                    var buffer = source.buffer()

                    var gzippedLength: Long? = null
                    if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                        gzippedLength = buffer.size()
                        GzipSource(buffer.clone()).use { gzippedResponseBody ->
                            buffer = Buffer()
                            buffer.writeAll(gzippedResponseBody)
                        }
                    }

                    if (!buffer.isProbablyUtf8()) {
                        return "End request - binary ${buffer.size()}:byte body omitted"
                    }

                    if (contentLength != 0L) {
                        return if (builder.isNeedFormatJson) {
                            getJsonString(buffer.clone().readString(Charset.forName("UTF-8")))
                        } else buffer.clone().readString(Charset.forName("UTF-8"))
                    }

                    return if (gzippedLength != null) {
                        "End request - ${buffer.size()}:byte, $gzippedLength-gzipped-byte body"
                    } else {
                        "End request - ${buffer.size()}:byte body"
                    }
                }
                else -> return "Body is null by Dinson edit !"
            }
        }


        private fun dotHeaders(headers: Headers): String {
            val builder = StringBuilder()
            headers.names().forEach {
                builder.append("$it: ${headers[it]}").append(LINE_SEPARATOR)
            }
            return builder.dropLast(1).toString()
        }


        private fun logLines(logger: Logger?, level: Int, tag: String, lines: Array<String>) {
            lines.forEach { line ->
                val lineLength = line.length
                val maxLogSize = 110
                for (i in 0..lineLength / maxLogSize) {
                    val start = i * maxLogSize
                    var end = (i + 1) * maxLogSize
                    end = if (end > line.length) line.length else end

                    logger?.log(level, tag, DEFAULT_LINE + line.substring(start, end))
                }
            }
        }

        private fun bodyToString(requestBody: RequestBody?, headers: Headers): String {
            return requestBody?.let {
                return try {
                    return when {
                        bodyHasUnknownEncoding(headers) -> {
                            "encoded body omitted)"
                        }
                        else -> {
                            val buffer = Buffer()
                            requestBody.writeTo(buffer)

                            if (buffer.isProbablyUtf8()) {
                                getJsonString(buffer.readString(Charset.forName("UTF-8"))) + LINE_SEPARATOR + "${requestBody.contentLength()}-byte body"
                            } else {
                                "binary ${requestBody.contentLength()}-byte body omitted"
                            }
                        }
                    }
                } catch (e: IOException) {
                    "{\"err\": \"" + e.message + "\"}"
                }
            } ?: ""
        }

        private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
            val contentEncoding = headers["Content-Encoding"] ?: return false
            return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
        }

        private fun getJsonString(msg: String): String {
            val message: String
            message = try {
                when {
                    msg.startsWith("{") -> {
                        val jsonObject = JSONObject(msg)
                        jsonObject.toString(JSON_INDENT)
                    }
                    msg.startsWith("[") -> {
                        val jsonArray = JSONArray(msg)
                        jsonArray.toString(JSON_INDENT)
                    }
                    else -> {
                        msg
                    }
                }
            } catch (e: JSONException) {
                msg
            } catch (e1: OutOfMemoryError) {
                OOM_OMITTED
            }
            return message
        }

        fun printFailed(builder: LoggingInterceptor.Builder, e: Exception) {
            builder.logger.log(builder.logLevel, builder.responseTag, UP_LINE)
            builder.logger.log(builder.logLevel, builder.responseTag, DEFAULT_LINE + "Response failed : ${e.message}")
            builder.logger.log(builder.logLevel, builder.responseTag, END_LINE)
        }
    }

    init {
        throw UnsupportedOperationException()
    }
}

internal fun Buffer.isProbablyUtf8(): Boolean {
    try {
        val prefix = Buffer()
        val byteCount = size().coerceAtMost(64)
        copyTo(prefix, 0, byteCount)
        for (i in 0 until 16) {
            if (prefix.exhausted()) {
                break
            }
            val codePoint = prefix.readUtf8CodePoint()
            if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                return false
            }
        }
        return true
    } catch (_: EOFException) {
        return false // Truncated UTF-8 sequence.
    }
}