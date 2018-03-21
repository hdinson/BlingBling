package dinson.customview.http;


import java.io.File;
import java.util.concurrent.TimeUnit;

import dinson.customview._global.ConstantsUtils;
import dinson.customview.http.manager.AuthenticatorManager;
import dinson.customview.http.manager.CookieManagerOld;
import dinson.customview.http.manager.CookieManger;
import dinson.customview.http.manager.JsonConverterFactory;
import dinson.customview.http.manager.LoggingInterceptor;
import dinson.customview.utils.UIUtils;
import okhttp3.Cache;
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
        mOkHttpClient = new OkHttpClient.Builder()
            .cache(new Cache(new File(ConstantsUtils.SDCARD_PRIVATE_CACHE, "http_cache"), 1024 * 1024 * 100))
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(new LoggingInterceptor())
            .cookieJar(new CookieManger(UIUtils.getContext()))
            .authenticator(new AuthenticatorManager())
            .build();
        mRetrofit = new Retrofit.Builder()
            .addConverterFactory(JsonConverterFactory.create())//对http请求结果进行统一的预处理
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//对rxJava提供支持
            .baseUrl("http://192.168.8.8")
            .client(mOkHttpClient)
            .build();
    }

    public static Retrofit getRetrofit() {
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

    public static void clearCookie() {
        ((CookieManagerOld) mOkHttpClient.cookieJar()).clearCookie();
    }
}
