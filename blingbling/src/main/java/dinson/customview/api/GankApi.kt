package dinson.customview.api


import dinson.customview.entity.gank.GankRoot
import dinson.customview.entity.gank.GankToday
import dinson.customview.entity.gank.ProjectsInfo
import dinson.customview.entity.gank.Welfare
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * 干货集中营的开放api
 */
interface GankApi {

    companion object {
        const val SERVER_URL = "http://gank.io/api"
    }

    /**
     * 获取福利
     */
    @Headers("Cache-Control:public,max-age=7200")
    @GET("$SERVER_URL/data/福利/{size}/{page}")
    fun ladyPic(@Path("size") size: Int, @Path("page") page: Int)
        : Observable<GankRoot<ArrayList<Welfare>>>

    /**
     * 获取福利
     */
    @GET("$SERVER_URL/random/data/福利/{size}")
    fun randomLadyPic(@Path("size") size: Int): Observable<GankRoot<ArrayList<Welfare>>>


    /**
     * 获取今天信息
     */
    @Headers("Cache-Control:public,max-age=7200")
    @GET("$SERVER_URL/today")
    fun loadTodayData(): Observable<GankToday>

    /**
     * 获取福利
     */
    @Headers("Cache-Control:public,max-age=7200")
    @GET("$SERVER_URL/data/Android/{size}/{page}")
    fun androidDateList(@Path("size") size: Int, @Path("page") page: Int)
        : Observable<GankRoot<ArrayList<ProjectsInfo>>>


}
