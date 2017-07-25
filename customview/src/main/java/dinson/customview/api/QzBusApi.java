package dinson.customview.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Dinson - 2017/7/25
 */
public interface QzBusApi {
    @GET("/index.php/Line/line/line/{num}/submit/查询/")
    Call<String> queryRoute(@Path("num") String route);
}
