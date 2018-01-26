package dinson.customview.activity

import android.os.Bundle
import dinson.customview.R
import dinson.customview._global.BaseActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test.*
import org.jsoup.Jsoup

class TestActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test)


        val list =  arrayListOf  ("https://user-gold-cdn.xitu.io/2017/6/4/f93812ae05d3c7933f8a6513dc37c629?imageView2/0/w/1280/h/960/format/webp/ignore-error/1",
            "https://user-gold-cdn.xitu.io/2017/6/4/a9a142b37e4e8b3fbe8c387c95e30050?imageView2/0/w/1280/h/960/format/webp/ignore-error/1",
            "https://user-gold-cdn.xitu.io/2017/6/4/11dee01bce6cf189a1cd69d8fe25e1d2?imageView2/0/w/1280/h/960/format/webp/ignore-error/1")


        banner.setPages(list,ParkDetailBannerViewHolder())
        banner.start()

     }




    private fun jsoupTest() {
        Observable.just("http://www.dinson.win/")
            .map { s ->
                val document = Jsoup.connect(s).get()
                document
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _ ->

            }
    }

}