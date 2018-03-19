package dinson.customview.activity

import android.os.Bundle
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.api.OneApi
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.click
import dinson.customview.kotlin.error
import kotlinx.android.synthetic.main.activity_test.*


class TestActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        iv.click {
            HttpHelper.create(OneApi::class.java).login("dinson", "Aa123456").compose(RxSchedulers.io_main())
                .subscribe({
                    error(it.string())
                }, {
                    error("请求错误")
                })
        }
        iv2.click {
            HttpHelper.create(OneApi::class.java).collectList().compose(RxSchedulers.io_main())
                .subscribe({
                    error(it.string())
                }, {
                    error("请求错误")
                })
        }
        iv3.click {
            HttpHelper.create(OneApi::class.java).addCollect(2674).compose(RxSchedulers.io_main())
                .subscribe({
                    error(it.string())
                }, {
                    error("请求错误")
                })
        }
        clear.click {
            HttpHelper.clearCookie()
        }
    }
}