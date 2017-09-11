package dinson.customview.http.manager;

import java.io.IOException;
import java.util.Locale;

import dinson.customview.utils.LogUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 拦截请求日志
 *
 * @author Dinson - 2017/7/25
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LogUtils.i("**********************************************************************");
        LogUtils.d(request.toString());
        String headers = formetStringArray(request.headers().toString().split("\n"));
        if (!headers.equals("[]")) LogUtils.d(headers);

        long t1 = System.nanoTime();
        okhttp3.Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();

        LogUtils.d(String.format(Locale.getDefault(), "Received response for in %.1fms", (t2 - t1) / 1e6d));
        LogUtils.d(formetStringArray(response.headers().toString().split("\n")));

        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        LogUtils.d("response body:" + content);
        LogUtils.i("**********************************************************************");
        return response.newBuilder()
            .body(okhttp3.ResponseBody.create(mediaType, content))
            .build();
    }


    private String formetStringArray(String[] arr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : arr) {
            String[] split = s.split(":", 2);
            String date = split.length == 2 ? split[1].trim() : split[0].trim();
            stringBuffer.append("[" + date + "]");
        }
        return stringBuffer.toString();
    }

}
