package dinson.customview.api


import dinson.customview.entity.gank.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

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

//https://api.mmzztt.com/wp-json/wp/v2/rand?page=1

    //https://mztapi.mmzztt.com/data/api.json?meizitu
    /**
     * 获取妹子图域名
     */

    @GET("https://mztapi.mmzztt.com/data/api.json?meizitu")
    fun getMeizituUrl(): Observable<MeiZiTuUrl>

    /**
     *获取专题
     */
    @Headers("Referer:https://app.mmzztt.com")
    fun getZUrl(@Url url: String): Observable<ResponseBody>

    /**
     * 搜索关键字
     */
    @GET("https://api.mmzztt.com/wp-json/wp/v2/posts")
    @Headers("Referer:https://app.mmzztt.com")
    fun searchByTags(@Query("tags") tags: String): Observable<ArrayList<MeiZiTuPicSet>>

    /**
     * 搜索关键字
     */
    @GET("https://api.mmzztt.com/wp-json/wp/v2/posts")
    @Headers("Referer:https://app.mmzztt.com")
    fun searchByKey(@Query("search") search: String): Observable<ArrayList<MeiZiTuPicSet>>

    /**
     * 获取妹子图精选图片
     */
    @GET("https://api.mmzztt.com/wp-json/wp/v2/rand")
    @Headers("Referer:https://app.mmzztt.com")
    fun meizituTopPic(@Query("page") page: Int): Observable<ArrayList<MeiZiTuPicSet>>


    /**
     * 获取妹子图最新图片
     */
    @GET("https://api.mmzztt.com/wp-json/wp/v2/posts")
    @Headers("Referer:https://app.mmzztt.com")
    fun meizituNewPic(@Query("page") page: Int): Observable<ArrayList<MeiZiTuPicSet>>

    /**
     * 获取妹子图最热图片
     */
    @GET("https://api.mmzztt.com/wp-json/wp/v2/rank")
    @Headers("Referer:https://app.mmzztt.com")
    fun meizituHotPic(@Query("page") page: Int): Observable<ArrayList<MeiZiTuPicSet>>

    /**
     * 获取妹子图自拍图片
     */
    @GET("https://api.mmzztt.com/wp-json/wp/v2/comments?post=3238&per_page=45")
    @Headers("Referer:https://app.mmzztt.com")
    fun meizituSelfiePic(@Query("page") page: Int): Observable<ArrayList<MeiZiTuNinePic>>

    /**
     * 获取妹子图街拍图片
     */
    @GET("https://api.mmzztt.com/wp-json/wp/v2/comments?post=174285&per_page=20")
    @Headers("Referer:https://app.mmzztt.com")
    fun meizituStreetPic(@Query("page") page: Int): Observable<ArrayList<MeiZiTuNinePic>>

    /**
     * 根据id获取图集
     */
    //https://app.mmzztt.com/wp-json/wp/v2/i?id=197982
    @GET("https://api.mmzztt.com/wp-json/wp/v2/i")
    @Headers("Referer:https://app.mmzztt.com",
        "Cache-Control:public,max-age=17200")
    fun loadPicSetById(@Query("id") id: String): Observable<AesContent>


    /**
     * 获取福利
     */
    @GET("$SERVER_URL/random/data/福利/{size}")
    fun randomLadyPic(@Path("size") size: Int): Observable<GankRoot<ArrayList<Welfare>>>


    /**
     * 掘金获取flutter专栏
     */
    @GET("https://timeline-merger-ms.juejin.im/v1/get_tag_entry?src=web&sort=createdAt&pageSize=20")
    fun loadJueJinArticle(@Query("tagId") tagId: String, @Query("page") page: Int)
        : Observable<JueJinResponse>
}
