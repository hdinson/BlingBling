package com.dinson.blingbase.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.dinson.blingbase.kotlin.getStatusBarHeight
import java.util.regex.Pattern

/**
 * 状态栏模式工具（目前支持MIUI6以上,Flyme4以上,Android M以上）
 * 黑白模式
 */
@SuppressLint("ObsoleteSdkInt", "PrivateApi", "unUsed")
object SystemBarModeUtils {

    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/

    @Suppress("unused")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun immersiveResource(activity: Activity, @ColorRes color: Int = android.R.color.white,
                          @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1f) {
        immersive(activity, ContextCompat.getColor(activity, color), alpha)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun immersive(activity: Activity, @ColorInt color: Int = 0xffffff,
                  @FloatRange(from = 0.0, to = 1.0) alpha: Float = 0f) {
        immersive(activity.window, color, alpha)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun immersive(window: Window, @ColorInt color: Int = 0xffffff,
                  @FloatRange(from = 0.0, to = 1.0) alpha: Float = 0f) {

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = mixtureColor(color, alpha)

        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = systemUiVisibility

        //适配刘海屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
    }

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
    @Suppress("unused")
    fun isMIUI6Later() = isMIUI6LaterInner()

    /** 判断是否Flyme4以上  */
    @Suppress("unused")
    fun isFlyme4Later() = isFlyme4LaterInner()


    /******************************************************************************************************/
    /**                             内部实现                                                              **/
    /******************************************************************************************************/

    /**
     * 计算颜色
     */
    private fun mixtureColor(color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        val a = if (color and -0x1000000 == 0) 0xff else color.ushr(24)
        return color and 0x00ffffff or ((a * alpha).toInt() shl 24)
    }

    /**
     * 创建假的透明栏
     */
    @Suppress("unused")
    private fun setTranslucentView(container: ViewGroup, color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        if (Build.VERSION.SDK_INT >= 19) {
            val mixtureColor = mixtureColor(color, alpha)
            var translucentView: View? = container.findViewById(android.R.id.custom)
            if (translucentView == null && mixtureColor != 0) {
                translucentView = View(container.context)
                translucentView.id = android.R.id.custom
                val lp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.context))
                container.addView(translucentView, lp)
            }
            translucentView?.setBackgroundColor(mixtureColor)
        }
    }

    /** 获取状态栏高度  */
    private fun getStatusBarHeight(context: Context): Int {
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0)
            context.resources.getDimensionPixelSize(resId)
        else TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24F, Resources.getSystem().displayMetrics).toInt()
    }

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
    private fun isFlyme4LaterInner() = (Build.FINGERPRINT.contains("Flyme_OS_4") || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
        || Pattern.compile("Flyme OS [4|5]", Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find())
}
