package dinson.customview.api

import dinson.customview.entity.exchange.ExchangeBean
import io.reactivex.Observable
import retrofit2.http.GET 

/**
 *  汇率兑换接口
 */
interface ExchangeApi{
    @GET("https://openexchangerates.org/api/latest.json?app_id=cf7a69aa7d174a4e8528a6a92b107903")
    fun getRate():Observable<ExchangeBean>

}