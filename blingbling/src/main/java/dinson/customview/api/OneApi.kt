package dinson.customview.api


import dinson.customview.entity.one.DailyDetailResult
import dinson.customview.entity.one.DailyList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * one一个api
 */

interface OneApi {

    @GET("http://v3.wufazhuce.com:8000/api/hp/idlist/0")
    fun loadDaily(): Observable<DailyList>

    @GET("http://v3.wufazhuce.com:8000/api/hp/detail/{id}")
    fun getDetail(@Path("id") id: Int): Observable<DailyDetailResult>
}
