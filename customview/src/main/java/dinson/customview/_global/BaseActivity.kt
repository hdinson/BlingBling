package dinson.customview._global

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import dinson.customview.R

/**
 * 所有activity的基类
 */
open class BaseActivity : AppCompatActivity() {
    private var mStartTime: Long = 0
    //private val mEndTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        mStartTime = System.currentTimeMillis()
        super.onCreate(savedInstanceState)

        /*共享元素*/
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        /*透明状态栏*/
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //getSupportActionBar().hide();
        /*activity的出现动画*/
        overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left)
        /*logcat点击跳转对用activity*/
        logShowActivity()
    }

    override fun onStart() {
        super.onStart()
        logShowActivity()
        setWindowBackgroundColor().apply {
            window.setBackgroundDrawable(this?.let { getDrawable(this) })
        }
    }

    /**
     * 设置窗口背景颜色
     */
    open fun setWindowBackgroundColor(): Int? {
        return R.color.window_bg
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (finishWithAnim())
            overridePendingTransition(R.anim.activity_in_from_left, R.anim.activity_out_to_right)
    }

    open fun finishWithAnim(): Boolean {
        return true
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        logShowActivity()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        logShowActivity()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        logShowActivity()
    }

    override fun onActivityReenter(resultCode: Int, data: Intent) {
        super.onActivityReenter(resultCode, data)
        logShowActivity()
    }

    override fun onLocalVoiceInteractionStarted() {
        super.onLocalVoiceInteractionStarted()
        logShowActivity()
    }

    override fun onLocalVoiceInteractionStopped() {
        super.onLocalVoiceInteractionStopped()
        logShowActivity()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        logShowActivity()
    }

    override fun onPause() {
        super.onPause()
        logShowActivity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        logShowActivity()
    }

    override fun onStop() {
        super.onStop()
        logShowActivity()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logShowActivity()
    }


    override fun onRestart() {
        super.onRestart()
        logShowActivity()
    }

    override fun onResume() {
        super.onResume()
        logShowActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        logShowActivity()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        logShowActivity()
    }

    private fun logShowActivity() {
        val stackTraceElement = Thread.currentThread().stackTrace

        var currentIndex = stackTraceElement.indices
            .firstOrNull { stackTraceElement[it].methodName.compareTo("logShowActivity") == 0 }
            ?.let { it + 1 }
            ?: -1

        currentIndex += 1
        val fullClassName = stackTraceElement[currentIndex].className
        if (!fullClassName.startsWith(ConstantsUtils.PACKAGE_NAME)) return
        val className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
        val methodName = stackTraceElement[currentIndex].methodName
        val lineNumber = stackTraceElement[currentIndex].lineNumber.toString()
        Log.v("dd", ConstantsUtils.LOGCAT_TAG + "at " + fullClassName + "." + methodName + "("
            + className + ".java:" + lineNumber + ")")
    }
}
