package dinson.customview.api

import dinson.customview.entity.zhihu.ZhihuTucaoListResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * 知乎相关api
 */
interface ZhihuTucaoApi {
    /**
     * 获取知乎最新的吐槽列表
     */
    @GET("http://news-at.zhihu.com/api/4/section/2")
    fun getStoriesList(): Observable<ZhihuTucaoListResponse>


}
