package dinson.customview.adapter

import dinson.customview.R
import dinson.customview.entity.MonsterHunter.DataBean
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_019_left.view.*

/**
 * 联动左侧列表适配器
 */
class _019LeftAdapter(dataList: List<DataBean>)
    : CommonAdapter<DataBean>(dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_019_left

    override fun convert(holder: CommonViewHolder, dataBean: DataBean, position: Int) {
        holder.itemView.tvName.text = dataBean.family
    }
}
