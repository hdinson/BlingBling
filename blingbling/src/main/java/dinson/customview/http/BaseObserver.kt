package dinson.customview.http

import android.net.ParseException
import android.system.ErrnoException
import com.google.gson.JsonParseException
import dinson.customview.kotlin.loge
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.OnErrorNotImplementedException
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * @author Dinson - 2017/7/25
 */
abstract class BaseObserver<T> : Observer<T> {
    override fun onSubscribe(d: Disposable) {}
    override fun onNext(value: T) {
        onHandlerSuccess(value)
        onFinal()
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        loge(false) { "onError() called with: [$e]" }
        onFinal()
    }

    override fun onComplete() {}
    abstract fun onHandlerSuccess(value: T)

    /**
     * 不管网络是否请求成功或者失败都会回调
     */
    fun onFinal() {}


}

fun Throwable.getShowMsg() = when (this) {
    is HttpException -> "${
    when (code()) {
        401 -> "文件未授权或证书错误"
        403 -> "服务器拒绝请求"
        404 -> "服务器找不到请求的文件"
        408 -> "请求超时，服务器未响应"
        500 -> "服务器内部错误）服务器遇到错误，无法完成请求。"
        502 -> "服务器从上游服务器收到无效响应。"
        503 -> "服务器目前无法使用"
        504 -> "服务器从上游服务器获取数据超时"
        else -> "服务器错误"
    }
    }，错误码：${code()}"
    is ParseException -> "json格式错误"
    is JSONException -> "json解析错误"
    is JsonParseException -> "json参数错误"
    is SSLHandshakeException -> "证书验证失败"
    is SocketTimeoutException -> "连接超时"
    is UnknownHostException -> "网络链接失败"
    is ErrnoException -> "网络不可访问"
    is OnErrorNotImplementedException -> cause?.message ?: message ?: "未知错误"
    else -> message ?: "未知错误"
}