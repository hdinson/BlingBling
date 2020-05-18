package dinson.customview.utils

import android.content.Context
import com.google.gson.Gson
import dinson.customview._global.GlobalApplication
import dinson.customview.entity.countdown.OnTheDay
import dinson.customview.model._005QiNiuConfig
import dinson.customview.model._025Schedule
import io.reactivex.Observable
import io.reactivex.functions.BiConsumer
import java.util.*
import java.util.concurrent.Callable
import kotlin.collections.ArrayList

/**
 * SharedPreferences工具类
 */
object SPUtils {

    /** 设置当前用户设置的币种 */
    fun setUserCurrency(currency: ArrayList<String>) {
        if (currency.size != 5) return
        putString(GlobalApplication.getContext(), "config", "currency", currency.joinToString(","))
    }

    /** 获取当前用户设置的币种 */
    fun getUserCurrency(): List<String>? {
        val value = getString(GlobalApplication.getContext(), "config", "currency", "")
        if (value.isEmpty()) return null

        val result = Arrays.asList(*value.split(",").dropLastWhile { it.isEmpty() }.toTypedArray())
        Observable.fromIterable(result)
            .filter { s -> !StringUtils.isEmpty(s) }
            .collect(Callable<ArrayList<String>> { ArrayList() },
                BiConsumer<ArrayList<String>, String> { obj, e -> obj.add(e) })
            .subscribe()
        return result
    }

    /**
     * 判断是否应该加载默认的应用图标
     */
    fun isDefaultAppIcon(context: Context): Boolean {
        val aBoolean = getBoolean(context, "config", "icon", false)
        putBoolean(context, "config", "icon", !aBoolean)
        return aBoolean
    }

    /**
     * 获取当前所有的七牛云的设置文件
     * @return 第一个参数为配置列表。第二个参数为当前选中的域名，要么同时存在，要么同时为null
     */
    fun getQiNiuConfig(ctx: Context): Pair<ArrayList<_005QiNiuConfig>?, String?> {
        val sp = ctx.getSharedPreferences("QiNiu", Context.MODE_PRIVATE)
        val domain = getQiuNiuDefaultDomain(ctx)
        if (domain.isEmpty()) {
            sp.edit().clear().apply()
            clearQiuNiuDefaultDomain(ctx)
            return Pair(null, null)
        }

        val ret = ArrayList<_005QiNiuConfig>()
        val gson = Gson()
        var isContainCurrent = false
        sp.all.keys.forEach {
            sp.getString(it, "").let {
                if (it.isNotEmpty()) {
                    val element = gson.fromJson(it, _005QiNiuConfig::class.java)
                    if (element.Domain == domain) isContainCurrent = true
                    ret.add(element)
                } else {
                    sp.edit().remove(it).apply()
                }
            }
        }
        if (ret.isEmpty()) {
            sp.edit().clear().apply()
            clearQiuNiuDefaultDomain(ctx)
            return Pair(null, null)
        }
        return if (!isContainCurrent) {
            setQiuNiuDefaultDomain(ctx, ret[0].Domain)
            Pair(ret, ret[0].Domain)
        } else Pair(ret, domain)
    }

    /**
     * 添加七牛云的设置信息到sp
     */
    fun addQiNiuConfig(ctx: Context, config: _005QiNiuConfig) {
        val toJson = Gson().toJson(config)
        val sp = ctx.getSharedPreferences("QiNiu", Context.MODE_PRIVATE)
        if (sp.all.isEmpty()) {
            setQiuNiuDefaultDomain(ctx, config.Domain)
        }
        sp.edit().putString(config.Domain, toJson).apply()
    }

    /**
     * 删除七牛云的设置信息
     */
    fun removeQiNiuConfig(ctx: Context, config: _005QiNiuConfig) {
        val sp = ctx.getSharedPreferences("QiNiu", Context.MODE_PRIVATE)
        if (!sp.all.containsKey(config.Domain)) return
        val domain = getQiuNiuDefaultDomain(ctx)
        if (domain.isEmpty()) {
            sp.edit().clear().apply()
            return
        }
        sp.edit().remove(config.Domain).apply()
        if (config.Domain == domain) {
            if (sp.all.isEmpty()) clearQiuNiuDefaultDomain(ctx)
            else setQiuNiuDefaultDomain(ctx, sp.all.keys.first())
        }
    }

