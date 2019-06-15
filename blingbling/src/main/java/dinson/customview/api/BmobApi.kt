package dinson.customview.api

import dinson.customview.BuildConfig
import dinson.customview.entity.bmob.TvShowResp
import dinson.customview.entity.exchange.ExchangeBean
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 *  bmob的API
 */
interface BmobApi {
    /**
     * 获取所有的电视节目
     */
    @Headers("X-Bmob-Application-Id: ${BuildConfig.BMOB_APPID}",
        "X-Bmob-REST-API-Key:${BuildConfig.BMOB_REST_API_KEY}")
    @GET("https://api.bmob.cn/1/classes/TvShow?order=showOrder")
    fun getVideoList(): Observable<TvShowResp>

}