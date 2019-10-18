package dinson.customview.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.View.OVER_SCROLL_NEVER
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.android.ActivityEvent
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter.MainContentAdapter
import dinson.customview.api.OneApi
import dinson.customview.api.XinZhiWeatherApi
import dinson.customview.entity.HomeWeather
import dinson.customview.entity.one.DailyList
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.click
import dinson.customview.kotlin.logd
import dinson.customview.listener.MainItemTouchHelper
import dinson.customview.listener.OnItemTouchMoveListener
import dinson.customview.model.HomeWeatherModelUtil
import dinson.customview.model.MainActivityModelUtil
import dinson.customview.utils.*
import dinson.customview.weight.recycleview.LinearItemDecoration
import dinson.customview.weight.recycleview.OnRvItemClickListener
import dinson.customview.weight.recycleview.RvItemClickSupport
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), OnItemTouchMoveListener {

    //private val mHeadData = ArrayList<DailyDetail>()
    private var mAMapLocationClient: AMapLocationClient? = null
    private lateinit var mTouchHelper: ItemTouchHelper
    private val mOneApi = HttpHelper.create(OneApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPullZoomView()
        initContent()
        initHead()
        getLocation()
        weatherLayout.click {
            RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE)
                .subscribe { AndroidInfoActivity.start(this) }
        }
    }

    /**
     * 初始化头部伸缩
     */
    private fun initPullZoomView() {
        mPullZoomView.apply {
            overScrollMode = OVER_SCROLL_NEVER
            isVerticalScrollBarEnabled = false
            setIsParallax(true)
            setIsZoomEnable(true)
            setSensitive(1.5f)
            setZoomTime(500)
        }
    }

    /**
     * 内容的数据集
     */
    private val mContentData = MainActivityModelUtil.dataList

    private fun initContent() {
        val mainAdapter = MainContentAdapter(mContentData, this)
        mTouchHelper = ItemTouchHelper(MainItemTouchHelper(mainAdapter))
        mTouchHelper.attachToRecyclerView(rvContent)
        rvContent.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(LinearItemDecoration(this@MainActivity))
        }
        RvItemClickSupport.addTo(rvContent).setOnItemClickListener(OnRvItemClickListener { _, _, position ->
            startActivity(Intent(this, mContentData[position].name))
        })
    }

    private fun initHead() {
        /*mainBanner.setPages(mHeadData, MainBannerHolder())
        mainBanner.setDuration(500)
        mainBanner.start()
        mainBanner.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                setDailyContent(mHeadData[position].data.hp_content)
            }
        })*/

        /*val mainHeardCache = CacheUtils.getMainHeardCache()
        Flowable.fromPublisher(if (mainHeardCache == null) mOneApi.daily else Flowable.just(mainHeardCache))
            .flatMap { list ->
                CacheUtils.setMainHeardCache(list)
                Flowable.fromIterable(list.data)
            }
            .flatMap { id ->
                val detail = CacheUtils.getDailyDetail(id)
                if (detail == null) mOneApi.getDetail(id) else Flowable.just(detail)
            }
            .filter { detail -> detail.data != null }
            .collect({ mHeadData }) { list, bean ->
                logv(bean.toString())
                CacheUtils.setDailyDetail(bean)
                list.add(bean)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _ ->
                //mainBanner.setPages(mHeadData, MainBannerHolder())
                if (mHeadData.isNotEmpty()) setDailyContent(mHeadData[0].data.hp_content)
            }) { throwable ->
                LogUtils.d(throwable.toString())
            }*/

        Observable
            .concat(Observable.create<DailyList> {
                val cache = AppCacheUtil.getMainHeardCache(this@MainActivity)
                if (cache == null) it.onComplete()
                else it.onNext(cache)
            }, mOneApi.loadDaily())
            .doOnNext {
                if (it.isLocalCache.not()) {
                    it.isLocalCache = true
                    AppCacheUtil.setMainHeardCache(this@MainActivity,it)
                }
            }
            .map { it.data[0] }
            .flatMap {
                val detail = AppCacheUtil.getDailyDetail(this@MainActivity,it)
                if (detail == null) mOneApi.getDetail(it) else Observable.just(detail)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe({
                if (it.isLocalCache.not()) {
                    it.isLocalCache = true
                    AppCacheUtil.setDailyDetail(this@MainActivity,it)
                }
                ivDaily.tag = null
                GlideUtils.setImage(this, it.data.hp_img_url, ivDaily)
            }, { LogUtils.d(it.toString()) }).addToManaged()
    }

    /**
     * 设置每日一图的详情
     */
    // fun setDailyContent(content: String) {
//        tvDailyContent.text = content.replace("from", "——")
//            .replace("by", "——")
    /* Observable.interval(10, TimeUnit.MILLISECONDS)
         .take(Math.max(subContent.length, subName.length).toLong())
         .compose(RxSchedulers.io_main())
         .subscribe({
             if (it < subName.length) tvDailyTitle.append(subName[it.toInt()].toString())
             if (it < subContent.length) tvDailyContent.append(subContent[it.toInt()].toString())
         })*/
    //}


    /**
     * 定位
     */
    private fun getLocation() {
        weatherLayout.click {
            RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE)
                .subscribe { AndroidInfoActivity.start(this) }
        }

        RxPermissions(this).request(Manifest.permission.ACCESS_COARSE_LOCATION)
            .subscribe { b ->
                if (!b) return@subscribe
                mAMapLocationClient = AMapLocationClient(this.applicationContext)
                mAMapLocationClient?.let {
                    it.setLocationListener(locationListener)//设置定位监听
                    it.setLocationOption(getAmapOption())//设置定位参数
                    it.startLocation()//开始定位
                }
            }.addToManaged()
    }

    private var locationListener = AMapLocationListener { location ->
        destroyLocation()//只定位一次
        if (null == location) {
            tvWeather.text = "定位失败"
            return@AMapLocationListener
        }
        if (location.errorCode != 0) return@AMapLocationListener  //errCode等于0代表定位成功

        val add = if (StringUtils.isEmpty(location.city)) location.province else location.city
        Observable.just<String>(add)
            .flatMap { city ->
                val cache = AppCacheUtil.getHomeWeatherCache(this@MainActivity,city)
                if (cache == null) HttpHelper.create(XinZhiWeatherApi::class.java).getWeather(city)
                else Observable.just(cache)
            }
            .map { homeWeather ->
                AppCacheUtil.setHomeWeatherCache(this@MainActivity,homeWeather)
                homeWeather
            }
            .compose(RxSchedulers.io_main())
            .subscribe({
                initWeatherLayout(it) //设置数据
            }, {
                tvWeather.text = add
            }).addToManaged()
    }

    private fun initWeatherLayout(weather: HomeWeather) {
        logd(".$weather")
        val resultsBean = weather.results[0]
        iconFontWeather.setText(HomeWeatherModelUtil.getWeatherFont(resultsBean.now.code))
        tvWeather.apply {
            typeface = TypefaceUtils.getAppleFont(this@MainActivity)
            text = String.format("%s℃", resultsBean.now.temperature)
        }
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
        viewHolder?.let { mTouchHelper.startDrag(it) }
    }

    override fun finishWithAnim(): Boolean = false
}