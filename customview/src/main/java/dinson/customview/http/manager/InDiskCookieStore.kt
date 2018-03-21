package dinson.customview.http.manager

import android.content.Context
import com.google.gson.Gson
import dinson.customview._global.ConstantsUtils
import dinson.customview._global.GlobalApplication
import dinson.customview.utils.AESUtils
import dinson.customview.utils.StringUtils
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

/**
 *  持久化缓存cookie
 */
class InDiskCookieStore {
    companion object {
        private const val COOKIE_PREFS_FLIE_NAME = "Cookies_Prefs666"
    }

    private val cookiePrefs = GlobalApplication.getContext()
        .getSharedPreferences(COOKIE_PREFS_FLIE_NAME, Context.MODE_PRIVATE)
    private val cookies = HashMap<String, ConcurrentHashMap<String, Cookie>>()

    init {
        //将持久化的cookies缓存到内存中 即map cookies
        cookiePrefs.all.filter { it.key.contains("@") }.forEach {
            val split = it.key.split("@")
            if (!cookies.containsKey(split[1])) cookies[split[1]] = ConcurrentHashMap()

            val sp = cookiePrefs.getString(it.key, "")
            if (StringUtils.isNotEmpty(sp)) {
                val cookie = decodeCookie(sp)
                cookies[split[1]]!![cookie.name()] = cookie
            }
        }
    }

    /**
     * 添加cookie
     * （本地加内存都存储）
     */
    fun addCookie(url: HttpUrl, cookie: Cookie) {
        val name = getCookieToken(cookie)
        //将cookies缓存到内存中 如果缓存过期 就重置此cookie
        if (!cookie.persistent()) {
            if (!cookies.containsKey(url.host())) {
                cookies[url.host()] = ConcurrentHashMap()
            }
            //todo 待验证，可能要存带@的name
            cookies[url.host()]!![cookie.name()] = cookie
        } else {
            if (cookies.containsKey(url.host())) {
                cookies[url.host()]!!.remove(name)
            }
        }
        //将cookies持久化到本地
        cookiePrefs.edit().putString(name, AESUtils.encrypt(ConstantsUtils.PACKAGE_NAME, encodeCookie(cookie))).apply()
    }


    /**
     * 获取单个cookie
     */
    fun getCookies(url: HttpUrl): List<Cookie> {
        val ret = ArrayList<Cookie>()
        if (cookies.containsKey(url.host()))
            ret.addAll(cookies[url.host()]!!.values)
        return ret
    }


    /**
     * 获取所有的cookie
     */
    fun getCookies(): List<Cookie> {
        val ret = ArrayList<Cookie>()
        for (key in cookies.keys)
            ret.addAll(cookies[key]!!.values)
        return ret
    }


    /**
     * 格式化cookie名称
     */
    private fun getCookieToken(cookie: Cookie): String = cookie.name() + "@" + cookie.domain()

    /**
     * cookies 序列化成 string
     *
     */
    private fun encodeCookie(cookie: Cookie) = Gson().toJson(cookie)

    /**
     * 将字符串反序列化成cookies
     */
    private fun decodeCookie(cookieString: String): Cookie =
        Gson().fromJson(AESUtils.decrypt(ConstantsUtils.PACKAGE_NAME, cookieString), Cookie::class.java)


}