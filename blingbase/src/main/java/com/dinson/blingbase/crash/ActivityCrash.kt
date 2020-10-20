package com.dinson.blingbase.crash

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import com.dinson.blingbase.R
import com.dinson.blingbase.kotlin.click
import com.dinson.blingbase.kotlin.getAppName
import com.dinson.blingbase.kotlin.hide
import com.dinson.blingbase.kotlin.sp
import com.dinson.blingbase.RxBling
import com.dinson.blingbase.utils.SystemBarModeUtils
import kotlinx.android.synthetic.main.activity_crash.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ActivityCrash : FragmentActivity() {
    @SuppressLint("PrivateResource", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val a = obtainStyledAttributes(R.styleable.AppCompatTheme)
        if (!a.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            setTheme(R.style.Theme_AppCompat_Light_DarkActionBar)
        }
        a.recycle()
        setContentView(R.layout.activity_crash)
        SystemBarModeUtils.immersive(this)
        SystemBarModeUtils.darkMode(this, true)
        SystemBarModeUtils.setPaddingTop(this, crashErrorRoot)

        val config = CrashTool.getConfigFromIntent(intent)
        if (config == null) {
            finish()
            return
        }
        if (config.isShowRestartButton() && config.restartActivityClass != null) {
            btnRestartCrashErrorActivity.click { CrashTool.restartApplication(this@ActivityCrash, config) }
            btnCloseCrashErrorActivity.click { CrashTool.closeApplication(this@ActivityCrash, config) }
        }
        tvCrashErrorBottomBar.text = getAppName()

        val message = CrashTool.getAllErrorDetailsFromIntent(this@ActivityCrash, intent)
        val file = log2File(message)

        tvCrashErrorLocateMoreInfo.text = "${tvCrashErrorLocateMoreInfo.text}\n\n${file.absolutePath}\n"
        if (config.isShowErrorDetails()) {
            btnCrashErrorActivityMoreInfo.click { //We retrieve all the error data and show it
                val dialog = AlertDialog.Builder(this@ActivityCrash)
                    .setTitle("错误细节")
                    .setMessage(message)
                    .setPositiveButton("关闭", null)
                    .setNeutralButton("复制到剪贴板"
                    ) { _, _ -> copyErrorToClipboard() }
                    .show()
                val textView = dialog.findViewById<TextView>(android.R.id.message)
                textView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, sp(12).toFloat())
            }
        } else {
            btnCrashErrorActivityMoreInfo.hide(true)
        }
        val errorDrawableId = config.errorDrawable
        if (errorDrawableId != null) {
            ivCrashErrorActivity.setImageDrawable(ResourcesCompat.getDrawable(resources, errorDrawableId, theme))
        }
    }

    private fun copyErrorToClipboard() {
        val errorInformation = CrashTool.getAllErrorDetailsFromIntent(this@ActivityCrash, intent)
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("错误信息", errorInformation)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this@ActivityCrash, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
    }


    /**
     * 打开日志文件并写入日志
     *
     * @return
     */
    @Synchronized
    private fun log2File(text: String): File {
        val now = Date()
        val date = SimpleDateFormat("HH点mm分ss秒", Locale.getDefault()).format(now)

        val path = RxBling.getApplicationContext().externalCacheDirs[0].parentFile!!.path + File.separator + "log" + File.separator +
            SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(now)
        val destDir = File(path)
        if (!destDir.exists()) {
            destDir.mkdirs()
        }
        val file = File(path, "Log_$date.txt")
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            val filerWriter = FileWriter(file, true)
            val bufWriter = BufferedWriter(filerWriter)
            bufWriter.write(text)
            bufWriter.newLine()
            bufWriter.close()
            filerWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
}