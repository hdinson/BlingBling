package dinson.customview.api

import dinson.customview.entity.HomeWeather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 知心天气API
 */
interface XinZhiWeatherApi {

    /*https://api.seniverse.com/v3/weather/now.json?key=prusvbyhfmheej3x&location=24.879364:118.643059&language=zh-Hans&unit=c*/
    @GET("https://api.seniverse.com/v3/weather/now.json?key=prusvbyhfmheej3x&language=zh-Hans&unit=c")
    fun getWeather(@Query("location") location: String): Observable<HomeWeather>
}
