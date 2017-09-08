package dinson.customview.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * 七牛云下载图片
 *
 * @author Dinson - 2017/9/8
 */
public interface QiNiuApi {

    @GET
    Call downloadPicFromNet(@Url String fileUrl);
}
