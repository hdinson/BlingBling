package dinson.customview.http.manager;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import dinson.customview.utils.LogUtils;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 自定义响应ResponseBody
 */
public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;

    /**
     * 构造器
     */
    public JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    /**
     * 转换
     *
     * @param responseBody
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        String response = responseBody.string();
        LogUtils.d("│ response body:" + response,false);
        LogUtils.d("└────────────────────────────────────────────────────────────────────────────────────────────────────────────────",false);
        return adapter.fromJson(response);
    }
}