package dinson.customview.http;


import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import dinson.customview.utils.LogUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    /**
     * 请求日志
     */
    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            LogUtils.i("====================================================================");
            LogUtils.i(request.toString());
            LogUtils.i(request.headers().toString());
            long t1 = System.nanoTime();
            okhttp3.Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
            LogUtils.i(String.format(Locale.getDefault(), "Received response in %.1fms", (t2 - t1) / 1e6d));
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            LogUtils.i("response body:" + content);
            LogUtils.i("====================================================================");
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    }
}
