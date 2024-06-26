package dinson.customview.activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.ImageView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.dinson.blingbase.kotlin.click
import com.dinson.blingbase.kotlin.dip
import com.dinson.blingbase.rxcache.rxCache
import com.dinson.blingbase.rxcache.stategy.CacheStrategy
import com.dinson.blingbase.utils.TypefaceUtil
import com.dinson.blingbase.widget.recycleview.LinearSpaceDecoration
import com.dinson.blingbase.widget.recycleview.RvItemClickSupport
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.android.ActivityEvent
import dinson.customview._global.ConstantsUtils.APP_FONT_PATH
import dinson.customview._global.ViewBindingActivity
import dinson.customview.adapter.MainContentAdapter
import dinson.customview.api.OneApi
import dinson.customview.api.XinZhiWeatherApi
import dinson.customview.databinding.ActivityMainBinding
import dinson.customview.entity.HomeWeather
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.logd
import dinson.customview.kotlin.loge
import dinson.customview.kotlin.logi
import dinson.customview.listener.MainItemTouchHelper
import dinson.customview.listener.OnItemTouchMoveListener
import dinson.customview.model.HomeWeatherModelUtil
import dinson.customview.model.MainActivityModelUtil
import dinson.customview.utils.toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.stefan.library.mu5viewpager.Mu5Interface
import java.util.concurrent.TimeUnit

class MainActivity : ViewBindingActivity<ActivityMainBinding>(), OnItemTouchMoveListener, Mu5Interface {

    private val mHeadData = ArrayList<String>()
    private var mAMapLocationClient: AMapLocationClient? = null
    private lateinit var mTouchHelper: ItemTouchHelper
    private val mOneApi = HttpHelper.create(OneApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initContent()
        initHead()
        getLocation()
        binding.weatherLayout.click {
             RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_MMS, Manifest.permission.READ_CALL_LOG)
                 .subscribe {
                     if (it) AndroidInfoActivity.start(this)
                     else {
                         "需要同意权限才能查看设备信息".toast()
                         val intent = Intent()
                         intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                         intent.data = Uri.parse("package:" + this.packageName)
                         startActivity(intent)
                     }
                 }
        }
    }


    /**
     * 内容的数据集
     */
    private val mContentData = MainActivityModelUtil.dataList

    private fun initContent() {
        val mainAdapter = MainContentAdapter(mContentData, this)
        mTouchHelper = ItemTouchHelper(MainItemTouchHelper(mainAdapter))
        mTouchHelper.attachToRecyclerView(binding.rvContent)
        binding.rvContent.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(LinearSpaceDecoration.Builder()
                .spaceTB(dip(1)).build())
        }
        RvItemClickSupport.addTo(binding.rvContent).setOnItemClickListener { _, _, position ->
            startActivity(Intent(this, mContentData[position].name))
        }
        //mu5Viewpager.setData(mHeadData, this)
    }

    /**
     * 加载one 模块
     */
    private fun initHead() {
        mOneApi.loadDaily()
            .rxCache("api_one_daily", CacheStrategy.firstCacheTimeout(12 ,TimeUnit.HOURS))
            .flatMap {
                logi { "${it.data.data}" }
                Observable.fromIterable(it.data.data)
            }
            .flatMap {
                mOneApi.getDetail(it).rxCache("one_daily_detail_$it", CacheStrategy.firstCache())
            }
            .map {
                logi { "${it.data.data}" }
                it.data
            }
            .toSortedList { o1, o2 ->
                val before = o1.data?.hpcontent_id ?: 0
                val after = o2.data?.hpcontent_id ?: 0
                return@toSortedList after.compareTo(before)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe({ list ->
                mHeadData.clear()
                mHeadData.addAll(list.filter { it.data != null }.map { it.data!!.hp_img_url })
                binding.mu5Viewpager.setData(mHeadData, this)
            }, {
                loge(it::toString)
            }).addToManaged()
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
        binding.weatherLayout.click {
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
            binding.tvWeather.text = "定位失败"
            return@AMapLocationListener
        }
        if (location.errorCode != 0) return@AMapLocationListener  //errCode等于0代表定位成功

        val city = if (location.city.isNullOrEmpty()) location.province else location.city
        HttpHelper.create(XinZhiWeatherApi::class.java).getWeather(city)
            .rxCache("api_home_weather_cache", CacheStrategy.firstCacheTimeout(1,TimeUnit.HOURS))
            .compose(RxSchedulers.io_main())
            .subscribe({
                initWeatherLayout(it.data) //设置数据
            }, {
                binding.tvWeather.text = city
                loge(it::toString)
            })
    }

    private fun initWeatherLayout(weather: HomeWeather) {
        logd { ".$weather" }
        val resultsBean = weather.results[0]
        binding.iconFontWeather.setText(HomeWeatherModelUtil.getWeatherFont(resultsBean.now.code))
        binding.tvWeather.apply {
            typeface = TypefaceUtil.getFontFromAssets(this@MainActivity, APP_FONT_PATH)
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

    override fun onIndexChange(p0: Int) {
        //"$p0/${datas.size}".loge()
    }

    override fun onLoadImage(p0: ImageView, url: String, p2: Int) {
        Glide.with(this).asBitmap().load(url)
            .into(object : SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.mu5Viewpager.bindSource(resource, p2, p0)
                }
            })
    }
}