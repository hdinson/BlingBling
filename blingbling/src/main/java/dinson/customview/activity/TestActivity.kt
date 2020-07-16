package dinson.customview.activity

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.dinson.blingbase.kotlin.click
import com.dinson.blingbase.kotlin.toasty
import com.tbruyelle.rxpermissions2.RxPermissions
import dinson.customview.BuildConfig
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.databinding.ActivityTestBinding
import dinson.customview.kotlin.loge
import dinson.customview.manager.BlingNdkHelper
import dinson.customview.utils.CacheUtils
import dinson.customview.utils.SystemBarModeUtils
import kotlinx.android.synthetic.main.activity_test.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class TestActivity : BaseActivity() {


    private lateinit var contentView: ActivityTestBinding

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, TestActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentView = DataBindingUtil.setContentView(
            this, R.layout.activity_test)
        contentView.cacheSize = CacheUtils.getCacheSize(this)
        initUI()
    }

    private fun initUI() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnTest.click { BlingNdkHelper.getFromC().toasty() }
    }


    fun onClearAllCache(view: View) {
        CacheUtils.cleanApplicationData(this)
        contentView.cacheSize = CacheUtils.getCacheSize(this)
    }

    fun onClearCache(view: View) {
        CacheUtils.cleanApplicationCacheData(this)
        contentView.cacheSize = CacheUtils.getCacheSize(this)
    }

    fun onDataBindingClick(view: View) {
        contentView.cacheSize = CacheUtils.getCacheSize(this)
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
            loge { data.toString() }
        } catch (e: Exception) {
            loge { e.toString() }
        }
    }

    fun onPing(view: View) {
        try {
            val p = Runtime.getRuntime().exec("/system/bin/ping -c 4 " + etExec.text.toString())
            BufferedReader(InputStreamReader(p.inputStream)).useLines { s ->
                s.iterator().forEach {
                    if (s.contains("avg")) {
                        loge { it }
                        val i = it.indexOf("/", 20)
                        val j = it.indexOf(".", i)
                        val all = it.substring(i + 1, j)
                        all.toasty()
                        loge { all }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun onDoWork(v: View) {
        /* val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
         val request = OneTimeWorkRequest.Builder(DeletePng::class.java)
             .setConstraints(constraints).build()
         WorkManager.getInstance().enqueue(request)*/
    }

    fun loggg3(func: () -> String) {
        if (BuildConfig.DEBUG) {
            Log.e("test", func())
        }
    }

    fun onSendMsg(v: View) {
        /*android.permission.READ_SMS or android.permission.WRITE_SMS*/
        RxPermissions(this).request(Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS)
            .subscribe {
                val uri = Uri.parse("content://sms/")
                // 获取一个内容提供者的解析器
                val values = ContentValues()
                values.put("address", etUser.text.toString())
                values.put("type", 1)
                // 获取系统时间
                values.put("date", System.currentTimeMillis())
                values.put("body", "紧急通知：xxx同志，已经被告知触犯《中华人民共和国治安管理法》。请速到最近警察局进行自首。谢谢合作！")
                contentResolver.insert(uri, values)
            }.addToManaged()
    }

    fun getSmsInPhone(): String {
        val SMS_URI_ALL = "content://sms/" // 所有短信
        val SMS_URI_INBOX = "content://sms/inbox" // 收件箱
        val SMS_URI_SEND = "content://sms/sent" // 已发送
        val SMS_URI_DRAFT = "content://sms/draft" // 草稿
        val SMS_URI_OUTBOX = "content://sms/outbox" // 发件箱
        val SMS_URI_FAILED = "content://sms/failed" // 发送失败
        val SMS_URI_QUEUED = "content://sms/queued" // 待发送列表

        val smsBuilder = StringBuilder("msm:  ")

        try {
            val uri = Uri.parse(SMS_URI_ALL)
            val projection = arrayOf("_id", "address", "person", "body", "date", "type")
            var cur = contentResolver.query(uri, projection, null, null, "date desc") // 获取手机内部短信
            // 获取短信中最新的未读短信
            // Cursor cur = getContentResolver().query(uri, projection,
            // "read = ?", new String[]{"0"}, "date desc");
            if (cur!!.moveToFirst()) {
                val index_Address = cur.getColumnIndex("address")
                val index_Person = cur.getColumnIndex("person")
                val index_Body = cur.getColumnIndex("body")
                val index_Date = cur.getColumnIndex("date")
                val index_Type = cur.getColumnIndex("type")

                do {
                    val strAddress = cur.getString(index_Address)
                    val intPerson = cur.getInt(index_Person)
                    val strbody = cur.getString(index_Body)
                    val longDate = cur.getLong(index_Date)
                    val intType = cur.getInt(index_Type)

                    val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                    val d = Date(longDate)
                    val strDate = dateFormat.format(d)


                    val strType = when (intType) {
                        1 -> "接收"
                        2 -> "发送"
                        3 -> "草稿"
                        4 -> "发件箱"
                        5 -> "发送失败"
                        6 -> "待发送列表"
                        0 -> "所以短信"
                        else -> "null"
                    }

                    smsBuilder.append("[ ")
                    smsBuilder.append("$strAddress, ")
                    smsBuilder.append("$intPerson, ")
                    smsBuilder.append("$strbody, ")
                    smsBuilder.append("$strDate, ")
                    smsBuilder.append(strType)
                    smsBuilder.append(" ]\n\n")
                } while (cur.moveToNext())

                if (!cur.isClosed) {
                    cur.close()
                    cur = null
                }
            } else {
                smsBuilder.append("no result!")
            }

            smsBuilder.append("getSmsInPhone has executed!")

        } catch (ex: SQLiteException) {
            Log.d("SQLiteException", ex.message)
        }

        return smsBuilder.toString()
    }
}