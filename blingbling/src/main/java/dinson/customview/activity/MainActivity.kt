package dinson.customview.activity

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.*
import android.os.Build.VERSION.RELEASE
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.Settings
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import android.support.v7.widget.helper.ItemTouchHelper
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.widget.TableRow
import android.widget.TextView
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.jakewharton.rxbinding2.view.RxView
import com.tbruyelle.rxpermissions2.RxPermissions
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter.MainContentAdapter
import dinson.customview.api.OneApi
import dinson.customview.api.XinZhiWeatherApi
import dinson.customview.entity.ClassBean
import dinson.customview.entity.HomeWeather
import dinson.customview.entity.one.DailyDetail
import dinson.customview.http.BaseObserver
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.*
import dinson.customview.listener.MainItemTouchHelper
import dinson.customview.listener.OnItemTouchMoveListener
import dinson.customview.model.HomeWeatherModelUtil
import dinson.customview.utils.CacheUtils
import dinson.customview.utils.LogUtils
import dinson.customview.utils.StringUtils
import dinson.customview.utils.TypefaceUtils
import dinson.customview.weight.banner.BannerPageClickListener
import dinson.customview.weight.banner.holder.MainBannerHolder
import dinson.customview.weight.recycleview.LinearItemDecoration
import dinson.customview.weight.recycleview.OnItemClickListener
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.aspect_ratio_iv_layout.*
import kotlinx.android.synthetic.main.layout_main_android_info.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity(), OnItemTouchMoveListener, OnItemClickListener.OnClickListener {

    //private val mContentData = ArrayList<ClassBean>()
    private val mHeadData = ArrayList<DailyDetail>()
    private var mAMapLocationClient: AMapLocationClient? = null
    private lateinit var mTouchHelper: ItemTouchHelper
    private val mOneApi = HttpHelper.create(OneApi::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initContent()
        initHead()
        getLocation()
    }


    /**
     * 内容的数据集
     */
    private val mContentData: List<ClassBean> by lazy {
        listOf(ClassBean(getString(R.string.wan_android_title), getString(R.string.wan_android_desc),
            _001WanAndroidActivity::class.java, getString(R.string.wan_android_img)),
            ClassBean(getString(R.string.zhihu_title), getString(R.string.zhihu_desc),
                _002ZhihuTucaoListActivity::class.java, getString(R.string.zhihu_img)),
            ClassBean(getString(R.string.exchange_rate_title), getString(R.string.exchange_rate_desc),
                _003ExchangeActivity::class.java, getString(R.string.exchange_rate_img)),
            ClassBean(getString(R.string.bilibili_title), getString(R.string.bilibili_desc),
                _004BiliBiliListActivity::class.java, getString(R.string.bilibili_img)),
            ClassBean(getString(R.string.qi_niu_title), getString(R.string.qi_niu_desc),
                _005QiNiuYunActivity::class.java, getString(R.string.qi_niu_img)),
            ClassBean(getString(R.string.floatingView_title), getString(R.string.floatingView_desc),
                _006FloatingViewActivity::class.java, getString(R.string.floatingView_img)),
            ClassBean(getString(R.string.spotlight_title), getString(R.string.spotlight_desc),
                _007SpotlightActivity::class.java, getString(R.string.spotlight_img)),
            ClassBean(getString(R.string.rich_editor_title), getString(R.string.rich_editor_desc),
                _008RichEditorActivity::class.java, getString(R.string.rich_editor_img)),
            ClassBean(getString(R.string.google_vr_title), getString(R.string.google_vr_desc),
                _009GoogleVRActivity::class.java, getString(R.string.google_vr_img)),
            ClassBean(getString(R.string.parallax_animation_title), getString(R.string.parallax_animation_desc),
                _010ParallaxActivity::class.java, getString(R.string.parallax_animation_img)),
            ClassBean(getString(R.string.diagonal_layout_title), getString(R.string.diagonal_layout_desc),
                _011DiagonalLayoutActivity::class.java, getString(R.string.diagonal_layout_img)),
            ClassBean(getString(R.string.qqnavi_title), getString(R.string.qqnavi_desc),
                _012QQNaviViewActivity::class.java, getString(R.string.qqnavi_img)),
            ClassBean(getString(R.string.step_view_title), getString(R.string.step_view_desc),
                _013StepViewActivity::class.java, getString(R.string.step_view_img)),
            ClassBean(getString(R.string.honor_clock_title), getString(R.string.honor_clock_desc),
                _014HonorClockActivity::class.java, getString(R.string.honor_clock_img)),
            ClassBean(getString(R.string.explosion_field_title), getString(R.string.explosion_field_desc),
                _015ExplosionFieldActivity::class.java, getString(R.string.explosion_field_img)),
            ClassBean(getString(R.string.parallax_iv_title), getString(R.string.parallax_iv_desc),
                _016ParallaxImgViewActivity::class.java, getString(R.string.parallax_iv_img)),
            ClassBean(getString(R.string.tetris_title), getString(R.string.tetris_desc),
                _017TetrisActivity::class.java, getString(R.string.tetris_img)),
            ClassBean(getString(R.string.nfc_title), getString(R.string.nfc_desc),
                _018NFCActivity::class.java, getString(R.string.nfc_img)),
            ClassBean(getString(R.string.ganged_recycle_title), getString(R.string.ganged_recycle_desc),
                _019GangedRecycleViewActivity::class.java, getString(R.string.ganged_recycle_img)),
            ClassBean(getString(R.string.facebook_title), getString(R.string.facebook_desc),
                _020ShimmerActivity::class.java, getString(R.string.facebook_img)),
            ClassBean(getString(R.string.like_smile_title), getString(R.string.like_smile_desc),
                _021LikeSmileViewActivity::class.java, getString(R.string.like_smile_img)),
            ClassBean(getString(R.string.test_layout_title), getString(R.string.test_layout_desc),
                TestActivity::class.java, getString(R.string.test_layout_img))
        )
    }

    private fun initContent() {
        val mainAdapter = MainContentAdapter(this, mContentData, this)
        mTouchHelper = ItemTouchHelper(MainItemTouchHelper(mainAdapter))
        mTouchHelper.attachToRecyclerView(rvContent)
        rvContent.apply {
            adapter = mainAdapter
            val layoutManager = LinearLayoutManager(this@MainActivity)
            layoutManager.isSmoothScrollbarEnabled = true
            //layoutManager.isAutoMeasureEnabled = true
            //setHasFixedSize(true)
            //isNestedScrollingEnabled = false
            setLayoutManager(layoutManager)
            addItemDecoration(LinearItemDecoration(this@MainActivity))
            addOnItemTouchListener(OnItemClickListener(this@MainActivity, rvContent, this@MainActivity))
        }
    }

    private fun initHead() {
        llDaily.setPadding(0, (screenWidth() / 1.5).toInt(), 0, 0)

        mainBanner.setPages(mHeadData, MainBannerHolder())
        mainBanner.setDuration(500)
        mainBanner.setBannerPageClickListener(BannerPageClickListener { _, position ->
            /*Glide储存本地文件*/
            /*Single.just(mHeadData[position].data.hp_img_url)
                .map { s ->
                    Glide.with(this@MainActivity).downloadOnly().load(s).downloadOnly(Target.SIZE_ORIGINAL,
                        Target.SIZE_ORIGINAL).get().path
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { path ->
                    debug("CurrentItem:" + position + " imgUrl:" + mHeadData[position].data.hp_img_url + "  imgPath:" + path)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, view, "dailyPic")
                    DailyPicActivity.start(this@MainActivity, mHeadData[position].data, path, options)
                }*/
            showDailyDetail(mHeadData[position].data)
        })
        mainBanner.start()

        val mainHeardCache = CacheUtils.getMainHeardCache()
        Flowable.fromPublisher(if (mainHeardCache == null) mOneApi.daily else Flowable.just(mainHeardCache))
            .flatMap { list ->
                CacheUtils.setMainHeardCache(list)
                Flowable.fromIterable(list.data)
            }
            .flatMap { id ->
                val detail = CacheUtils.getDailyDetail(id!!)
                if (detail == null) mOneApi.getDetail(id) else Flowable.just(detail)
            }
            .filter { detail -> detail.data != null }
            .collect({ mHeadData }) { list, bean ->
                verbose(bean.toString())
                CacheUtils.setDailyDetail(bean)
                list.add(bean)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _ -> mainBanner.setPages(mHeadData, MainBannerHolder()) }) { throwable ->
                LogUtils.d(throwable.toString())
                vsContent.inflate()
                ivImg.setImageResource(R.drawable.def_img)
            }
    }

    /**
     * 显示每日一图的详情
     */
    private fun showDailyDetail(daily: DailyDetail.DataBean) {
        llDaily.show()


        val content = daily.hp_content
        val indexOfStop = content.lastIndexOf("by")
        val indexOfQm = content.lastIndexOf("from")
        val index = if (indexOfStop > indexOfQm) indexOfStop else indexOfQm

        val subContent = content.substring(0, index)
        val subName = content.substring(index).trim { it <= ' ' }

        tvDailyTitle.text = ""
        tvDailyContent.text = ""
        val subscribe = Observable.interval(10, TimeUnit.MILLISECONDS)
            .take(Math.max(subContent.length, subName.length).toLong())
            .compose(RxSchedulers.io_main())
            .subscribe({
                if (it < subName.length) tvDailyTitle.append(subName[it.toInt()].toString())
                if (it < subContent.length) tvDailyContent.append(subContent[it.toInt()].toString())
            })
        llDaily.click {
            subscribe.dispose()
            llDaily.hide()
        }
    }

    /**
     * 定位
     */
    private fun getLocation() {
        RxPermissions(this).request(Manifest.permission.ACCESS_COARSE_LOCATION)
            .subscribe {
                if (!it) return@subscribe
                mAMapLocationClient = AMapLocationClient(this.applicationContext)
                mAMapLocationClient?.let {
                    it.setLocationListener(locationListener)//设置定位监听
                    it.setLocationOption(getAmapOption())//设置定位参数
                    it.startLocation()//开始定位
                }
            }
    }

    private var locationListener = AMapLocationListener { location ->
        destroyLocation()//只定位一次
        if (null == location) return@AMapLocationListener
        if (location.errorCode != 0) return@AMapLocationListener  //errCode等于0代表定位成功

        Observable.just<String>(location.city)
            .flatMap { city ->
                val cache = CacheUtils.getHomeWeatherCache(city)
                if (cache == null) HttpHelper.create(XinZhiWeatherApi::class.java).getWeather(city)
                else Observable.just(cache)
            }
            .map { homeWeather ->
                CacheUtils.setHomeWeatherCache(homeWeather)
                homeWeather
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<HomeWeather>() {
                override fun onHandlerSuccess(weather: HomeWeather) {
                    initWeatherLayout(weather) //设置数据
                }
            })
    }

    private fun initWeatherLayout(weather: HomeWeather) {
        debug(".$weather")
        val resultsBean = weather.results[0]
        iconFontWeather.setText(HomeWeatherModelUtil.getWeatherFont(resultsBean.now.code))
        tvWeather.apply {
            typeface = TypefaceUtils.getAppleFont(this@MainActivity)
            text = String.format("%s℃", resultsBean.now.temperature)
        }

        /*val alphaAnimator = ObjectAnimator.ofFloat(weatherLayout, "alpha", 0f, 1f)
        val scaleX = ObjectAnimator.ofFloat(weatherLayout, "scaleX", 0f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(weatherLayout, "scaleY", 0f, 1.0f)
        val set = AnimatorSet()
        set.interpolator = DecelerateInterpolator()
        set.play(alphaAnimator).with(scaleX).with(scaleY)
        set.start()*/

        weatherLayout.setOnClickListener { view ->
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
        }
    }

    /**
     * 水波纹扩散，显示Android设备信息
     */
    private fun revealAndShowAndroidInfo() {
        val cx = weatherLayout.x + weatherLayout.halfWidth()
        val cy = weatherLayout.y + weatherLayout.halfHeight()
        val endRadius = Math.hypot(screenHeight().toDouble(), screenWidth().toDouble()).toFloat()
        val startRadius = weatherLayout.height.toFloat()
        weatherLayout.hide()

        vsAndroidInfo?.apply {
            vsAndroidInfo.inflate()
            createTableRows().forEach { tableLayout.addView(it) }
            RxView.clicks(tableLayout).throttleFirst(1, TimeUnit.SECONDS).subscribe {
                concealAndHiddenAndroidInfo()
            }
        }
        scrollView.show()
        ViewAnimationUtils.createCircularReveal(scrollView, cx.toInt(), cy.toInt(), startRadius, endRadius).apply {
            duration = 300
            interpolator = AccelerateInterpolator()
        }.start()
    }

    /**
     * 水波纹收缩，隐藏Android设备信息
     */
    private fun concealAndHiddenAndroidInfo() {
        val cx = weatherLayout.x + weatherLayout.halfWidth()
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
        }.start()
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
            put("系统版本", "Android $RELEASE SDK$SDK_INT")
            put("品牌型号", "$BRAND $MODEL")
            put("AndroidID", Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))//不可靠
            put("设备指纹", FINGERPRINT)
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
        val path = weatherLayout.createArcPath(0f, 0f)
        ValueAnimator.ofFloat(0f, 1f).apply {
            startDelay = 100L
            addUpdateListener(weatherLayout.getArcListener(path))
        }.start()
    }

    /**
     * 销毁定位
     */
    private fun destroyLocation() {
        if (null != mAMapLocationClient) {
            mAMapLocationClient!!.onDestroy()
            mAMapLocationClient = null
        }
    }

    /**
     * 默认的定位参数
     */
    private fun getAmapOption(): AMapLocationClientOption {
        val mOption = AMapLocationClientOption()
        mOption.apply {
            //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
            locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
            isGpsFirst = false//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
            httpTimeOut = 30000//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            interval = 2000//可选，设置定位间隔。默认为2秒
            isNeedAddress = true//可选，设置是否返回逆地理地址信息。默认是true
            isOnceLocation = true//可选，设置是否单次定位。默认是false
            isOnceLocationLatest = false//可选，是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
            isSensorEnable = false//可选，设置是否使用传感器。默认是false
            //可选，是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
            isWifiScan = true
            isLocationCacheEnable = true //可选，设置是否使用缓存定位，默认为true
        }
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP)//可选，
        // 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return mOption
    }

    override fun onItemTouchMove(viewHolder: RecyclerView.ViewHolder?) {
        mTouchHelper.startDrag(viewHolder)
    }

    override fun onItemClick(view: View, position: Int) {
        startActivity(Intent(this, mContentData[position].name))
    }

    override fun onBackPressed() {
        if (scrollView?.visibility == View.VISIBLE) concealAndHiddenAndroidInfo()
        else super.onBackPressed()
    }

    override fun finishWithAnim(): Boolean = false

}

