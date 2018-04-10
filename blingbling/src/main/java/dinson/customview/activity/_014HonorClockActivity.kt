package dinson.customview.activity

import android.os.Bundle
import dinson.customview.R
import dinson.customview._global.BaseActivity
import kotlinx.android.synthetic.main.activity__014_honor_clock.*
import java.text.SimpleDateFormat
import java.util.*


class _014HonorClockActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__014_honor_clock)

        clockView.performAnimation()
        tvTime.text = getTime()
    }

    /**
     * 获取时间字符串
     */
    private fun getTime(): String {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)
        return "${sdf.format(calendar.time)} ${getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))} " +
            "\n第${calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH)}周 第${calendar.get(Calendar.DAY_OF_YEAR)}天"
    }

    /**
     * 获取中文周几
     */
    private fun getDayOfWeek(week: Int) = when (week) {
        Calendar.SUNDAY -> "周日"
        Calendar.MONDAY -> "周一"
        Calendar.TUESDAY -> "周二"
        Calendar.WEDNESDAY -> "周三"
        Calendar.THURSDAY -> "周四"
        Calendar.FRIDAY -> "周五"
        Calendar.SATURDAY -> "周六"
        else -> ""
    }

    override fun onResume() {
        super.onResume()
        clockView.performAnimation()
    }

    override fun onPause() {
        super.onPause()
        clockView.cancelAnimation()
    }
}
