package dinson.customview.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import com.dinson.blingbase.kotlin.click
import com.dinson.blingbase.kotlin.show
import com.dinson.blingbase.kotlin.toasty
import com.dinson.blingbase.utils.DateUtils
import com.dinson.blingbase.utils.SystemBarModeUtils
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.event._025EditScheduleEvent
import dinson.customview.model._025Schedule
import dinson.customview.utils.SPUtils
import dinson.customview.utils.StringUtils
import kotlinx.android.synthetic.main.activity__025_add_schedule.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*


class _025AddScheduleActivity : BaseActivity() {

    companion object {
        private const val EXTRA = "extra"
        fun start(context: Context, extra: _025Schedule? = null) {
            val intent = Intent(context, _025AddScheduleActivity::class.java)
            extra?.apply { intent.putExtra(EXTRA, this) }
            context.startActivity(intent)
        }
    }

    private var mRepeatType = 0
    private lateinit var mYear: String
    private lateinit var mMonth: String
    private lateinit var mDay: String
    private var mScheduleId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__025_add_schedule)

        intUI()
    }

    private fun intUI() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val schedule = intent?.getParcelableExtra<_025Schedule>(EXTRA)
        if (schedule == null) {
            val dateOfDay = DateUtils.getDateOfDay(0, "yyyy-MM-dd EEEE")
            val dateArr = dateOfDay.split(" ")[0].split("-")
            mYear = dateArr[0]
            mMonth = dateArr[1]
            mDay = dateArr[2]
            tvChooseDateTime.text = dateOfDay
        } else {
            val dateArr = schedule.dateTime.split(" ")[0].split("-")
            mYear = dateArr[0]
            mMonth = dateArr[1]
            mDay = dateArr[2]
            tvChooseDateTime.text = schedule.dateTime

            etScheduleName.setText(schedule.name)
            val top = SPUtils.getScheduleTop(this)
            switchTop.isChecked = top == schedule.id
            showRepeatDialog.text = mRepeatTypeArr[schedule.repeatType]
            actionDelete.show()
            mScheduleId = schedule.id
            mRepeatType = schedule.repeatType
            actionDelete.click {
                SPUtils.deleteSchedule(this@_025AddScheduleActivity, mScheduleId)
                EventBus.getDefault().post(_025EditScheduleEvent(mScheduleId))
                onBackPressed()
            }
        }

        actionSave.click {
            val name = etScheduleName.text.toString()
            if (StringUtils.isEmpty(name)) {
                "请输入日程标题".toasty()
                return@click
            }
            val time = tvChooseDateTime.text.split(" ")[0]
            if (StringUtils.isEmpty(mScheduleId)) {
                mScheduleId = DateUtils.currentTimeMillis10().toString()
            }
            val bean = _025Schedule(mScheduleId, name, time, mRepeatType)
            SPUtils.addSchedule(this@_025AddScheduleActivity, bean)
            if (switchTop.isChecked) {
                SPUtils.setScheduleTop(this@_025AddScheduleActivity, mScheduleId)
            }
            EventBus.getDefault().post(_025EditScheduleEvent(bean.id.toString()))
            onBackPressed()
        }
        tvChooseDateTime.click { showStartDateDialog() }
        showRepeatDialog.click { showRepeatDialog() }
    }

    @SuppressLint("SetTextI18n")
    private fun showStartDateDialog() {
        //实例化DatePickerDialog对象
        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                //选择完日期后会调用该回调函数
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
                val time = format.parse("$year-${month + 1}-$day")
                val week = SimpleDateFormat("EEEE", Locale.CHINA).format(time)
                mYear = year.toString()
                mMonth = if (month < 9) "0${month + 1}" else (month + 1).toString()
                mDay = if (day < 10) "0$day" else day.toString()
                tvChooseDateTime.text = "$mYear-$mMonth-$mDay $week"
            }, mYear.toInt(), mMonth.toInt() - 1, mDay.toInt())
        //弹出选择日期对话框
        datePickerDialog.show()
    }

    private val mRepeatTypeArr = arrayOf("不重复", "每周重复", "每月重复", "每年重复")

    private fun showRepeatDialog() {
        val dialog = AlertDialog.Builder(this)
            .setItems(mRepeatTypeArr) { _, which ->
                showRepeatDialog.text = mRepeatTypeArr[which]
                mRepeatType = which
            }.create()
        dialog.show()
    }
}
