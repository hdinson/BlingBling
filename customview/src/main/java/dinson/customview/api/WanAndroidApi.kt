package dinson.customview.api


import dinson.customview.entity.wanandroid.WanAndArticleResponse
import dinson.customview.entity.wanandroid.WanAndroidBaseResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 玩安卓的开放api
 */

interface WanAndroidApi {
    /**
     * 登陆
     */
    @POST("http://www.wanandroid.com/user/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String, @Field("password") password: String)
        : Observable<WanAndroidBaseResponse>

    /**
     * 注册
     */
    @POST("http://www.wanandroid.com/user/register")
    @FormUrlEncoded
    fun register(@Field("username") username: String, @Field("password") password: String,
                 @Field("repassword") repassword: String): Observable<WanAndroidBaseResponse>

    /**
     * 获取首页所有最新的文章
     */
    @Headers("Cache-Control:public,max-age=20")
    @GET("http://www.wanandroid.com/article/list/{index}/json")
    fun getMainArticleList(@Path("index") pageIndex: Int): Observable<WanAndArticleResponse>

    /**
     * 获取收藏列表
     */
    @GET("http://www.wanandroid.com/lg/collect/list/0/json")
    fun collectList(): Observable<WanAndArticleResponse>

    /**
     * 添加收藏
     */
    @POST("http://www.wanandroid.com/lg/collect/{id}/json")
    fun addCollect(@Path("id") id: Int): Observable<WanAndroidBaseResponse>

    /**
     * 取消收藏（在文章列表取消收藏）
     * 文章列表和收藏列表的id不同
     */
    @POST("http://www.wanandroid.com/lg/uncollect_originId/{id}/json")
    fun delCollectFromMainList(@Path("id") id: Int): Observable<WanAndroidBaseResponse>

    /**
     * 取消收藏（在收藏列表取消收藏）
     * 文章列表和收藏列表的id不同
     */
    @POST("http://www.wanandroid.com/lg/uncollect/{id}/json")
    @FormUrlEncoded
    fun delCollectFromCollectList(@Path("id") id: Int, @Field("originId") field: Int = -1): Observable<ResponseBody>
}
