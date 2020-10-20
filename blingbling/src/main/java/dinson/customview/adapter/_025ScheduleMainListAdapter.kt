package dinson.customview.adapter

import android.annotation.SuppressLint
import dinson.customview.R
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import dinson.customview.entity._025._025Schedule
import kotlinx.android.synthetic.main.item_025_schedule_main.view.*
import kotlin.math.abs

class _025ScheduleMainListAdapter(dataList: MutableList<_025Schedule>)
    : CommonAdapter<_025Schedule>(dataList) {


    override fun getLayoutId(viewType: Int): Int = R.layout.item_025_schedule_main

    @SuppressLint("SetTextI18n")
    override fun convert(holder: CommonViewHolder, t: _025Schedule, position: Int) {
        holder.itemView.tvScheduleName.text = t.name
        holder.itemView.tvTimeCount.text = abs(t.getDisplayDay()).toString()
    }


}