package dinson.customview.api;


import dinson.customview.entity.one.DailyDetail;
import dinson.customview.entity.one.DailyList;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
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

    @GET("http://p2c0m2mi6.bkt.clouddn.com/cntv.txt")
    Observable<ResponseBody> get();

}
