package dinson.customview.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.api.OneApi
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.click
import dinson.customview.kotlin.error
import kotlinx.android.synthetic.main.activity_test.*
import android.view.WindowManager
import android.view.LayoutInflater
import dinson.customview._global.ConstantsUtils
import dinson.customview.utils.AESUtils


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
        dialog.click {
            val layoutInflater = LayoutInflater.from(this)
            val layout = layoutInflater.inflate(R.layout.dialog_001_login, null)
            val dialog = AlertDialog.Builder(this).create()
            dialog.show()
            dialog.window.setContentView(layout)
           // dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        }

        output.click {
            val str = "jtef:/dfas[23ajfkav8293.12!@#$%^&*()_+}{:ha"
            error(str)


            val code = AESUtils.encrypt(ConstantsUtils.PACKAGE_NAME, str)
            error(code)

            val code2 = AESUtils.decrypt(ConstantsUtils.PACKAGE_NAME, code)
            error(code2)
        }
    }
}