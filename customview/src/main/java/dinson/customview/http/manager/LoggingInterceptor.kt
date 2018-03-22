package dinson.customview.http.manager

import dinson.customview.kotlin.then
import dinson.customview.kotlin.times
import dinson.customview.kotlin.verbose
import dinson.customview.utils.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 *  日志拦截器
 */
class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        verbose("┌${"─".times(120)}", showLine = false)
        verbose("│ ${chain.request()}", showLine = false)
        //verbose("│ ${request.headers().toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }}", showLine = false)
        val request = when {
            NetworkUtils.isNetworkAvailable() -> chain.request()
            else -> {
                verbose("│ Network error, then loading cache", showLine = false)
                chain.request().newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            }
        }

        val t1 = System.nanoTime()
        val response = chain.proceed(request)
        val t2 = System.nanoTime()

        verbose("│ Received response for in ${(t2 - t1) / 1e6}ms", showLine = false)
        (response.headers().size() != 0) then {
            verbose("│ ${response.headers().toString().split("\n")}", showLine = false)
        }

        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        if (content.isNotEmpty()) verbose("│ $content", showLine = false)
        verbose("└${"─".times(120)}", showLine = false)
        return response.newBuilder()
            .body(okhttp3.ResponseBody.create(mediaType, content))
            .build()
    }
}