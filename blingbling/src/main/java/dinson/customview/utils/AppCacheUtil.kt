package dinson.customview.utils

import com.google.gson.Gson
import dinson.customview._global.GlobalApplication
import dinson.customview.entity.HomeWeather
import dinson.customview.entity.exchange.ExchangeBean
import dinson.customview.entity.one.DailyDetail
import dinson.customview.entity.one.DailyList

object AppCacheUtil{

    /**
     * 设置首页头部one缓存
     *
     * @param bean entity
     */
    fun setMainHeardCache(bean: DailyList) {
        val json = Gson().toJson(bean)
        LogUtils.d("<DailyList> Put Cache >> $json", false)

        //缓存的时间是到凌晨4点
        val now = System.currentTimeMillis()
        val today4 = DateUtils.getDataTimestamp(0) * 1000L + 14400000
        if (now < today4)
            CacheUtils.setCache(GlobalApplication.getContext(),"home_head_list", json, today4 - now)
        else {
            val deathLine = (DateUtils.getDataTimestamp(1) + 14400L) * 1000 - now
            CacheUtils.setCache(GlobalApplication.getContext(),"home_head_list", json, deathLine)
        }
    }

    /**
     * 获取首页头部one缓存
     *
     * @return null表示没有数据
     */
    fun getMainHeardCache(): DailyList? {
        val homeList = CacheUtils.getCache(GlobalApplication.getContext(),"home_head_list")
        LogUtils.d("<DailyList> Get Cache << $homeList", false)
        return if (homeList == null) null else Gson().fromJson<DailyList>(homeList, DailyList::class.java)
    }

    /**
     * 设置首页头部one详情缓存
     *
     * @param bean entity
     */
    fun setDailyDetail(bean: DailyDetail) {
        val json = Gson().toJson(bean)
        LogUtils.d("<DailyDetail> Put Cache >> $json", false)
        CacheUtils.setCache(GlobalApplication.getContext(),"home_heard_detail" + bean.data.hpcontent_id, json)
    }

    /**
     * 根据id获取首页头部one详情缓存
     *
     * @param id 数据id
     * @return null表示没有数据
     */
    fun getDailyDetail(id: Int): DailyDetail? {
        val cache = CacheUtils.getCache(GlobalApplication.getContext(),"home_heard_detail$id")
        LogUtils.d("<DailyDetail> Get Cache << $cache", false)
        return if (cache == null) null else Gson().fromJson<DailyDetail>(cache, DailyDetail::class.java)
    }


    /**
     * 设置首页头部one缓存
     *
     * @param bean entity
     */
    fun setHomeWeatherCache(bean: HomeWeather) {
        val json = Gson().toJson(bean)
        LogUtils.d("<HomeWeather> Put Cache >> $json", false)
        CacheUtils.setCache(GlobalApplication.getContext(),"lastKnowWeather", json, 3600000)//缓存时间1小时
    }

    /**
     * 获取首页头部one缓存
     *
     * @return null表示没有数据
     */
    fun getHomeWeatherCache(city: String): HomeWeather? {
        val homeList = CacheUtils.getCache(GlobalApplication.getContext(),"lastKnowWeather")
        if (homeList == null) {
            LogUtils.d("<HomeWeather> is out of date or no exist !", false)
            return null
        }
        val homeWeather = Gson().fromJson<HomeWeather>(homeList, HomeWeather::class.java)
        val cacheName = homeWeather.results[0].location.name
        if (cacheName.contains(city) || city.contains(cacheName)) {
            LogUtils.d("<HomeWeather> Get Cache << $homeList", false)
            return homeWeather
        }
        LogUtils.d("<HomeWeather> LocationCity is change!", false)
        return null
    }

    /**
     * 设置汇率兑换比率缓存
     *
     * @param beanStr entity序列化后
     */
    fun setExangeRateCache(beanStr: String) {
        if (GlobalApplication.IS_DEBUG) {
            val json = Gson().toJson(beanStr)
            LogUtils.d("<ExchangeBean> Put Cache >> $json", false)
        }
        CacheUtils.setCache(GlobalApplication.getContext(),"lastKnowExchangeRate", beanStr)//缓存永久有效
    }

    /**
     * 获取首页头部one缓存
     *
     * @return null表示没有数据
     */
    fun getExchangeRateCache(): String? {
        val exchangeRate = CacheUtils.getCache(GlobalApplication.getContext(),"lastKnowExchangeRate")
        if (exchangeRate == null) {
            LogUtils.d("<ExchangeBean> is out of date or no exist !", false)
            return null
        }
        if (GlobalApplication.IS_DEBUG) {
            val exchangeBean = Gson().fromJson<ExchangeBean>(exchangeRate, ExchangeBean::class.java)
            LogUtils.d("<ExchangeBean> Get Cache << $exchangeBean", false)
        }
        return exchangeRate
    }
}