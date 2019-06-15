package dinson.customview.api


import dinson.customview.entity.countdown.IDailyNews
import dinson.customview.entity.countdown.OnTheDay
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import java.util.*


interface DaysMatterApi {
    /**
     * 每日要闻
     */
    @Headers("Cache-Control:public,max-age=7200")
    @GET("http://idaily-cdn.appcloudcdn.com/api/list/v3/android_mini/zh-hans?ver=android&app_ver=36&page=1")
    fun loadDailyNews(): Observable<ArrayList<IDailyNews>>

    /**
     * 同日事件
     */
    @GET("http://alpha.daysmatter.com/app/idays/on_this_day?lang=zh_hans&vendor=xiaomi&app_ver=34")
    fun loadOnThisDays(): Observable<HashMap<String, ArrayList<OnTheDay>>>
}
