package dinson.customview.adapter

import android.content.Context

import dinson.customview.R
import dinson.customview.entity.MonsterHunter.DataBean
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder

/**
 * @author Dinson - 2017/7/21
 */
class _004LeftAdapter(context: Context, dataList: List<DataBean>)
    : CommonAdapter<DataBean>(context, dataList) {

    override fun getLayoutId(viewType: Int)=  R.layout.item_004_left

    override fun convert(holder: CommonViewHolder, dataBean: DataBean, position: Int) {
        holder.setTvText(R.id.tvName, dataBean.family)
    }
}
