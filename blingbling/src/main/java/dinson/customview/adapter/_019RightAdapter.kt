package dinson.customview.adapter

import android.content.Context
import android.widget.ImageView

import dinson.customview.R
import dinson.customview.entity.MonsterHunter
import dinson.customview.entity.MonsterHunter.DataBean.MonsterBean
import dinson.customview.utils.GlideUtils
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder

/**
 * @author Dinson - 2017/7/21
 */
class _019RightAdapter(context: Context, dataList: List<MonsterHunter.DataBean.MonsterBean>)
    : CommonAdapter<MonsterBean>(context, dataList) {

    override fun getLayoutId(viewType: Int)
        = if (viewType == 0) R.layout.item_019_right_title else R.layout.item_019_right_normal

    override fun getItemViewType(position: Int)
        = if (mDataList[position].isTitle) 0 else 1

    override fun convert(holder: CommonViewHolder, monsterBean: MonsterBean, position: Int) {
        when (getItemViewType(position)) {
            0 -> holder.setTvText(R.id.tvTitle, monsterBean.family)
            1 -> {
                GlideUtils.setImage(mContext, monsterBean.icon, holder.getView<ImageView>(R.id.ivImg))
                holder.setTvText(R.id.tvName, monsterBean.name)
            }
        }
    }
}
