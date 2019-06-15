package dinson.customview.http.manager

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * 自定义响应ResponseBody
 */
class JsonResponseBodyConverter<T>(gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

    /**
     * 转换
     */
    override fun convert(responseBody: ResponseBody): T {
        return adapter.fromJson(responseBody.string())
    }
}