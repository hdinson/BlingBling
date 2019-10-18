package dinson.customview.api


import dinson.customview.entity.gank.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 干货集中营的开放api
 */
interface GankApi {

    companion object {
        const val SERVER_URL = "http://gank.io/api"

    }

    /**
     * 获取GankIO福利
     */
    @Headers("Cache-Control:public,max-age=7200")
    @GET("$SERVER_URL/data/福利/{size}/{page}")
    fun gankIOGirlsPic(@Path("size") size: Int, @Path("page") page: Int)
        : Observable<GankRoot<ArrayList<Welfare>>>


    /**
     * 获取妹子图域名
     */
    @GET("https://mztapi.mmzztt.com/data/api.json?meizitu")
    fun getMeizituUrl(): Observable<MeiZiTuUrl>

    /**
     *获取专题
     */
    @GET("https://api.meizitu.net/json/x.json")
    @Headers("Referer:https://app.mmzztt.com")
    fun gettext(): Observable<ResponseBody>

    /**
     * 获取妹子图精选图片
     */
    @GET("https://api.meizitu.net/wp-json/wp/v2/rand")
    @Headers("Referer:https://app.mmzztt.com")
    fun meizituTopPic(@Query("page") page: Int): Observable<ArrayList<MeiZiTuPicSet>>


    /**
     * 获取妹子图最新图片
     */
    @GET("https://api.meizitu.net/wp-json/wp/v2/posts")
    @Headers("Referer:https://app.mmzztt.com")
    fun meizituNewPic(@Query("page") page: Int): Observable<ArrayList<MeiZiTuPicSet>>

    /**
     * 获取妹子图最热图片
     */
    @GET("https://api.meizitu.net/wp-json/wp/v2/rank")
    @Headers("Referer:https://app.mmzztt.com")
    fun meizituHotPic(@Query("page") page: Int): Observable<ArrayList<MeiZiTuPicSet>>

    /**
     * 获取妹子图自拍图片
     */
    @GET("https://api.meizitu.net/wp-json/wp/v2/comments?post=3238&per_page=45")
    @Headers("Referer:https://app.mmzztt.com")
    fun meizituSelfiePic(@Query("page") page: Int): Observable<ArrayList<MeiZiTuNinePic>>

    /**
     * 获取妹子图街拍图片
     */
    @GET("https://api.meizitu.net/wp-json/wp/v2/comments?post=174285&per_page=20")
    @Headers("Referer:https://app.mmzztt.com")
    fun meizituStreetPic(@Query("page") page: Int): Observable<ArrayList<MeiZiTuNinePic>>

    /**
     * 根据id获取图集
     */
    @GET("https://ios.meizitu.net/wp-json/wp/v2/i")
    @Headers("Referer:https://app.mmzztt.com",
        "Cache-Control:public,max-age=17200")
    fun loadPicSetById(@Query("id") id: String): Observable<AesContent>


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

    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     *  请求个数： 数字，大于0
     *  第几页：数字，大于0
     */


}
