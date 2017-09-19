package dinson.customview.api;


import dinson.customview.entity.one.DailyDetail;
import dinson.customview.entity.one.DailyList;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * one一个api
 */

public interface OneApi {

    @GET("http://v3.wufazhuce.com:8000/api/hp/idlist/0")
    Flowable<DailyList> getDaily();

    @GET("http://v3.wufazhuce.com:8000/api/hp/detail/{id}")
    Flowable<DailyDetail> getDetail(@Path("id") int id);

    @GET("http://ondlsj2sn.bkt.clouddn.com/Fu9ecL8Yb9QueqV5YFAj9CO4-xdO.json")
    Observable<String> get();

    /*POST /delete/bmV3ZG9jczpmaW5kX21hbi50eHQ= HTTP/1.1
User-Agent: curl/7.30.0
Host: rs.qiniu.com
Accept: *

 String token = "dinson-blog:FpUVRI5ACAX7YkTOXLuD7mP_3BPg.webp"; // 编码前

        String base64Token = Base64.encodeToString(token.getBytes(), Base64.DEFAULT);//  编码后

    Authorization: QBox http://ondlsj2sn.bkt.clouddn.com/FpUVRI5ACAX7YkTOXLuD7mP_3BPg.webp
User-Agent: curl/7.30.0
Host: rs.qiniu.com
Accept: *
    Authorization: QBox u8WqmQu1jH21kxpIQmo2LqntzugM1VoHE9_pozCU:2LJIG...(过长已省略)

    Host:           rs.qiniu.com

:  QBox <AccessToken>

            */

    @Headers({"Host:ondlsj2sn.bkt.clouddn.com",
        "Content-Type:application/x-www-form-urlencoded", "Accept: */*"

    })
    @POST("http://iovip-z2.qbox.me/delete/{url}")
    Observable<String> postDelete(@Path("url") String url, @Header("Authorization") String at);


}
