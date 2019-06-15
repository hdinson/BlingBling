package dinson.customview.activity

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dinson.customview.R


class SplashActivity : AppCompatActivity() {

    companion object {
        private const val defaultIconPath = "dinson.customview.activity.SplashActivity"
        private const val newIconPath = "dinson.customview.activity.SplashAliasActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onRestart() {
        super.onRestart()
        //changeIcon(if (SPUtils.isDefaultAppIcon(this)) defaultIconPath else newIconPath)
        onBackPressed()
    }

    /**
     * 更换应用图标
     */
    private fun changeIcon(activityPath: String) {
        val pm = packageManager
        pm.setComponentEnabledSetting(componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
        pm.setComponentEnabledSetting(ComponentName(this, activityPath),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        //重启桌面 加速显示
        restartSystemLauncher(pm)
    }

    /**
     * 重启桌面，加速显示
     * 需要加<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />
     */
    private fun restartSystemLauncher(pm: PackageManager) {
        val am = getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        val i = Intent(Intent.ACTION_MAIN)
        i.addCategory(Intent.CATEGORY_HOME)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        val resolves = pm.queryIntentActivities(i, 0)
        resolves
            .filter { it.activityInfo != null }
            .forEach { am.killBackgroundProcesses(it.activityInfo.packageName) }
    }
}
