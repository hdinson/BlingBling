package dinson.customview.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dinson.customview.R
import dinson.customview.activity._025AddScheduleActivity
import dinson.customview.adapter._025ScheduleMainListAdapter
import dinson.customview.event._025EditScheduleEvent
import com.dinson.blingbase.kotlin.hide
import com.dinson.blingbase.kotlin.show
import dinson.customview.model._025Schedule
import dinson.customview.utils.SPUtils
import dinson.customview.weight._025provider.WidgetProviderBig
import dinson.customview.weight._025provider.WidgetProviderMiddle
import dinson.customview.weight._025provider.WidgetProviderSmall

import com.dinson.blingbase.widget.recycleview.RvItemClickSupport
import kotlinx.android.synthetic.main.fragment_025_days_matter.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class _025DaysMatterFragment : ViewPagerLazyFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_025_days_matter, container, false)
    }

    private val mData = ArrayList<_025Schedule>()
    private lateinit var mAdapter: _025ScheduleMainListAdapter

    override fun lazyInit() {
        //TextViewCompat.setAutoSizeTextTypeWithDefaults(tv_scale, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        crvScheduleList.layoutManager = LinearLayoutManager(context)
        mAdapter = _025ScheduleMainListAdapter(mData)
        crvScheduleList.adapter = mAdapter
        RvItemClickSupport.addTo(crvScheduleList)
            .setOnItemClickListener { _, _, position ->
                _025AddScheduleActivity.start(context!!, mData[position])
            }
        initData()
    }

    private fun initData() {
        val spData = SPUtils.getScheduleList(context!!)
        mData.clear()
        mData.addAll(spData)
        if (mData.isNotEmpty()) {
            emptyView.hide()
            val top = SPUtils.getScheduleTop(emptyView.context)
            val find = mData.find { it.id == top }
            initTop(find ?: mData[0])
        } else {
            topCard.hide(true)
            emptyView.show()
        }
        mAdapter.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun initTop(schedule: _025Schedule) {
        topCard.show()
        tvTopTitle.text = schedule.name
        tvTopDate.text = "目标日: ${schedule.dateTime}"
        val displayDay = schedule.displayDay
        tvDayCount.text = if (displayDay < 0) "已过 ${-displayDay}" else displayDay.toString()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: _025EditScheduleEvent) {
        initData()
        WidgetProviderBig.sendToRefresh(context!!, scheduleId = event.id)
        WidgetProviderMiddle.sendToRefresh(context!!, scheduleId = event.id)
        WidgetProviderSmall.sendToRefresh(context!!, scheduleId = event.id)
    }
}