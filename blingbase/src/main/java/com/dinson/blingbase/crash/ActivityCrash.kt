package com.dinson.blingbase.crash

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import com.dinson.blingbase.R
import com.dinson.blingbase.kotlin.getAppName
import com.dinson.blingbase.kotlin.loge
import com.dinson.blingbase.kotlin.sdCardIsAvailable
import com.dinson.blingbase.kotlin.sp
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

        "ActivityCrash onCreate.. ".loge()

        //This is needed to avoid a crash if the developer has not specified
        //an app-level theme that extends Theme.AppCompat
        val a = obtainStyledAttributes(R.styleable.AppCompatTheme)
        if (!a.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            setTheme(R.style.Theme_AppCompat_Light_DarkActionBar)
        }
        a.recycle()
        setContentView(R.layout.activity_crash)

        //Close/restart button logic:
        //If a class if set, use restart.
        //Else, use close and just finish the app.
        //It is recommended that you follow this logic if implementing a custom error activity.
        val restartButton = findViewById<Button>(R.id.crash_error_activity_restart_button)
        val closeButton = findViewById<Button>(R.id.crash_error_activity_close_button)
        val tvCrashTool = findViewById<TextView>(R.id.rx_crash_tool)
        val config = CrashTool.getConfigFromIntent(intent)
        if (config == null) {
            //This should never happen - Just finish the activity to avoid a recursive crash.
            finish()
            return
        }
        if (config.isShowRestartButton() && config.restartActivityClass != null) {
            restartButton.setText(R.string.crash_error_restart_app)
            restartButton.setOnClickListener { CrashTool.restartApplication(this@ActivityCrash, config) }
            closeButton.setOnClickListener { CrashTool.closeApplication(this@ActivityCrash, config) }
        } else {
            closeButton.visibility = View.GONE
        }
        val message = CrashTool.getAllErrorDetailsFromIntent(this@ActivityCrash, intent)
        val file = log2File(message)
        val appName = getAppName()
        tvCrashTool.text = appName
        val locateButton = findViewById<TextView>(R.id.crash_error_locate_more_info_button)
        locateButton.text = "${locateButton.text}\n\n${file.absolutePath}\n"
        val moreInfoButton = findViewById<Button>(R.id.crash_error_activity_more_info_button)
        if (config.isShowErrorDetails()) {
            moreInfoButton.setOnClickListener { //We retrieve all the error data and show it
                val dialog = AlertDialog.Builder(this@ActivityCrash)
                    .setTitle(R.string.crash_error_details_title)
                    .setMessage(message)
                    .setPositiveButton(R.string.crash_error_details_close, null)
                    .setNeutralButton(R.string.crash_error_details_copy
                    ) { _, _ -> copyErrorToClipboard() }
                    .show()
                val textView = dialog.findViewById<TextView>(android.R.id.message)
                textView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, sp(12).toFloat())
            }
        } else {
            moreInfoButton.visibility = View.GONE
        }
        val defaultErrorActivityDrawableId = config.errorDrawable
        val errorImageView = findViewById<ImageView>(R.id.crash_error_activity_image)
        if (defaultErrorActivityDrawableId != null) {
            errorImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, defaultErrorActivityDrawableId, theme))
        }
    }

    private fun copyErrorToClipboard() {
        val errorInformation = CrashTool.getAllErrorDetailsFromIntent(this@ActivityCrash, intent)
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        //Are there any devices without clipboard...?
        val clip = ClipData.newPlainText(getString(R.string.crash_error_details_clipboard_label), errorInformation)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this@ActivityCrash, R.string.crash_error_details_copied, Toast.LENGTH_SHORT).show()
    }


    /**
     * 打开日志文件并写入日志
     *
     * @return
     */
    @Synchronized
    private fun log2File(text: String): File {

        val sdCardIsAvailable = sdCardIsAvailable()
        val LOG_FILE_PATH = rootPath.path + File.separator + packageName + File.separator + "Log"
        val LOG_FILE_NAME = "Log_"

        val now = Date()
        val date = SimpleDateFormat("HH点mm分ss秒", Locale.getDefault()).format(now)
        val dateLogContent = """
            Date:${SimpleDateFormat("yyyy年MM月dd日_HH点mm分ss秒", Locale.getDefault()).format(now)}
            LogType:e 
            Content:
            $text
            """.trimIndent() // 日志输出格式
        val path = LOG_FILE_PATH + File.separator + SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(now)
        val destDir = File(path)
        if (!destDir.exists()) {
            destDir.mkdirs()
        }
        val file = File(path, "${LOG_FILE_NAME}$date.txt")
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            val filerWriter = FileWriter(file, true)
            val bufWriter = BufferedWriter(filerWriter)
            bufWriter.write(dateLogContent)
            bufWriter.newLine()
            bufWriter.close()
            filerWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }


    /**
     * 得到SD卡根目录.
     */

    val rootPath: File
        get() {
            var path: File? = null
            path = if (sdCardIsAvailable()) {
                Environment.getExternalStorageDirectory() // 取得sdcard文件路径
            } else {
                Environment.getDataDirectory()
            }
            return path
        }

}