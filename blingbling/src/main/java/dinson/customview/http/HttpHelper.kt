package dinson.customview.http

import com.dinson.blingbase.retrofit.log.LogLevel
import com.dinson.blingbase.retrofit.log.LoggingInterceptor
import com.dinson.blingbase.retrofit.manager.RxCookieManager
import dinson.customview._global.ConstantsUtils.SDCARD_PRIVATE_CACHE
import dinson.customview.http.manager.JsonConverterFactory.Companion.create
import dinson.customview.kotlin.logi
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Retrofit统一封装设置,返回retrofit对象
 */
object HttpHelper {

    private const val DEFAULT_TIMEOUT = 10  //超时时间


    //打印网络请求日志
    private val httpLoggingInterceptor = LoggingInterceptor.Builder()
        .setLogLevel(LogLevel.VERBOSE)
        .setRequestTag("Request")
        .setResponseTag("Response")
        .addHeader("ddd", "dinson")
        .build()
    private var mOkHttpClient = OkHttpClient.Builder()
        .authenticator { route: Route?, response: Response ->
            logi { "Authenticating for response: $response" }
            logi { "Challenges: " + response.challenges() }
            val credential = Credentials.basic("jesse", "password1")
            response.request().newBuilder()
                .header("Authorization", credential)
                .build()
        }
        .cache(Cache(File(SDCARD_PRIVATE_CACHE, "http_cache"),
            1024 * 1024 * 100)) //100M的缓存
        .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .cookieJar(RxCookieManager()) //.authenticator(new AuthenticatorManager())
        .build()
    private var mRetrofit = Retrofit.Builder()
        .addConverterFactory(create()) //对http请求结果进行统一的预处理
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //对rxJava提供支持
        .baseUrl("http://192.168.8.8")
        .client(mOkHttpClient)
        .build()

    fun <T> create(tc: Class<T>): T {
        return mRetrofit.create(tc)
    }

    /**
     * 清楚所有的Cookie
     */
    fun clearAllCookie() {
        (mOkHttpClient.cookieJar() as RxCookieManager).getCookieStore().removeAllCookies()
    }

    /**
     * 清楚所有的Cookie
     */
    fun clearCookie(domain: String?) {
        (mOkHttpClient.cookieJar() as RxCookieManager).getCookieStore().removeCookies(domain!!)
    }

    /**
     * 清楚所有的Cookie
     */
    fun getCookie(domain: String?): List<Cookie> {
        return (mOkHttpClient.cookieJar() as RxCookieManager).getCookieStore().getCookies(domain!!)
    }
}
