package dinson.customview.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter.MainContentAdapter
import dinson.customview.adapter.MainHeadAdapter
import dinson.customview.api.OneApi
import dinson.customview.api.XinZhiWeatherApi
import dinson.customview.entity.ClassBean
import dinson.customview.entity.HomeWeather
import dinson.customview.entity.one.DailyDetail
import dinson.customview.http.BaseObserver
import dinson.customview.http.HttpHelper
import dinson.customview.listener.MainItemTouchHelper
import dinson.customview.listener.OnItemTouchMoveListener
import dinson.customview.model.HomeWeatherModelUtil
import dinson.customview.utils.CacheUtils
import dinson.customview.utils.LogUtils
import dinson.customview.utils.TypefaceUtils
import dinson.customview.weight.recycleview.LinearItemDecoration
import dinson.customview.weight.recycleview.OnItemClickListener
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.aspect_ratio_iv_layout.*

class MainActivity : BaseActivity(), OnItemTouchMoveListener, OnItemClickListener.OnClickListener {

    //private val mContentData = ArrayList<ClassBean>()
    private val mHeadData = ArrayList<DailyDetail>()
    private lateinit var mMainHeadAdapter: MainHeadAdapter
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
        listOf(ClassBean(getString(R.string.facebook_title), getString(R.string.facebook_desc),
            _001ShimmerActivity::class.java, getString(R.string.facebook_img)),
            ClassBean(getString(R.string.qqnavi_title), getString(R.string.qqnavi_desc),
                _002QQNaviViewActivity::class.java, getString(R.string.qqnavi_img)),
            ClassBean(getString(R.string.exchange_rate_title), getString(R.string.exchange_rate_desc),
                _003ExchangeActivity::class.java, getString(R.string.exchange_rate_img)),
            ClassBean(getString(R.string.ganged_recycle_title), getString(R.string.ganged_recycle_desc),
                _004GangedRecycleViewActivity::class.java, getString(R.string.ganged_recycle_img)),
            ClassBean(getString(R.string.like_smile_title), getString(R.string.like_smile_desc),
                _005LikeSmileViewActivity::class.java, getString(R.string.like_smile_img)),
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
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(LinearItemDecoration(this@MainActivity))
            addOnItemTouchListener(OnItemClickListener(this@MainActivity, rvContent, this@MainActivity))
        }
    }

    private fun initHead() {
        rvHead.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mMainHeadAdapter = MainHeadAdapter(this, mHeadData)
        rvHead.adapter = mMainHeadAdapter
        //点击轮播图片，获取源文件，执行跳转动画
        rvHead.addOnItemTouchListener(OnItemClickListener(this,
            rvHead, OnItemClickListener.OnClickListener { view, position ->
            Single.just(mHeadData[position].data.hp_img_url)
                .map { s ->
                    Glide.with(this@MainActivity).load(s).downloadOnly(Target.SIZE_ORIGINAL,
                        Target.SIZE_ORIGINAL).get().path
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { path ->
                    LogUtils.e("CurrentItem:" + position + " imgUrl:" + mHeadData[position].data.hp_img_url + "  imgPath:" + path)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, view, "dailyPic")
                    DailyPicActivity.start(this@MainActivity, mHeadData[position].data, path, options)
                }
        }))

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
                LogUtils.d(bean.toString())
                CacheUtils.setDailyDetail(bean)
                list.add(bean)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _ -> mMainHeadAdapter.notifyDataSetChanged() }) { throwable ->
                LogUtils.d(throwable.toString())
                vsContent.inflate()
                ivImg.setImageResource(R.drawable.def_img)
            }
    }

    private fun getLocation() {
        mAMapLocationClient = AMapLocationClient(this.applicationContext)
        mAMapLocationClient?.let {
            it.setLocationListener(locationListener)//设置定位监听
            it.setLocationOption(getAmapOption())//设置定位参数
            it.startLocation()//开始定位
        }
    }


    private var locationListener = AMapLocationListener { location ->
        // TODO: 2017/9/27 定位权限判断
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
        LogUtils.d("." + weather.toString())
        weatherLayout.visibility = View.VISIBLE
        val resultsBean = weather.results[0]
        iconFontWeather.apply {
            setText(HomeWeatherModelUtil.getWeatherFont(resultsBean.now.code))
            typeface = TypefaceUtils.get(this@MainActivity, "fonts/FZLanTingHeiS_Regular.ttf")
            text = String.format("%s℃", resultsBean.now.temperature)
        }

        val alphaAnimator = ObjectAnimator.ofFloat(weatherLayout, "alpha", 0f, 1f)
        val scaleX = ObjectAnimator.ofFloat(weatherLayout, "scaleX", 0f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(weatherLayout, "scaleY", 0f, 1.0f)
        val set = AnimatorSet()
        set.interpolator = DecelerateInterpolator()
        set.play(alphaAnimator).with(scaleX).with(scaleY)
        set.start()
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

    override fun onItemClick(view: View?, position: Int) {
        startActivity(Intent(this, mContentData[position].name))
    }


    override fun finishWithAnim(): Boolean = false

}
