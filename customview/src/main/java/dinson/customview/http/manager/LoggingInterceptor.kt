package dinson.customview.http.manager

import dinson.customview.kotlin.times
import dinson.customview.kotlin.verbose
import dinson.customview.utils.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 *  日志拦截器+缓存策略
 *  缓存策略：有网络时，根据Cache-Control决定要不要网络请求
 *           无网络时，从本地缓存取数据
 */
class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response? {

        verbose("┌${"─".times(120)}", showLine = false)
        verbose("│ Request{method= ${chain.request().method()}, url=${chain.request().url()}}", showLine = false)

        val request = when {
            NetworkUtils.isNetworkAvailable() -> chain.request()
            else -> {
                verbose("│ Loaded From LocalCache - Network error", showLine = false)
                //强制使用缓存
                chain.request().newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            }
        }
        val t1 = System.nanoTime()
        val response = chain.proceed(request)
        val t2 = System.nanoTime()

        verbose("│ Received response for in ${(t2 - t1) / 1e6}ms", showLine = false)

        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        if (content.isNotEmpty()) verbose("│ ${content.replace("\n","")}", showLine = false)
        verbose("└${"─".times(120)}", showLine = false)
        return response.newBuilder().removeHeader("Pragma")
            .body(okhttp3.ResponseBody.create(mediaType, content)).build()
    }
}