package dinson.customview.adapter

import android.annotation.SuppressLint
import dinson.customview.R
import dinson.customview.model._025Schedule
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_025_choose_schedule.view.*

class _025ChooseScheduleListAdapter(dataList: MutableList<_025Schedule>)
    : CommonAdapter<_025Schedule>(dataList) {


    override fun getLayoutId(viewType: Int): Int = R.layout.item_025_choose_schedule

    @SuppressLint("SetTextI18n")
    override fun convert(holder: CommonViewHolder, t: _025Schedule, position: Int) {
        holder.itemView.tvScheduleName.text = t.name
    }
}