package dinson.customview.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.view.Window
import android.view.WindowManager
import dinson.customview.kotlin.getStatusBarHeight
import java.util.regex.Pattern

/**
 * 状态栏模式工具（目前支持MIUI6以上,Flyme4以上,Android M以上）
 * 黑白模式
 */
@SuppressLint("ObsoleteSdkInt", "PrivateApi")
object SystemBarModeUtils {
    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/
    /**
     * 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)
     */
    fun darkMode(activity: Activity, isDark: Boolean) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setDarkModeForM(activity.window, isDark)
            }
            isFlyme4LaterInner() -> setDarkModeForFlyme4(activity.window, isDark)
            isMIUI6LaterInner() -> setDarkModeForMIUI6(activity.window, isDark)
        }
    }

    /** 增加View的paddingTop,增加的值为状态栏高度*/
    fun setPaddingTop(context: Context, vararg view: View) {
        if (Build.VERSION.SDK_INT < 19) return
        view.forEach {
            val lp = it.layoutParams
            val statusBarHeight = context.getStatusBarHeight()
            if (lp == null || lp.height == 0) return@forEach
            if (lp.height > 0) lp.height += statusBarHeight//增高
            it.setPadding(it.paddingLeft, it.paddingTop + statusBarHeight,
                it.paddingRight, it.paddingBottom)
        }
    }

    /** 判断是否为MIUI6以上  */
    fun isMIUI6Later() = isMIUI6LaterInner()

    /** 判断是否Flyme4以上  */
    fun isFlyme4Later() = isFlyme4LaterInner()


    /******************************************************************************************************/
    /**                             内部实现                                                              **/
    /******************************************************************************************************/
    /**
     * android 6.0设置字体颜色
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setDarkModeForM(window: Window, dark: Boolean) {
        window.decorView.systemUiVisibility = if (dark) {
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }

    /**
     * 设置MIUI6+的状态栏是否为darkMode,darkMode时候字体颜色及icon变黑
     * http://dev.xiaomi.com/doc/p=4769/
     *
     * @return 设置是否成功
     */
    private fun setDarkModeForMIUI6(window: Window, darkMode: Boolean) = try {
        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        val darkModeFlag = field.getInt(layoutParams)
        val extraFlagField = window.javaClass.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
        extraFlagField.invoke(window, if (darkMode) darkModeFlag else 0, darkModeFlag)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    /**
     * 设置Flyme4+的darkMode,darkMode时候字体颜色及icon变黑
     * http://open-wiki.flyme.cn/index.php?title=Flyme%E7%B3%BB%E7%BB%9FAPI
     */
    private fun setDarkModeForFlyme4(window: Window, dark: Boolean) = try {
        val e = window.attributes
        val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
        val flymeFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
        darkFlag.isAccessible = true
        flymeFlags.isAccessible = true
        val bit = darkFlag.getInt(null)
        var value = flymeFlags.getInt(e)
        value = if (dark) value or bit else value and bit.inv()
        flymeFlags.setInt(e, value)
        window.attributes = e
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    /** 判断是否为MIUI6以上  */
    @SuppressLint("PrivateApi")
    private fun isMIUI6LaterInner() = try {
        val clz = Class.forName("android.os.SystemProperties")
        val mtd = clz.getMethod("get", String::class.java)
        var temp = mtd.invoke(null, "ro.miui.ui.version.name") as String
        temp = temp.replace("[vV]".toRegex(), "")
        val version = Integer.parseInt(temp)
        version >= 6
    } catch (e: Exception) {
        false
    }

    /** 判断是否Flyme4以上  */
    private fun isFlyme4LaterInner()
        = (Build.FINGERPRINT.contains("Flyme_OS_4") || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
        || Pattern.compile("Flyme OS [4|5]", Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find())
}
