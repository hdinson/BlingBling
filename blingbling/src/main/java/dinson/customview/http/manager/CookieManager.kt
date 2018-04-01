package dinson.customview.http.manager

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Cookie管理器
 */
class CookieManager : CookieJar {
    private var cookieStore = InDiskCookieStore()

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        cookies.forEach { cookieStore.addCookie(url, it) }
    }

    override fun loadForRequest(url: HttpUrl) = cookieStore.getCookies(url)

    fun getCookieStore() = cookieStore
}
