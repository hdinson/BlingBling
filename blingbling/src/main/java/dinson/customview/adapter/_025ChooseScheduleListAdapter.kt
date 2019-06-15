package dinson.customview.adapter

import android.annotation.SuppressLint
import dinson.customview.R
import dinson.customview.model._025Schedule
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_025_choose_schedule.view.*

class _025ChooseScheduleListAdapter(dataList: List<_025Schedule>)
    : CommonAdapter<_025Schedule>(dataList) {


    override fun getLayoutId(viewType: Int): Int = R.layout.item_025_choose_schedule

    @SuppressLint("SetTextI18n")
    override fun convert(holder: CommonViewHolder, t: _025Schedule, position: Int) {
        holder.itemView.tvScheduleName.text = t.name
    }
}