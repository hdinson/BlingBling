package dinson.customview.activity

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.dinson.blingbase.kotlin.toasty
import com.dinson.blingbase.utils.RxNotification
import com.dinson.blingbase.utils.SystemBarModeUtils
import com.huawei.hmf.tasks.Task
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLApplication
import com.huawei.hms.mlsdk.common.MLException
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.document.MLDocument
import com.huawei.hms.mlsdk.document.MLDocumentSetting
import com.huawei.hms.mlsdk.text.MLRemoteTextSetting
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.tbruyelle.rxpermissions2.RxPermissions
import dinson.customview.BuildConfig
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.databinding.ActivityTestBinding
import dinson.customview.kotlin.loge
import dinson.customview.kotlin.logi
import dinson.customview.manager.BlingNdkHelper
import dinson.customview.utils.CacheUtils
import dinson.customview.utils.GlideEngine
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

    fun onOpenDelv(view: View) {
        BlingNdkHelper.getFromC().toasty()
    }

    fun onPostNormalMsg(view: View) {
        val channelId = (1..Int.MAX_VALUE).random()
        val resultPendingIntent = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        RxNotification.Builder(this, "简单的通知", "通知内容", R.mipmap.ic_launcher)
            .setChannelId(channelId)
            .setChannelName("测试界面通知")
            .setTickerText("滚动的通知。。。")
            //.setPendingIntent(resultPendingIntent)
            .setLargeIcon(R.drawable.ic_arrows_down)
            .build().show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSendRemindNotification(view: View) {
        val channelId = (1..Int.MAX_VALUE).random()
        val resultPendingIntent = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        RxNotification.Builder(this, "带声音的通知", "内容", R.mipmap.ic_launcher)
            .setSoundRes(R.raw.early_bird)
            .setChannelId(channelId)
            .setLightsColor(Color.BLUE, 2000, 2000)
            .setChannelName("测试界面通知")
            .setImportance(NotificationManager.IMPORTANCE_HIGH)
            .setVibrate(longArrayOf(0, 5000, 500, 1000))
            //.setPendingIntent(resultPendingIntent)
            .setLargeIcon(R.drawable.ic_arrows_down)
            .build().show()
    }

    private var mProgress = 0.1f
    fun onSendProgressNotification(view: View) {
        RxNotification.Builder(this, "下载音乐", "下载双人枕头中...", R.mipmap.ic_launcher)
            .setChannelId(10088)
            .setChannelName("下载音乐")
            .setProgress(mProgress)
            .setEnableVibration(false)
            .build().show()
        mProgress += 0.2f
        if (mProgress >= 1f) {
            mProgress = 0f
        }
    }

    fun onCustomNotification(view: View) {


        val remoteViews = RemoteViews(this.packageName, R.layout.layout_remote_test_act)

        val channelId = (1..Int.MAX_VALUE).random()
        RxNotification.Builder(this, "自定义视图标题", "自定义视图内容（应该不显示）", R.mipmap.ic_launcher)
            .setChannelId(channelId)
            .setChannelName("自定义视图")
            .setRemoteView(remoteViews)
            .build().show()


    }

    fun onOCR(view: View) {

        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .loadImageEngine(GlideEngine.createGlideEngine())
            .maxSelectNum(1)// 最大图片选择数量 int
            .minSelectNum(1)// 最小选择数量 int
            .imageSpanCount(4)// 每行显示个数 int
            .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
            .isCamera(true)// 是否显示拍照按钮 true or false
            .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
            .enableCrop(true)// 是否裁剪 true or false
            .compress(true)// 是否压缩 true or false
            .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    val path = if (selectList[0].isCompressed) selectList[0].compressPath else selectList[0].path
//                    mSelectorGoodsImg?.setGoodsImgLocalPath(path)
                    val bitmap = BitmapFactory.decodeFile(path)

                    val start = System.currentTimeMillis()
/*

                    val languageList = ArrayList<String>()
                    languageList.add("zh")
                    languageList.add("en")
                    val setting = MLDocumentSetting.Factory() // 设置识别语言列表，使用ISO 639-1标准。
                        .setLanguageList(languageList) // 设置文本边界框返回格式，
                        // MLRemoteTextSetting.NGON：返回四边形的四个顶点坐标。
                        // MLRemoteTextSetting.ARC：返回文本排列为弧形的多边形边界的顶点，最多可返回多达72个顶点的坐标。
                        .setBorderType(MLRemoteTextSetting.ARC)
                        .create()
                    val analyzer = MLAnalyzerFactory.getInstance().getRemoteDocumentAnalyzer(setting)
*/


                    val options = MLRemoteTextSetting.Factory().setBorderType(MLRemoteTextSetting.ARC).create()
                    val analyzer = MLAnalyzerFactory.getInstance().getRemoteTextAnalyzer(options)



                    logi { "识别模块初始化时间：${System.currentTimeMillis() - start} ms" }
                    val frame = MLFrame.fromBitmap(bitmap)
                    ivImg.setImageBitmap(bitmap)
                    val task = analyzer.asyncAnalyseFrame(frame)
                    task.addOnSuccessListener {
                        loge { "成功：${it.stringValue}" }
                    }.addOnFailureListener {
                        // 识别失败。
                        // Recognition failure.
                        try {
                            val mlException: MLException = it as MLException
                            // 获取错误码，开发者可以对错误码进行处理，根据错误码进行差异化的页面提示。错误码信息可参见：机器学习服务错误码。
                            val errorCode: Int = mlException.errCode
                            // 获取报错信息，开发者可以结合错误码，快速定位问题。
                            val errorMessage: String = mlException.message.toString()
                            loge { "失败：$errorCode, $errorMessage" }
                        } catch (error: Exception) {
                            // 转换错误处理。
                        }
                    }
                }
            }
        }
    }
}