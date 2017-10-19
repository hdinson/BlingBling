package dinson.customview.activity

import android.os.Bundle
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.api.ExchangeApi
import dinson.customview.http.HttpHelper
import dinson.customview.model._003ModelUtil
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
    }


    private fun getCurrencyData() {
        var currencyList = _003ModelUtil.getCurrencyList()
        Observable.fromIterable(currencyList)
            .subscribe { tvContent.append("$it\n") }
    }

    private fun getExchangeData() {
        HttpHelper.create(ExchangeApi::class.java).getRate()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                tvContent.text = it.toString()
            }
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