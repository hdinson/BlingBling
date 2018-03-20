package dinson.customview.http.manager

import dinson.customview.kotlin.times
import dinson.customview.kotlin.verbose
import okhttp3.Interceptor
import okhttp3.Response

/**
 *   @author Dinson - 2018/3/20
 */
class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        verbose("┌${"─".times(120)}", showLine = false)
        verbose("│ " + request.toString(), showLine = false)
        verbose("│ ${request.headers().toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }}", showLine = false)

        val t1 = System.nanoTime()
        val response = chain.proceed(chain.request())
        val t2 = System.nanoTime()

        verbose("│ Received response for in ${(t2 - t1) / 1e6}ms", showLine = false)
        verbose("│ ${response.headers().toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }}", showLine = false)

        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        verbose("│ $content", showLine = false)
        verbose("└${"─".times(120)}", showLine = false)
        return response.newBuilder()
            .body(okhttp3.ResponseBody.create(mediaType, content))
            .build()
    }
}