package com.dinson.blingbase.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.math.BigDecimal

@Suppress("unused")
object CacheUtils {

    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     *
     */
    private fun cleanInternalCache(context: Context) {
        deleteFilesByDirectory(context.cacheDir)
    }

    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     *
     * @param context
     */
    fun cleanDatabases(context: Context) {
        deleteFilesByDirectory(File("/data/data/"
            + context.packageName + "/databases"))
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     *
     * @param context
     */
    fun cleanSharedPreference(context: Context) {
        deleteFilesByDirectory(File("/data/data/"
            + context.packageName + "/shared_prefs"))
    }

    /**
     * * 按名字清除本应用数据库 * *
     *
     * @param context
     * @param dbName
     */
    fun cleanDatabaseByName(context: Context, dbName: String) {
        context.deleteDatabase(dbName)
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     *
     * @param context
     */
    fun cleanFiles(context: Context) {
        deleteFilesByDirectory(context.filesDir)
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    fun cleanExternalCache(context: Context) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteFilesByDirectory(context.externalCacheDir!!)
        }
    }

    /**
     * * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * *
     *
     * @param filePath
     */
    fun cleanCustomCache(filePath: String) {
        deleteFilesByDirectory(File(filePath))
    }

    /**
     * * 清除本应用所有的数据 * *
     *
     * @param context
     * @param filepath
     */
    fun cleanApplicationData(context: Context, vararg filepath: String?) {
        cleanInternalCache(context)
        cleanExternalCache(context)
        cleanDatabases(context)
        cleanSharedPreference(context)
        cleanFiles(context)
        for (filePath in filepath) {
            filePath?.apply { cleanCustomCache(this) }
        }
    }

    /**
     * * 清除本应用缓存的数据 * *
     *
     * @param context
     */
    fun cleanApplicationCacheData(context: Context) {
        cleanInternalCache(context)
        cleanExternalCache(context)
    }


    fun getCacheSize(context: Context): String {
        val cacheSize = getFolderSize(context.cacheDir)
        val externalSize = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            getFolderSize(context.externalCacheDir!!).toDouble()
        } else 0.00
        return getFormatSize(cacheSize + externalSize)
    }


    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (i in fileList.indices) {
                // 如果下面还有文件
                size += if (fileList[i].isDirectory) {
                    getFolderSize(fileList[i])
                } else {
                    fileList[i].length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 删除指定目录下文件及目录
     */
    private fun deleteFilesByDirectory(file: File, deleteThisPath: Boolean = false) {
        try {
            if (file.isDirectory) {// 如果下面还有文件
                val files = file.listFiles()
                for (i in files.indices) {
                    deleteFilesByDirectory(files[i], true)
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory) {// 如果是文件，删除
                    file.delete()
                } else {// 目录
                    if (file.listFiles().isEmpty()) {// 目录下没有文件或者目录，删除
                        file.delete()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 格式化单位
     */
    private fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return size.toString() + "B"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }


    fun setCache(context: Context, url: String, json: String, deathLine: Long = 0) {
        val death = if (deathLine != 0L) System.currentTimeMillis() + deathLine else 0L
        val cacheFile = File(context.externalCacheDirs[0].path, MD5.encode(url))
        cacheFile.bufferedWriter().use {
            it.write("$death\n")
            it.write(json)
        }
    }

    fun getCache(context: Context, url: String): String? {
        val cacheFile = File(context.externalCacheDirs[0].path, MD5.encode(url))
        if (cacheFile.exists()) {
            cacheFile.bufferedReader().apply {
                val deathLine = this.readLine().toLong()
                return if (deathLine != 0L && System.currentTimeMillis() > deathLine) {
                    null
                } else this.readLines().joinToString()
            }
        }
        return null
    }

}
