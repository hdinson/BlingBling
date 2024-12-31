package dinson.customview.utils

import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import dinson.customview.entity._025._025Schedule
import dinson.customview.entity.countdown.OnTheDay
import dinson.customview.model._005QiNiuConfig
import io.reactivex.Observable
import io.reactivex.functions.BiConsumer
import java.util.*
import java.util.concurrent.Callable
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.dropLastWhile
import kotlin.collections.first
import kotlin.collections.forEach
import kotlin.collections.isEmpty
import kotlin.collections.isNullOrEmpty
import kotlin.collections.joinToString
import kotlin.collections.listOf
import kotlin.collections.toTypedArray

object MMKVUtils {

    private val sAppConfig by lazy { MMKV.mmkvWithID("app_config") }
    private val sTimer by lazy { MMKV.mmkvWithID("schedule") }
    private val sTimerOnTheDay by lazy { MMKV.mmkvWithID("on_the_day") }
    private val sQiNiu by lazy { MMKV.mmkvWithID("qi_niu") }

    private const val KEY_CONFIG_TOP = "top"
    private const val KEY_CONFIG_CURRENCY = "currency"
    private const val KEY_CONFIG_QI_NIU = "qi_niu"


    /** 设置当前用户设置的币种 */
    fun setUserCurrency(currency: ArrayList<String>) {
        if (currency.size != 5) return
        sAppConfig.encode(KEY_CONFIG_CURRENCY, currency.joinToString(","))
    }

    /** 获取当前用户设置的币种 */
    fun getUserCurrency(): List<String>? {
        val value = sAppConfig.decodeString(KEY_CONFIG_CURRENCY, "")
        if (value.isEmpty()) return null

        val result = listOf(*value.split(",").dropLastWhile { it.isEmpty() }.toTypedArray())
        Observable.fromIterable(result)
            .filter { s -> s.isNotEmpty() }
            .collect(
                Callable<ArrayList<String>> { ArrayList() },
                BiConsumer { obj, e -> obj.add(e) })
            .subscribe()
        return result
    }


    /**
     * 获取当前所有的七牛云的设置文件
     * @return 第一个参数为配置列表。第二个参数为当前选中的域名，要么同时存在，要么同时为null
     */
    fun getQiNiuConfig(): Pair<ArrayList<_005QiNiuConfig>?, String?> {
        val domain = sAppConfig.decodeString(KEY_CONFIG_QI_NIU, "")
        if (domain.isEmpty()) {
            sQiNiu.clearAll()
            sAppConfig.removeValueForKey(KEY_CONFIG_QI_NIU)
            return Pair(null, null)
        }

        val ret = ArrayList<_005QiNiuConfig>()
        val gson = Gson()
        var isContainCurrent = false
        val allKeys = sQiNiu.allKeys()
        if (allKeys.isNullOrEmpty()) {
            sQiNiu.clearAll()
            sAppConfig.removeValueForKey(KEY_CONFIG_QI_NIU)
            return Pair(null, null)
        }
        allKeys.forEach { setting ->
            sQiNiu.decodeString(setting, "").let {
                if (it.isNotEmpty()) {
                    val element = gson.fromJson(it, _005QiNiuConfig::class.java)
                    if (element.Domain == domain) isContainCurrent = true
                    ret.add(element)
                } else {
                    sQiNiu.removeValueForKey(it)
                }
            }
        }
        if (ret.isEmpty()) {
            sQiNiu.clearAll()
            sAppConfig.removeValueForKey(KEY_CONFIG_QI_NIU)
            return Pair(null, null)
        }
        return if (!isContainCurrent) {
            setQiuNiuDefaultDomain(ret[0].Domain)
            Pair(ret, ret[0].Domain)
        } else Pair(ret, domain)
    }

    /**
     * 添加七牛云的设置信息到sp
     */
    fun addQiNiuConfig(config: _005QiNiuConfig) {
        val toJson = Gson().toJson(config)
        val allKeys = sQiNiu.allKeys()
        if (allKeys.isNullOrEmpty()) {
            setQiuNiuDefaultDomain(config.Domain)
        }
        sQiNiu.encode(config.Domain, toJson)
    }

    /**
     * 删除七牛云的设置信息
     */
    fun removeQiNiuConfig(config: _005QiNiuConfig) {
        if (sQiNiu.containsKey(config.Domain).not()) return
        val domain = sAppConfig.decodeString(KEY_CONFIG_QI_NIU, "")
        if (domain.isEmpty()) {
            sQiNiu.clearAll()
            return
        }
        sQiNiu.removeValueForKey(config.Domain)
        if (config.Domain == domain) {
            val allKeys = sQiNiu.allKeys()
            if (allKeys.isNullOrEmpty()) {
                sAppConfig.removeValueForKey(KEY_CONFIG_QI_NIU)
            } else sAppConfig.encode(KEY_CONFIG_QI_NIU, allKeys.first())
        }
    }

    /**
     * 设置七牛云默认选中的设置信息
     */
    fun setQiuNiuDefaultDomain(domain: String) {
        sAppConfig.encode(KEY_CONFIG_QI_NIU, domain)
    }

    /**
     * 更新七牛云配置信息，只支持对当前选中的配置信息进行修改
     */
    fun updateQiNiuDefault(config: _005QiNiuConfig) {
        val domain = sAppConfig.decodeString(KEY_CONFIG_QI_NIU, "")
        if (domain.isEmpty()) {
            sQiNiu.clearAll()
            return
        }
        if (sQiNiu.containsKey(domain)) {
            sQiNiu.removeValueForKey(domain)
        }
        sAppConfig.encode(KEY_CONFIG_QI_NIU, config.Domain)
        sQiNiu.encode(config.Domain, Gson().toJson(config))
    }


    /**
     * 获取倒数日日程
     */
    fun getScheduleList(): ArrayList<_025Schedule> {
        val result = ArrayList<_025Schedule>()
        val gson = Gson()
        val allKey = sTimer.allKeys()
        if (allKey == null || allKey.isEmpty()) return result
        sTimer.allKeys().forEach {
            val temp = sTimer.decodeString(it) ?: ""
            result.add(gson.fromJson(temp, _025Schedule::class.java))
        }
        return result
    }

    /**  根据id获取倒数日日程*/
    fun getScheduleById(id: String): _025Schedule? {
        if (id.isEmpty()) return null
        val json = sTimer.decodeString(id)
        if (json.isNullOrEmpty()) return null
        return Gson().fromJson(json, _025Schedule::class.java)
    }

    /** 添加倒数日日程*/
    fun addSchedule(schedule: _025Schedule) {
        sTimer.encode(schedule.id, Gson().toJson(schedule))
    }

    /**  删除倒数日日程*/
    fun deleteSchedule(id: String) {
        sTimer.removeValueForKey(id)
    }

    /** 置顶的日程 */
    fun setScheduleTop(id: String) {
        sAppConfig.encode(KEY_CONFIG_TOP, id)
    }

    /** 置顶的日程 */
    fun getScheduleTop() = sAppConfig.decodeString(KEY_CONFIG_TOP, "") ?: ""

    /** 获取历史上的今天 */
    fun getOnTheDay(key: String) = sTimerOnTheDay.decodeString(key, "") ?: ""

    /** 设置历史上的今天 */
    fun setOnTheDay(onTheDay: HashMap<String, ArrayList<OnTheDay>>) {
        val gson = Gson()
        onTheDay.forEach {
            sTimerOnTheDay.encode(it.key, gson.toJson(it.value))
        }
    }
}