package dinson.customview._global

import dinson.customview.kotlin.loge
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

/**
 * 异常拦截类
 */
object CrashHandler : Thread.UncaughtExceptionHandler {

    private lateinit var mErrorLogDir: String
    private const val mExpirationDate = 3   //文件的存活周期：单位：天
    private const val mPrefix = "error_"

    fun init(errorLogDir: String) {
        mErrorLogDir = errorLogDir
        Thread.setDefaultUncaughtExceptionHandler(this)
        //删除过期文件
        Thread(Runnable { deleteExpirationFile() }).start()
    }

    /**
     * 拦截的线程
     */
    override fun uncaughtException(p0: Thread?, ex: Throwable?) {
        ex?.let { disposeThrowable(ex) }
    }

    /**
     * 写文件
     */
    private fun disposeThrowable(ex: Throwable) {
        try {
            val nowTime = SimpleDateFormat("MMdd_HHmmss", Locale.CHINA).format(System.currentTimeMillis())
            val file = File("$mErrorLogDir$mPrefix$nowTime.txt")
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            val pw = PrintWriter(BufferedWriter(FileWriter(file, true)))
            ex.printStackTrace(pw)
            pw.println()
            pw.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        loge { ex.toString() }
        //退出程序
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(1)
    }


    /**
     * 删除过期文件
     */
    private fun deleteExpirationFile() {
        val expirationTime = (mExpirationDate * 24 * 3600 * 1000).toLong()
        val nowTime = System.currentTimeMillis()
        val dir = File(mErrorLogDir)
        if (dir.isDirectory) {
            dir.listFiles().filter { nowTime - it.lastModified() >= expirationTime }
                .forEach { it.delete() }
        }
    }
}