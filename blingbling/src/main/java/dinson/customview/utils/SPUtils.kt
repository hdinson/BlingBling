package dinson.customview.utils

import android.content.Context
import com.dinson.blingbase.RxBling
import com.google.gson.Gson
import dinson.customview.entity._025._025Schedule
import dinson.customview.entity.countdown.OnTheDay
import dinson.customview.model._005QiNiuConfig
import io.reactivex.Observable
import io.reactivex.functions.BiConsumer
import java.util.*
import java.util.concurrent.Callable
import kotlin.collections.ArrayList

/**
 * SharedPreferences工具类
 */
object SPUtils {






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