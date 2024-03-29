package dinson.customview.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._025ChooseScheduleListAdapter

import dinson.customview.widget._025provider.WidgetProviderBig
import dinson.customview.widget._025provider.WidgetProviderMiddle
import dinson.customview.widget._025provider.WidgetProviderSmall

import com.dinson.blingbase.widget.recycleview.RvItemClickSupport
import dinson.customview.utils.MMKVUtils
import kotlinx.android.synthetic.main.activity__025_choose_schedule.*

class _025ChooseScheduleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__025_choose_schedule)
        overridePendingTransition(R.anim._025_choose_schedule_bottom_in,
            R.anim._025_choose_schedule_bottom_out)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initUI()
    }

    private fun initUI() {
        val scheduleList = MMKVUtils.getScheduleList()
        rcvScheduleList.adapter = _025ChooseScheduleListAdapter(scheduleList)
        rcvScheduleList.layoutManager = LinearLayoutManager(this)
        RvItemClickSupport.addTo(rcvScheduleList)
            .setOnItemClickListener { _, _, position ->
                val schedule = scheduleList[position]
                val flag = intent.getStringExtra(EXTRA_FLAG)
                val ids = intent.getIntExtra(EXTRA_ID, 0)
                when (flag) {
                    FLAG_BIG -> WidgetProviderBig.sendToSetData(this, ids, schedule.id)
                    FLAG_MIDDLE -> WidgetProviderMiddle.sendToSetData(this, ids, schedule.id)
                    FLAG_SMALL -> WidgetProviderSmall.sendToSetData(this, ids, schedule.id)
                }
                onBackPressed()
            }
    }

    override fun finishWithAnim() = false

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim._025_choose_schedule_top_in,
            R.anim._025_choose_schedule_bottom_out)
    }

    companion object {
        const val EXTRA_FLAG = "extra_flag"
        const val EXTRA_ID = "extra_id"
        const val FLAG_BIG = "flag_big"
        const val FLAG_MIDDLE = "flag_middle"
        const val FLAG_SMALL = "flag_small"
    }
}
