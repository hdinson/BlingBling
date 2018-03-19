package dinson.customview.api;


import dinson.customview.entity.wanandroid.WanAndArticleResponse;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 玩安卓的开放api
 */

public interface WanAndroidApi {
    /**
     * 登陆
     */
    @POST("http://www.wanandroid.com/user/login")
    Observable<ResponseBody> login(@Query("username") String username, @Query("password") String password);

    /**
     * 获取首页所有最新的文章
     */
    @GET("http://www.wanandroid.com/article/list/{index}/json")
    Observable<WanAndArticleResponse> getMainArticleList(@Path("index") int pageIndex);

    /**
     * 获取收藏列表
     */
    @GET("http://www.wanandroid.com/lg/collect/list/0/json")
    Observable<ResponseBody> collectList();

    /**
     * 添加收藏
     */
    @POST("http://www.wanandroid.com/lg/collect/{id}/json")
    Observable<ResponseBody> addCollect(@Path("id") int id);

    /**
     * 取消收藏（在文章列表取消收藏）
     * 文章列表和收藏列表的id不同
     */
    @POST("http://www.wanandroid.com/lg/uncollect_originId/{id}/json")
    Observable<ResponseBody> delCollectFromMainList(@Path("id") int id);

    /**
     * 取消收藏（在收藏列表取消收藏）
     * 文章列表和收藏列表的id不同
     */
    @POST("http://www.wanandroid.com/lg/uncollect/{id}/json")
    Observable<ResponseBody> delCollectFromCollectList(@Path("id") int id);
}
