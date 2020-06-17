package dinson.customview.http.manager

import android.content.Context
import android.net.ConnectivityManager
import dinson.customview._global.GlobalApplication
import com.dinson.blingbase.kotlin.logv
import com.dinson.blingbase.kotlin.times
import com.dinson.blingbase.utils.RxBling
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.net.URLDecoder

/**
 *  日志拦截器+缓存策略
 *  缓存策略：有网络时，根据Cache-Control决定要不要网络请求
 *           无网络时，从本地缓存取数据
 */
class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response? {

        logv("\t┌${"─".times(120)}", showLine = false)
        chain.request().
        logv("\t│ Request{method= ${chain.request().method()}, url=${chain.request().url()}}", showLine = false)

        val request = when {
            isNetworkAvailable() -> chain.request()
            else -> {
                logv("\t│ Loaded From LocalCache - Network error", showLine = false)
                //强制使用缓存
                chain.request().newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            }
        }
        request.body()?.apply {
            logv("\t│ Body: ${Buffer().let {
                this.writeTo(it)
                URLDecoder.decode(it.readUtf8(),"UTF-8")
            }}", showLine = false)
        }
        val t1 = System.nanoTime()
        val response = chain.proceed(request)
        val t2 = System.nanoTime()

        logv("\t│ Received response for in ${(t2 - t1) / 1e6}ms", showLine = false)

        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        if (content.isNotEmpty()) logv("\t│ ${content.replace("\n", "")}", showLine = false)
        logv("\t└${"─".times(120)}", showLine = false)
        return response.newBuilder().removeHeader("Pragma")
            .body(okhttp3.ResponseBody.create(mediaType, content)).build()
    }


    /**
     * 判断网络是否连接
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    private fun isNetworkAvailable(): Boolean {
        val info = (RxBling.context.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager).activeNetworkInfo
        return info != null && info.isConnected
    }

    /**
     *   字符串相关的扩展方法
     */
    fun String.times(count: Int): String {
        return (1..count).fold(StringBuilder()) { acc, _ ->
            acc.append(this)
        }.toString()
    }
}


