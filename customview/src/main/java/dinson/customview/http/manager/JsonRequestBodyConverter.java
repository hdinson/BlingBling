package dinson.customview.http.manager;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import dinson.customview.utils.LogUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * 自定义请求RequestBody
 */
public class JsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private final TypeAdapter<T> adapter;

    /**
     * 构造器
     */
    public JsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        LogUtils.i("POST: " + value.toString(),false);
        String postBody = adapter.toJson(value);//对象转化成json
        return RequestBody.create(MEDIA_TYPE, postBody);
    }
}
