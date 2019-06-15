package dinson.customview.http.manager

import dinson.customview.kotlin.logv
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

/**
 *  日志拦截器+缓存策略
 *  缓存策略：有网络时，根据Cache-Control决定要不要网络请求
 *           无网络时，从本地缓存取数据
 */
class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response? {
        //缓存，如果忽略服务器处理，自己定义
        val cacheControl = chain.request().cacheControl().toString()
        val request = chain.request().newBuilder().removeHeader("Accept-Encoding").build()
        val response = chain.proceed(request)
        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()

        //输出请求头
        if (request.headers().size() != 0) {
            logv("\t│ ${chain.request().headers().toString().split("\n")
                .dropLastWhile { it.isEmpty() }}", showLine = false)
        }

        logv("\t│ Loaded From Network - Cache-Control: ${if (cacheControl.isEmpty()) "None"
        else cacheControl}", showLine = false)

        //输出响应头
        if (response.headers().size() != 0) {
            logv("\t│ ${response.headers().toString().split("\n")
                .dropLastWhile { it.isEmpty() }}", showLine = false)
        }

        return response.newBuilder().removeHeader("Pragma")
            .removeHeader("Vary")//汇率兑换响应头的坑，导致cacheControl无效，原因未知
            .header("Cache-Control", cacheControl)
            .body(ResponseBody.create(mediaType, content)).build()
    }
}