    /**
     * 设置七牛云默认选中的设置信息
     */
    fun setQiuNiuDefaultDomain(ctx: Context, domain: String) {
        putString(ctx, "config", "QiNiu", domain)
    }

    /**
     * 获取七牛云默认选中的设置信息
     */
    private fun getQiuNiuDefaultDomain(ctx: Context) = getString(ctx, "config", "QiNiu", "")

    /**
     * 获取七牛云默认选中的设置信息
     */
    private fun clearQiuNiuDefaultDomain(ctx: Context) {
        val sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE)
        sp.edit().remove("QiNiu").apply()
    }

    /**
     * 更新七牛云配置信息，只支持对当前选中的配置信息进行修改
     */
    fun updateQiNiuDefault(ctx: Context, config: _005QiNiuConfig) {
        val sp = ctx.getSharedPreferences("QiNiu", Context.MODE_PRIVATE)
        val domain = getQiuNiuDefaultDomain(ctx)
        if (domain.isEmpty()) {
            sp.edit().clear().apply()
            return
        }
        if (sp.all.containsKey(domain)) {
            sp.edit().remove(domain).apply()
        }
        setQiuNiuDefaultDomain(ctx, config.Domain)
        sp.edit().putString(config.Domain, Gson().toJson(config)).apply()
    }

    /**
     * 获取倒数日日程
     */
    fun getScheduleList(context: Context): ArrayList<_025Schedule> {
        val sp = context.getSharedPreferences("schedule", Context.MODE_PRIVATE)
        val result = ArrayList<_025Schedule>()
        val gson = Gson()
        sp.all.forEach {
            result.add(gson.fromJson(it.value.toString(), _025Schedule::class.java))
        }
        return result
    }

    /**
     * 根据id获取倒数日日程
     */
    fun getScheduleById(context: Context, id: String): _025Schedule? {
        if (StringUtils.isEmpty(id)) return null
        val sp = context.getSharedPreferences("schedule", Context.MODE_PRIVATE)
        val json = sp.getString(id, "")
        if (StringUtils.isEmpty(json)) return null
        return Gson().fromJson(json, _025Schedule::class.java)
    }

    /**
     * 添加倒数日日程
     */
    fun addSchedule(context: Context, schedule: _025Schedule) {
        val sp = context.getSharedPreferences("schedule", Context.MODE_PRIVATE)
        sp.edit().putString(schedule.id.toString(), Gson().toJson(schedule)).apply()
    }

    /**
     * 删除倒数日日程
     */
    fun deleteSchedule(context: Context, id: String) {
        context.getSharedPreferences("schedule", Context.MODE_PRIVATE)
            .edit().remove(id).apply()
    }

    /** 置顶的日程 */
    fun setScheduleTop(context: Context, id: String) {
        putString(context, "common", "top", id)
    }

    /** 置顶的日程 */
    fun getScheduleTop(context: Context) = getString(context,
        "common", "top", "")

    fun getOnTheDay(context: Context, key: String) =
        getString(context, "daily", key, "")


    fun setOnTheDay(context: Context, onTheDay: HashMap<String, ArrayList<OnTheDay>>) {
        val edit = context.getSharedPreferences("daily", Context.MODE_PRIVATE).edit()
        val gson = Gson()
        onTheDay.forEach {
            edit.putString(it.key, gson.toJson(it.value))
        }
        edit.apply()
    }

    /******************************************************************************************************/
    /**                             内部实现                                                              **/
    /******************************************************************************************************/
    fun getBoolean(ctx: Context, fileName: String, key: String, defValue: Boolean): Boolean {
        val sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return sp.getBoolean(key, defValue)
    }

    fun putBoolean(ctx: Context, fileName: String, key: String, value: Boolean) {
        val sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        sp.edit().putBoolean(key, value).apply()
    }

    fun putString(ctx: Context, fileName: String, key: String, value: String) {
        val sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        sp.edit().putString(key, value).apply()
    }

    fun getString(ctx: Context, fileName: String, key: String, defValue: String): String {
        val sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return sp.getString(key, defValue) ?: ""
    }

    fun putInt(ctx: Context, fileName: String, key: String, value: Int) {
        val sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        sp.edit().putInt(key, value).apply()
    }

    fun getInt(ctx: Context, fileName: String, key: String, defValue: Int): Int {
        val sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return sp.getInt(key, defValue)
    }
}