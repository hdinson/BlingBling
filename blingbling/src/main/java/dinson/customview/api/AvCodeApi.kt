package dinson.customview.api

import dinson.customview.entity.av.MovieVideo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 *  通过番号搜索电影
 */
interface AvCodeApi{
    @Headers("Cache-Control:public,max-age=360000")
    @GET("http://api.rekonquer.com/psvs/search.php")
    fun queryAvCode(@Query("kw") key:String):Observable<MovieVideo>
}