package dinson.customview.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

/**
 * 测试api
 */
interface TestApi {
    @GET("http://p2c0m2mi6.bkt.clouddn.com/cntv.txt")
    fun getCntvText()


}