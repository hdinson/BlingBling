package dinson.customview.entity._025

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
@Suppress("unused")
data class _025Schedule(
    val id: String, val name: String,
    var dateTime: String, var repeatType: Int = 0
) : Parcelable {

    /**
     * 计算时间间隔，带正负数
     */
    fun getDisplayDay(): Int {
        return when (repeatType) {
            0 ->                     //不重复
                compareFuture(dateTime).toInt()
            1 ->                     //每周重复
                getRepeatDay(Calendar.WEEK_OF_MONTH)
            2 ->                     //每月重复
                getRepeatDay(Calendar.MONTH)
            3 ->                     //每年重复
                getRepeatDay(Calendar.YEAR)
            else -> 0
        }
    }

    fun getWeek(): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val temp = "异常"
        try {
            val parse = format.parse(dateTime)
            val formatWeek =
                SimpleDateFormat("EEEE", Locale.CHINA)
            formatWeek.format(parse)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return temp
    }

    private fun compareFuture(future: String): Long {
        val dfs =
            SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val end = dfs.parse(future)
        val now = dfs.format(Date())
        val start = dfs.parse(now)
        val time = (end.time - start.time) / 100000 / 36 / 24
        return if (time < 0) time - 1 else time
    }

    private fun getRepeatDay(field: Int): Int {
        val dfs =
            SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val target = dfs.parse(dateTime)
        val c2 = Calendar.getInstance()
        c2.time = target
        var i = 1
        val nowStr = dfs.format(Date())
        val now = dfs.parse(nowStr).time
        var c3 = c2.clone() as Calendar
        while (c3.time.time < now) {
            c3 = c2.clone() as Calendar
            c3.add(field, i)
            i++
        }
        return ((c3.time.time - now) / 100000 / 36 / 24).toInt()
    }
}