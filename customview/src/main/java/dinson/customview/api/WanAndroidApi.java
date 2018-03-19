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
     * 获取首页所有最新的文章
     */
    @GET("http://www.wanandroid.com/article/list/{index}/json")
    Observable<WanAndArticleResponse> getMainArticleList(@Path("index") int pageIndex);

    @POST("http://www.wanandroid.com/user/login")
    Observable<ResponseBody> login(@Query("username") String username, @Query("password") String password);

    @GET("http://www.wanandroid.com/lg/collect/list/0/json")
    Observable<WanAndArticleResponse> login2();


}
