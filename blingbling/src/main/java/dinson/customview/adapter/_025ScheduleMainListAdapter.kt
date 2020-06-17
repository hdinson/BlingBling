package dinson.customview.adapter

import android.annotation.SuppressLint
import dinson.customview.R
import dinson.customview.model._025Schedule
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_025_schedule_main.view.*
import kotlin.math.abs

class _025ScheduleMainListAdapter(dataList: List<_025Schedule>)
    : CommonAdapter<_025Schedule>(dataList) {


    override fun getLayoutId(viewType: Int): Int = R.layout.item_025_schedule_main

    @SuppressLint("SetTextI18n")
    override fun convert(holder: CommonViewHolder, t: _025Schedule, position: Int) {
        holder.itemView.tvScheduleName.text = t.name
        holder.itemView.tvTimeCount.text = abs(t.displayDay).toString()
    }


}