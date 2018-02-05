package dinson.customview.activity

import android.os.Bundle
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.api.OneApi
import dinson.customview.entity.BaseParams
import dinson.customview.entity.Login
import dinson.customview.http.HttpHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

class TestActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test)



        tvTitle.setOnClickListener {
            /* // jsoupTest()
              HttpHelper.create(OneApi::class.java).get()
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe {
                      tvContent.text=it.string()
                  }*/


            val builder = OkHttpClient.Builder()
            builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
            //.addInterceptor(HttpHelper.LoggingInterceptor())//添加拦截器 日志
            val retrofit = Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())//对http请求结果进行统一的预处理
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//对rxjava提供支持
                .baseUrl("http://192.168.1.1")
                .build()


            retrofit.create(testApi::class.java).getToken(BaseParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    tvContent.text=it.string()
                }
        }

    }


    interface testApi {
        @POST("http://120.77.200.50:8083/admin/oss/getUploadToken")
        fun getToken(@Body entity: BaseParams): Observable<ResponseBody>

        /**
         * 登录
         */
        @POST("api/user/login")
        fun loginByPwd(@Body entity: Login): Observable<ResponseBody>
    }


    private fun jsoupTest() {
        Observable.just("https://github.com/DinsonCat/SomeDoc/blob/master/mh.json")
            .map { s ->
                val document = Jsoup.connect(s).get()
                document
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { s ->
                tvContent.text = s.body()
                    .toString()
            }
    }

}