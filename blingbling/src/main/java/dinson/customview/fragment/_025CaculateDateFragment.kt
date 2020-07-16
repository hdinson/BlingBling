package dinson.customview.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.dinson.blingbase.kotlin.click
import dinson.customview.kotlin.loge
import com.dinson.blingbase.utils.DateUtils
import com.jakewharton.rxbinding2.widget.RxTextView
import dinson.customview.R
import kotlinx.android.synthetic.main.fragment_025_caculate_date.*
import java.text.SimpleDateFormat
import java.util.*


class _025CaculateDateFragment : ViewPagerLazyFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_025_caculate_date, container, false)
    }

    private lateinit var mStartYear: String
    private lateinit var mStartMonth: String
    private lateinit var mStartDay: String
    private lateinit var mNowYear: String
    private lateinit var mNowMonth: String
    private lateinit var mNowDay: String
    private lateinit var mEndYear: String
    private lateinit var mEndMonth: String
    private lateinit var mEndDay: String
    private lateinit var mDefaultHint: String

    override fun lazyInit() {
        selectStartDate.click { showStartDateDialog() }
        selectEndDate.click { showEndDateDialog() }
        mDefaultHint = resources.getString(R.string._025_input_num_to_calculate)

        RxTextView.textChanges(etBefore).subscribe {
            if (it.isEmpty()) {
                tvBefore.text = mDefaultHint
                return@subscribe
            }
            val num = it.toString().toInt()
            val dateOfDay = DateUtils.getDateOfDay(-num, "yyyy-MM-dd EEEE")
            tvBefore.text = dateOfDay
        }.addToManaged()
        RxTextView.textChanges(etAfter).subscribe {
            if (it.isEmpty()) {
                tvAfter.text = mDefaultHint
                return@subscribe
            }
            val num = it.toString().toInt()
            val dateOfDay = DateUtils.getDateOfDay(num, "yyyy-MM-dd EEEE")
            tvAfter.text = dateOfDay
        }.addToManaged()

        val now = Date(System.currentTimeMillis())
        val nowStr = SimpleDateFormat("yyyy-MM-dd-EEEE", Locale.CHINA).format(now)
        val dateArr = nowStr.split("-")
        mNowYear = dateArr[0]
        mNowMonth = dateArr[1]
        mNowDay = dateArr[2]
        setStartDate(mNowYear, mNowMonth, mNowDay, dateArr[3])
        setEndDate(mNowYear, mNowMonth, mNowDay, dateArr[3])
    }

    private fun showStartDateDialog() {
        //实例化DatePickerDialog对象
        val datePickerDialog = DatePickerDialog(context!!,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                //选择完日期后会调用该回调函数
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
                val time = format.parse("$year-${month + 1}-$day")
                val week = SimpleDateFormat("EEEE", Locale.CHINA).format(time)
                setStartDate(year.toString(), if (month < 9) "0${month + 1}" else (month + 1).toString(),
                    if (day < 10) "0$day" else day.toString(), week)
            }, mStartYear.toInt(), mStartMonth.toInt() - 1, mStartDay.toInt())
        //弹出选择日期对话框
        datePickerDialog.show()
    }

    private fun showEndDateDialog() {
        //实例化DatePickerDialog对象
        val datePickerDialog = DatePickerDialog(context!!,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                //选择完日期后会调用该回调函数
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
                val time = format.parse("$year-${month + 1}-$day")
                val week = SimpleDateFormat("EEEE", Locale.CHINA).format(time)
                setEndDate(year.toString(), if (month < 9) "0${month + 1}" else (month + 1).toString(),
                    if (day < 10) "0$day" else day.toString(), week)

                //计算还有几天
                val dr = DateUtils.compareTime("$mEndYear-$mEndMonth-$mEndDay",
                    "$mNowYear-$mNowMonth-$mNowDay", "yyyy-MM-dd")
                loge { "$dr : ${dr / 3600 / 24}" }
                tvTimeDr.text = (dr / 3600 / 24).toString()
            }, mEndYear.toInt(), mEndMonth.toInt() - 1, mEndDay.toInt())
        //弹出选择日期对话框
        datePickerDialog.show()
    }

    /**
     * 设置开始日期
     */
    @SuppressLint("SetTextI18n")
    private fun setStartDate(year: String, month: String, day: String, week: String) {
        mStartYear = year
        mStartMonth = month
        mStartDay = day
        tvNow.text = "$mStartYear-$mStartMonth-$mStartDay $week"
    }

    /**
     * 设置开始日期
     */
    @SuppressLint("SetTextI18n")
    private fun setEndDate(year: String, month: String, day: String, week: String) {
        mEndYear = year
        mEndMonth = month
        mEndDay = day
        tvEnd.text = "$mEndYear-$mEndMonth-$mEndDay $week"
    }
}
