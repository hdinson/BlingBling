package dinson.customview.http.manager

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * 自定义 json转换器
 */
class JsonConverterFactory(private val gson: Gson) : Converter.Factory() {

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return JsonRequestBodyConverter(gson, adapter) //请求
    }

    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return JsonResponseBodyConverter(gson, adapter) //响应
    }

    companion object {
        @JvmOverloads
        fun create(gson: Gson = Gson()): JsonConverterFactory {
            return JsonConverterFactory(gson)
        }
    }
}

