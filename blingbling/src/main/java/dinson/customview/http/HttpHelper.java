package dinson.customview.http;


import com.dinson.blingbase.retrofit.log.LogLevel;
import com.dinson.blingbase.retrofit.log.LoggingInterceptor;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dinson.customview._global.ConstantsUtils;
import dinson.customview.http.manager.CookieManager;
import dinson.customview.http.manager.JsonConverterFactory;
import dinson.customview.utils.LogUtils;
import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Retrofit统一封装设置,返回retrofit对象
 */
public class HttpHelper {

    private static final int DEFAULT_TIMEOUT = 10;

    private static HttpHelper sHttpHelper;
    private static Retrofit mRetrofit;
    private static OkHttpClient mOkHttpClient;

    private HttpHelper() {

        //打印网络请求日志
        LoggingInterceptor httpLoggingInterceptor = new LoggingInterceptor.Builder()
            .log(LogLevel.VERBOSE)
            .requestTag("Request")
            .responseTag("Response").build();

        mOkHttpClient = new OkHttpClient.Builder()
            .authenticator((route, response) -> {
                LogUtils.e("Authenticating for response: " + response);
                LogUtils.e("Challenges: " + response.challenges());
                String credential = Credentials.basic("jesse", "password1");
                return response.request().newBuilder()
                    .header("Authorization", credential)
                    .build();
            })
            .cache(new Cache(new File(ConstantsUtils.INSTANCE.getSDCARD_PRIVATE_CACHE(), "http_cache"),
                1024 * 1024 * 100))//100M的缓存
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .cookieJar(new CookieManager())
            //.authenticator(new AuthenticatorManager())
            .build();
        mRetrofit = new Retrofit.Builder()
            .addConverterFactory(JsonConverterFactory.Companion.create())//对http请求结果进行统一的预处理
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//对rxJava提供支持
            .baseUrl("http://192.168.8.8")
            .client(mOkHttpClient)
            .build();
    }

    private static Retrofit getRetrofit() {
        if (sHttpHelper == null) {
            synchronized (HttpHelper.class) {
                if (sHttpHelper == null) {
                    sHttpHelper = new HttpHelper();
                }
            }
        }
        return mRetrofit;
    }

    public static <T> T create(Class<T> tClass) {
        return getRetrofit().create(tClass);
    }

    /**
     * 清楚所有的Cookie
     */
    public static void clearAllCookie() {
        ((CookieManager) mOkHttpClient.cookieJar()).getCookieStore().removeAllCookies();
    }

    /**
     * 清楚所有的Cookie
     */
    public static void clearCookie(String domain) {
        ((CookieManager) mOkHttpClient.cookieJar()).getCookieStore().removeCookies(domain);
    }

    /**
     * 清楚所有的Cookie
     */
    public static List<Cookie> getCookie(String domain) {
        return ((CookieManager) mOkHttpClient.cookieJar()).getCookieStore().getCookies(domain);
    }
}
