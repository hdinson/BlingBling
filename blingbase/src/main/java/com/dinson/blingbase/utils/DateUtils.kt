package com.dinson.blingbase.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused")
object DateUtils {

    /**
     * 获取当前13位时间戳
     */
    fun currentTimeMillis13() = System.currentTimeMillis()

    /**
     * 获取当前10位时间戳
     */
    fun currentTimeMillis10() = (System.currentTimeMillis() / 1000).toInt()

    /**
     * 将时间戳转为字符串
     *
     * @param time   要转化的时间
     * @param format 时间格式
     * @return 转化后的字符串
     */
    fun long2Str(time: Long, format: String = "yyyy-MM-dd HH:mm:ss"): String {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.format(Date(time))
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 将时间戳转为字符串
     *
     * @param time   要转化的时间
     * @param format 时间格式
     * @return 转化后的字符串
     */
    fun int2Str(time: Int, format: String = "yyyy-MM-dd HH:mm:ss"): String {
        return long2Str(time * 1000L, format)
    }

    /**
     * 字符串时间转化为时间戳
     *
     * @param time   字符串时间
     * @param format 时间格式
     * @return 时间戳 返回0表示时间格式错误
     */
    fun str2int(time: String, format: String = "yyyy-MM-dd HH:mm:ss"): Int {
        return try {
            val timeData = SimpleDateFormat(format, Locale.getDefault()).parse(time) ?: return 0
            (timeData.time / 1000).toInt()
        } catch (e: ParseException) {
            0
        }
    }

    /**
     * 得到日期
     *
     * @param offset 偏移天数，0表示今天，-1表示昨天，1表示明天
     * @return yyyy-MM-dd
     */
    fun getDateOfDay(offset: Int = 0, format: String = "yyyy-MM-dd"): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, offset)
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(calendar.time)
    }

    /**
     * 得到日期
     *
     * @param offset 偏移天数，0表示今年，-1表示去年，1表示明年
     * @return yyyy
     */
    fun getDateOfYear(offset: Int = 0): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, offset)
        val sdf = SimpleDateFormat("yyyy", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    /**
     * 得到日期时间戳
     *
     * @param offset 偏移天数，0表示今天，-1表示昨天，1表示明天
     * @return 13位时间戳
     */
    fun getDateOfTimestamp(offset: Int = 0): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, offset)
        return calendar.timeInMillis
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，分钟前，小时前，
     *
     * @param timeStamp 毫秒时间戳
     * @return 刚刚，分钟前，小时前 ..
     */
    fun convertTimeToFormat(timeStamp: Long): String {
        val curTime = System.currentTimeMillis() / 1000.toLong()
        return when (val time = curTime - timeStamp) {
            in 0..59 -> "刚刚"
            in 60..3599 -> "${time / 60}分钟前"
            in 3600..3600 * 24 -> "${time / 3600}小时前"
            in 3600 * 24..3600 * 48 -> "昨天"
            in 3600 * 24..600 * 24 * 30 -> "${time / 3600 / 24}天前"
            in 3600 * 24 * 30..3600 * 24 * 30 * 12 -> "${time / 3600 / 24 / 30}个月前"
            else -> if (time >= 3600 * 24 * 30 * 12) "${time / 3600 / 24 / 30 / 12}年前" else "刚刚"
        }
    }


    /**
     * 将一个时间戳转换成提示性时间字符串，如12:14，昨天 12:14，前天 12:14，
     *
     * @param timestamp 13位毫秒时间戳
     * @return 提示性时间字符串
     */
    fun convertTimestamp(timestamp: Long): String {
        val today = getDateOfTimestamp(0)
        val yesT = getDateOfTimestamp(-1)
        val beYesT = getDateOfTimestamp(-2)
        return when {
            timestamp >= today -> long2Str(timestamp, "HH:mm")
            timestamp in yesT until today -> "昨天  " + long2Str(timestamp, "HH:mm")
            timestamp in beYesT until yesT -> "前天  " + long2Str(timestamp, "HH:mm")
            else -> long2Str(timestamp, "MM月dd日  HH:mm")
        }
    }

    /**
     * 比较两个时间大小
     *
     * @param time1
     * @param time2
     * @param format 按格式比较
     * @return 正数：第一个大
     * 负数：第二个大
     * 0：一样大
     * 绝对值：相差秒数
     * @throws Exception 时间转化异常
     */
    @Throws(Exception::class)
    fun compareTime(time1: String, time2: String, format: String): Int {
        val dfs = SimpleDateFormat(format, Locale.getDefault())
        val begin = dfs.parse(time1)
        val end = dfs.parse(time2)
        return (begin!!.time - end!!.time).toInt() / 1000
    }


    /**
     * 获取最大时间
     *
     * @param time1
     * @param time2
     * @param format 按格式比较
     * @return 时间戳
     */
    fun getMax(time1: String, time2: String, format: String, default: String = "时间转换异常"): String {
        return try {
            val compare = compareTime(time1, time2, format)
            if (compare >= 0) time1 else time2
        } catch (e: Exception) {
            e.printStackTrace()
            return default
        }
    }

    /**
     * 获取最大时间
     *
     * @param time1
     * @param time2
     * @param format 按格式比较
     * @return 时间戳
     */
    fun getMin(time1: String, time2: String, format: String, default: String = "时间转换异常"): String {
        return try {
            val compare = compareTime(time1, time2, format)
            if (compare <= 0) time1 else time2
        } catch (e: Exception) {
            e.printStackTrace()
            return default
        }
    }


    /**
     * 将一个时间转化成另一种格式
     *
     * @param time
     * @param oldFormat 转化前的格式
     * @param newFormat 转化后的格式
     * @return 时间格式错误返回格式化前时间
     */
    fun convertStr(time: String, oldFormat: String, newFormat: String): String {
        val oldSdf = SimpleDateFormat(oldFormat, Locale.getDefault())
        val newSdf = SimpleDateFormat(newFormat, Locale.getDefault())
        return try {
            newSdf.format(oldSdf.parse(time)!!)
        } catch (e: Exception) {
            e.printStackTrace()
            return time
        }
    }

    /**
     * 获取当前时间
     *
     * @param format 格式
     * @return
     */
    fun getCurrentDateTime(format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(Date())
    }

    /**
     * 将一个秒数转换为 xx:xx:xx的时间样式。如传入5 则范围 00:00:05
     *
     * @param seconds 秒
     * @return x天 xx:xx:xx的时间样式
     */
    fun calcTimeInFormat(seconds: Int): String {
        val builder = StringBuilder()
        val tHour = 3600 //小时换算基数
        val tMinute = 60 //分钟换算基数
        if (seconds > 0) {
            val hour = seconds / tHour.toLong()
            if (hour > 0) {
                if (hour < 10) builder.append(0)
                builder.append(hour).append(':')
            } else builder.append("00:")
            val minute = seconds % tHour / tMinute.toLong()
            if (minute < 10) builder.append(0)
            builder.append(minute).append(':')
            val second = seconds % tMinute.toLong()
            if (second < 10) builder.append(0)
            builder.append(second).append(':')
        } else return "00:00:00"
        return builder.toString()
    }

    /**
     * 转换服务器时间。默认服务器时间为 "yyyy-MM-dd HH:mm:ss" 样式，失败返回当前时间
     *
     * @param datetimeStr
     * @return
     */
    fun str2Date(datetimeStr: String, format: String): Date? {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return try {
            dateFormat.parse(datetimeStr)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 转换成默认服务器时间样式。默认服务器时间为 "yyyy-MM-dd HH:mm:ss" 样式，失败返回当前时间
     *
     * @param date 时间对象
     * @return
     */
    fun date2Str(date: Date, format: String = "yyyy-MM-dd HH:mm:ss"): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }
}