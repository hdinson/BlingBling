package dinson.customview.utils

import android.content.Context
import android.os.Environment
import com.google.gson.Gson
import dinson.customview._global.GlobalApplication
import dinson.customview.entity.HomeWeather
import dinson.customview.entity.exchange.ExchangeBean
import dinson.customview.entity.one.DailyDetail
import dinson.customview.entity.one.DailyList
import dinson.customview.kotlin.logi
import java.io.File
import java.math.BigDecimal


object CacheUtils {


    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     *
     */
    private fun cleanInternalCache(context: Context) {
        deleteFilesByDirectory(context.cacheDir)
    }

    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     *
     * @param context
     */
    fun cleanDatabases(context: Context) {
        deleteFilesByDirectory(File("/data/data/"
            + context.packageName + "/databases"))
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     *
     * @param context
     */
    fun cleanSharedPreference(context: Context) {
        deleteFilesByDirectory(File("/data/data/"
            + context.packageName + "/shared_prefs"))
    }

    /**
     * * 按名字清除本应用数据库 * *
     *
     * @param context
     * @param dbName
     */
    fun cleanDatabaseByName(context: Context, dbName: String) {
        context.deleteDatabase(dbName)
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     *
     * @param context
     */
    fun cleanFiles(context: Context) {
        deleteFilesByDirectory(context.filesDir)
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    fun cleanExternalCache(context: Context) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteFilesByDirectory(context.externalCacheDir!!)
        }
    }

    /**
     * * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * *
     *
     * @param filePath
     */
    fun cleanCustomCache(filePath: String) {
        deleteFilesByDirectory(File(filePath))
    }

    /**
     * * 清除本应用所有的数据 * *
     *
     * @param context
     * @param filepath
     */
    fun cleanApplicationData(context: Context, vararg filepath: String?) {
        cleanInternalCache(context)
        cleanExternalCache(context)
        cleanDatabases(context)
        cleanSharedPreference(context)
        cleanFiles(context)
        for (filePath in filepath) {
            filePath?.apply { cleanCustomCache(this) }
        }
    }

    /**
     * * 清除本应用缓存的数据 * *
     *
     * @param context
     */
    fun cleanApplicationCacheData(context: Context) {
        cleanInternalCache(context)
        cleanExternalCache(context)
    }


    fun getCacheSize(context: Context): String {
        val cacheSize = getFolderSize(context.cacheDir)
        val externalSize = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            getFolderSize(context.externalCacheDir!!).toDouble()
        } else 0.00
        return getFormatSize(cacheSize + externalSize)
    }


    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (i in fileList.indices) {
                // 如果下面还有文件
                size += if (fileList[i].isDirectory) {
                    getFolderSize(fileList[i])
                } else {
                    fileList[i].length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 删除指定目录下文件及目录
     */
    fun deleteFilesByDirectory(file: File, deleteThisPath: Boolean = false) {
        try {
            if (file.isDirectory) {// 如果下面还有文件
                val files = file.listFiles()
                for (i in files.indices) {
                    deleteFilesByDirectory(files[i], true)
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory) {// 如果是文件，删除
                    file.delete()
                } else {// 目录
                    if (file.listFiles().isEmpty()) {// 目录下没有文件或者目录，删除
                        file.delete()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 格式化单位
     */
    private fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return size.toString() + "B"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }


    fun setCache(context: Context, url: String, json: String, deathLine: Long = 0) {
        val death = if (deathLine != 0L) System.currentTimeMillis() + deathLine else 0L
        val cacheFile = File(context.externalCacheDirs[0].path, MD5.encode(url))
        cacheFile.bufferedWriter().use {
            it.write("$death\n")
            it.write(json)
        }
    }

    fun getCache(context: Context, url: String): String? {
        val cacheFile = File(context.externalCacheDirs[0].path, MD5.encode(url))
        if (cacheFile.exists()) {
            cacheFile.bufferedReader().apply {
                val deathLine = this.readLine().toLong()
                return if (deathLine != 0L && System.currentTimeMillis() > deathLine) {
                    null
                } else this.readLines().joinToString()
            }
        }
        return null
    }

    /**
     * 设置首页头部one缓存
     *
     * @param bean entity
     */
    fun setMainHeardCache(context:Context,bean: DailyList) {
        val json = Gson().toJson(bean)
        LogUtils.d("<DailyList> Put Cache >> $json", false)

        //缓存的时间是到凌晨4点
        val now = System.currentTimeMillis()
        val today4 = DateUtils.getDataTimestamp(0) * 1000L + 14400000
        if (now < today4)
            setCache(context,"home_head_list", json, today4 - now)
        else {
            val deathLine = (DateUtils.getDataTimestamp(1) + 14400L) * 1000 - now
            setCache(context,"home_head_list", json, deathLine)
        }
    }

    /**
     * 获取首页头部one缓存
     *
     * @return null表示没有数据
     */
    fun getMainHeardCache(context: Context): DailyList? {
        val homeList = getCache(context,"home_head_list")
        LogUtils.d("<DailyList> Get Cache << " + homeList!!, false)
        return if (homeList == null) null else Gson().fromJson(homeList, DailyList::class.java)
    }

    /**
     * 设置首页头部one详情缓存
     *
     * @param bean entity
     */
    fun setDailyDetail(context: Context,bean: DailyDetail) {
        val json = Gson().toJson(bean)
        setCache(context,"home_heard_detail" + bean.data.hpcontent_id, json)
    }

    /**
     * 根据id获取首页头部one详情缓存
     *
     * @param id 数据id
     * @return null表示没有数据
     */
    fun getDailyDetail(context: Context,id: Int): DailyDetail? {
        val cache = getCache(context,"home_heard_detail$id") ?: return null
        return Gson().fromJson(cache, DailyDetail::class.java)
    }


    /**
     * 设置首页头部one缓存
     *
     * @param bean entity
     */
    fun setHomeWeatherCache(context: Context,bean: HomeWeather) {
        val json = Gson().toJson(bean)
        LogUtils.d("<HomeWeather> Put Cache >> $json", false)
        setCache(context,"lastKnowWeather", json, 3600000)//缓存时间1小时
    }

    /**
     * 获取首页头部one缓存
     *
     * @return null表示没有数据
     */
    fun getHomeWeatherCache(context: Context,city: String): HomeWeather? {
        val homeList = getCache(context,"lastKnowWeather")
        if (homeList == null) {
            LogUtils.d("<HomeWeather> is out of date or no exist !", false)
            return null
        }
        val homeWeather = Gson().fromJson(homeList, HomeWeather::class.java)
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
    fun setExangeRateCache(context: Context,beanStr: String) {
        if (GlobalApplication.IS_DEBUG) {
            val json = Gson().toJson(beanStr)
            LogUtils.d("<ExchangeBean> Put Cache >> $json", false)
        }
        setCache(context,"lastKnowExchangeRate", beanStr)//缓存永久有效
    }

    /**
     * 获取首页头部one缓存
     *
     * @return null表示没有数据
     */
    fun getExchangeRateCache(context: Context): String? {
        val exchangeRate = getCache(context,"lastKnowExchangeRate")
        if (exchangeRate == null) {
            LogUtils.d("<ExchangeBean> is out of date or no exist !", false)
            return null
        }
        if (GlobalApplication.IS_DEBUG) {
            val exchangeBean = Gson().fromJson(exchangeRate, ExchangeBean::class.java)
            LogUtils.d("<ExchangeBean> Get Cache << $exchangeBean", false)
        }
        return exchangeRate
    }

}
