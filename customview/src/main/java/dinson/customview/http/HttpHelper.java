package dinson.customview.http;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit统一封装设置,返回retrofit对象
 */
public class HttpHelper {


    private static final int DEFAULT_TIMEOUT = 10;

    private Retrofit retrofit;

    //构造方法私有
    private HttpHelper() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor());//添加拦截器 日志

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())//对http请求结果进行统一的预处理
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//对rxjava提供支持
                .baseUrl("http://192.168.8.8")
                .build();
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpHelper INSTANCE = new HttpHelper();
    }

    //获取Retrofit
    public static Retrofit getRetrofit() {
        return SingletonHolder.INSTANCE.retrofit;
    }
}
