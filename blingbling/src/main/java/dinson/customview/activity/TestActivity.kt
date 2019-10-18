package dinson.customview.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.databinding.DataBindingUtil
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.databinding.ActivityTestBinding
import dinson.customview.kotlin.click
import dinson.customview.kotlin.loge
import dinson.customview.kotlin.toast
import dinson.customview.manager.BlingNdkHelper
import dinson.customview.utils.CacheUtils
import dinson.customview.utils.SystemBarModeUtils
import kotlinx.android.synthetic.main.activity_test.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class TestActivity : BaseActivity() {


    private lateinit var contentView: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentView = DataBindingUtil.setContentView(
            this, R.layout.activity_test)
        contentView.cacheSize = "click times : $clickTimes"
        initUI()
    }

    private fun initUI() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnTest.click { BlingNdkHelper.getFromC().toast() }
    }


    fun onClearAllCache(view: View) {
        CacheUtils.cleanApplicationData(this)
        CacheUtils.getCacheSize(this).loge()
//        contentView.cacheSize = CacheUtils.getCacheSize(this)
    }

    fun onClearCache(view: View) {
        CacheUtils.cleanApplicationCacheData(this)
//        contentView.cacheSize = CacheUtils.getCacheSize(this)
    }


    var clickTimes = 0
    fun onDataBindingClick(view: View) {
//        contentView.cacheSize = "click times : ${++clickTimes}"
    }

    fun onExec(view: View) {
        try {
            val text = etExec.text.toString()
            val p = Runtime.getRuntime().exec(text)
            val data = StringBuilder()
            val ie = BufferedReader(InputStreamReader(p.errorStream))
            val inBufferedReader = BufferedReader(InputStreamReader(p.inputStream))

            ie.forEachLine { data.append(it).append("\n") }
            inBufferedReader.forEachLine { data.append(it).append("\n") }
            loge(data.toString())
        } catch (e: Exception) {
            loge(e.toString())
        }
    }

    fun onPing(view: View) {
        try {
            val p = Runtime.getRuntime().exec("/system/bin/ping -c 4 " + etExec.text.toString())
            BufferedReader(InputStreamReader(p.inputStream)).useLines { s ->
                s.iterator().forEach {
                    if (s.contains("avg")) {
                        it.loge()
                        val i = it.indexOf("/", 20)
                        val j = it.indexOf(".", i)
                        val all = it.substring(i + 1, j)
                        all.toast()
                        all.loge()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    /**
     * 打开开发者模式界面
     */
    private fun startDevelopmentActivity() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
            startActivity(intent)
        } catch (e: Exception) {
            try {
                val componentName = ComponentName("com.android.settings", "com.android.settings.DevelopmentSettings")
                val intent = Intent()
                intent.component = componentName
                intent.action = "android.intent.action.View"
                startActivity(intent)
            } catch (e1: Exception) {
                try {
                    val intent = Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS")//部分小米手机采用这种方式跳转
                    startActivity(intent)
                } catch (e2: Exception) {

                }

            }

        }

    }



    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, TestActivity::class.java))
        }
    }
}