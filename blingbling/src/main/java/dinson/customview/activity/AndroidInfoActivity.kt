package dinson.customview.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.TableRow
import android.widget.TextView
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.kotlin.*
import dinson.customview.utils.StringUtils
import kotlinx.android.synthetic.main.activity_android_info.*


class AndroidInfoActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_info)


        /*weatherLayout.setOnClickListener { view ->
            RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE).subscribe {
                if (!it) return@subscribe
                val offsetX = screenWidth() / 2f - view.x - view.halfWidth()
                val offsetY = screenHeight() / 2f - view.y - view.halfHeight()
                val path = view.createArcPath(offsetX, offsetY)
                ValueAnimator.ofFloat(0f, 1f).apply {
                    addUpdateListener(view.getArcListener(path))
                    onEnd { revealAndShowAndroidInfo() }
                }.start()
            }
        }*/

        initUI()
    }

    private fun initUI() {
        createTableRows().forEach { tableLayout.addView(it) }
    }

    /**
     * 水波纹扩散，显示Android设备信息
     */
    private fun revealAndShowAndroidInfo() {
        /*val cx = weatherLayout.x + weatherLayout.halfWidth()
        val cy = weatherLayout.y + weatherLayout.halfHeight()
        val endRadius = Math.hypot(screenHeight().toDouble(), screenWidth().toDouble()).toFloat()
        val startRadius = weatherLayout.height.toFloat()
        weatherLayout.hide()

        *//* vsAndroidInfo?.apply {
             vsAndroidInfo.inflate()
             createTableRows().forEach { tableLayout.addView(it) }
             RxView.clicks(tableLayout).throttleFirst(1, TimeUnit.SECONDS).subscribe {
                 concealAndHiddenAndroidInfo()
             }
         }*//*
        scrollView.show()
        ViewAnimationUtils.createCircularReveal(scrollView, cx.toInt(), cy.toInt(), startRadius, endRadius).apply {
            duration = 300
            interpolator = AccelerateInterpolator()
        }.start()*/
    }

    /**
     * 水波纹收缩，隐藏Android设备信息
     */
    private fun concealAndHiddenAndroidInfo() {
        /*val cx = weatherLayout.x + weatherLayout.halfWidth()
        val cy = weatherLayout.y + weatherLayout.halfHeight()
        val startRadius = Math.hypot(screenHeight().toDouble(), screenWidth().toDouble()).toFloat()
        val endRadius = weatherLayout.height.toFloat()
        ViewAnimationUtils.createCircularReveal(scrollView, cx.toInt(), cy.toInt(), startRadius, endRadius).onEnd {
            scrollView.hide()
            weatherLayout.show()
            resetWeatherLayout()
        }.animator().apply {
            duration = 500
            interpolator = AccelerateInterpolator()
        }.start()*/
    }


    /**
     * 创建 AndroidInfo 行
     */
    private fun createTableRows() = getAndroidInfoData().map {
        val tableRow = TableRow(this)
        val whiteColor = getCompatColor(R.color.white)
        val padding = dip(8)
        val paddingHalf = dip(4)

        tableRow.addView(TextView(this).apply {
            setTextColor(whiteColor)
            text = it.key
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setPadding(padding, paddingHalf, padding, paddingHalf)
        })

        tableRow.addView(TextView(this).apply {
            setTextColor(whiteColor)
            text = it.value
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setPadding(padding, paddingHalf, padding, paddingHalf)
        })

        tableRow
    }

    @SuppressLint("HardwareIds", "MissingPermission")
    private fun getAndroidInfoData(): LinkedHashMap<String, String> {

        val phone = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        return LinkedHashMap<String, String>().apply {
            put("系统版本", "Android ${Build.VERSION.RELEASE} SDK${Build.VERSION.SDK_INT}")
            put("品牌型号", "${Build.BRAND} ${Build.MODEL}")
            put("AndroidID", Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))//不可靠
            put("设备指纹", Build.FINGERPRINT)
            put("Root状态", if (isDeviceRooted()) "已root" else "未root")
            put("分辨率", "${outMetrics.heightPixels}x${outMetrics.widthPixels}")
            put("屏幕密度", "density: ${outMetrics.density}/${outMetrics.scaledDensity} dpi:${outMetrics.densityDpi}")
            put("状态栏高度", "${getStatusBarHeight()}px")
            put("IMEI", getIMEI(phone))
            put("CPU型号", getCpuInfo())
            put("CPU架构", Build.SUPPORTED_ABIS.joinToString(","))
            val value = getSDCardMemory()
            put("SDCard", "${StringUtils.byte2FileSize(value[1])}可用 / ${StringUtils.byte2FileSize(value[0])}总容量")

            //添加sim卡信息
            if (phone.simState == TelephonyManager.SIM_STATE_READY) {
                put("SIM状态", getSimState(phone))
                put("手机号码", getLine1Number(phone))
                put("SIM卡序列号", getSubscriberId(phone))
                put("IMSI", getSimSerialNumber(phone))
                put("服务商", getSimOperatorName(phone))
                put("SIM卡国家码", getSimCountryIso(phone))
                put("漫游", if (isNetworkRoaming(phone)) "漫游中" else "未使用漫游服务")
                put("数据活动状态", getDataActivity(phone))
                put("数据连接状态", getDataState(phone))
            }
        }
    }

    /**
     * 重置天气布局
     */
    private fun resetWeatherLayout() {
        /*  val path = weatherLayout.createArcPath(0f, 0f)
          ValueAnimator.ofFloat(0f, 1f).apply {
              startDelay = 100L
              addUpdateListener(weatherLayout.getArcListener(path))
          }.start()*/
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AndroidInfoActivity::class.java))
        }
    }
}
