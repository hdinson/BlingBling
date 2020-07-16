package dinson.customview.utils

import android.content.Context
import com.dinson.blingbase.utils.DateUtils
import com.google.gson.Gson
import dinson.customview.BuildConfig
import dinson.customview.entity.HomeWeather
import dinson.customview.entity.exchange.ExchangeBean
import dinson.customview.entity.one.DailyDetail
import dinson.customview.entity.one.DailyList
import dinson.customview.kotlin.logd

object AppCacheUtil {

    /**
     * 设置首页头部one缓存
     *
     * @param bean entity
     */
    fun setMainHeardCache(context: Context, bean: DailyList) {
        val json = Gson().toJson(bean)
        logd(false) { "<DailyList> Put Cache >> $json" }
        //缓存的时间是到凌晨4点
        val now = System.currentTimeMillis()
        val today4 = DateUtils.getDateOfTimestamp(0) * 1000L + 14400000
        if (now < today4)
            CacheUtils.setCache(context, "home_head_list", json, today4 - now)
        else {
            val deathLine = (DateUtils.getDateOfTimestamp(1) + 14400L) * 1000 - now
            CacheUtils.setCache(context, "home_head_list", json, deathLine)
        }
    }

    /**
     * 获取首页头部one缓存
     *
     * @return null表示没有数据
     */
    fun getMainHeardCache(context: Context): DailyList? {
        val homeList = CacheUtils.getCache(context, "home_head_list")
        logd(false) { "<DailyList> Get Cache << $homeList" }
        return if (homeList == null) null else Gson().fromJson(homeList, DailyList::class.java)
    }

    /**
     * 设置首页头部one详情缓存
     *
     * @param bean entity
     */
    fun setDailyDetail(context: Context, bean: DailyDetail) {
        val json = Gson().toJson(bean)
        logd(false) { "<DailyDetail> Put Cache >> $json" }
        CacheUtils.setCache(context, "home_heard_detail" + bean.data.hpcontent_id, json)
    }

    /**
     * 根据id获取首页头部one详情缓存
     *
     * @param id 数据id
     * @return null表示没有数据
     */
    fun getDailyDetail(context: Context, id: Int): DailyDetail? {
        val cache = CacheUtils.getCache(context, "home_heard_detail$id")
        logd(false) { "<DailyDetail> Get Cache << $cache" }
        return if (cache == null) null else Gson().fromJson<DailyDetail>(cache, DailyDetail::class.java)
    }


    /**
     * 设置首页头部one缓存
     *
     * @param bean entity
     */
    fun setHomeWeatherCache(context: Context, bean: HomeWeather) {
        val json = Gson().toJson(bean)
        logd(false) { "<HomeWeather> Put Cache >> $json" }
        CacheUtils.setCache(context, "lastKnowWeather", json, 3600000)//缓存时间1小时
    }

    /**
     * 获取首页头部one缓存
     *
     * @return null表示没有数据
     */
    fun getHomeWeatherCache(context: Context, city: String): HomeWeather? {
        val homeList = CacheUtils.getCache(context, "lastKnowWeather")
        if (homeList == null) {
            logd(false) { "<HomeWeather> is out of date or no exist !" }
            return null
        }
        val homeWeather = Gson().fromJson<HomeWeather>(homeList, HomeWeather::class.java)
        val cacheName = homeWeather.results[0].location.name
        if (cacheName.contains(city) || city.contains(cacheName)) {
            logd(false) { "<HomeWeather> Get Cache << $homeList" }
            return homeWeather
        }
        logd(false) { "<HomeWeather> LocationCity is change!" }
        return null
    }

    /**
     * 设置汇率兑换比率缓存
     *
     * @param beanStr entity序列化后
     */
    fun setExchangeRateCache(context: Context, beanStr: String) {
        if (BuildConfig.DEBUG) {
            val json = Gson().toJson(beanStr)
            logd(false) { "<ExchangeBean> Put Cache >> $json" }
        }
        CacheUtils.setCache(context, "lastKnowExchangeRate", beanStr)//缓存永久有效
    }

    /**
     * 获取首页头部one缓存
     *
     * @return null表示没有数据
     */
    fun getExchangeRateCache(context: Context): String? {
        val exchangeRate = CacheUtils.getCache(context, "lastKnowExchangeRate")
        if (exchangeRate == null) {
            logd(false) { "<ExchangeBean> is out of date or no exist !" }
            return null
        }
        if (BuildConfig.DEBUG) {
            val exchangeBean = Gson().fromJson(exchangeRate, ExchangeBean::class.java)
            logd(false) { "<ExchangeBean> Get Cache << $exchangeBean" }
        }
        return exchangeRate
    }
}