package dinson.customview.utils

import android.content.Context
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
        putString(UIUtils.getContext(), "config", "currency", currency.joinToString(","))
    }

    /** 获取当前用户设置的币种 */
    fun getUserCurrency(): List<String>? {
        val value = getString(UIUtils.getContext(), "config", "currency", "")
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
        return sp.getString(key, defValue)
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