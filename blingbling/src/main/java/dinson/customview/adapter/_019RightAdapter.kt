package dinson.customview.adapter

import android.widget.ImageView
import android.widget.TextView
import dinson.customview.BuildConfig
import dinson.customview.R
import dinson.customview.entity.MonsterHunter
import dinson.customview.entity.MonsterHunter.DataBean.MonsterBean
import dinson.customview.utils.GlideUtils
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder

/**
 * 联动右边的适配器
 */
class _019RightAdapter(dataList: List<MonsterHunter.DataBean.MonsterBean>)
    : CommonAdapter<MonsterBean>(dataList) {

    override fun getLayoutId(viewType: Int) = if (viewType == 0) R.layout.item_019_right_title else R.layout.item_019_right_normal

    override fun getItemViewType(position: Int) = if (mDataList[position].isTitle) 0 else 1

    override fun convert(holder: CommonViewHolder, monsterBean: MonsterBean, position: Int) {

        when (getItemViewType(position)) {
            0 -> holder.itemView.findViewById<TextView>(R.id.tvTitle).text = monsterBean.family
            1 -> {
                val iv = holder.itemView.findViewById<ImageView>(R.id.ivImg)
                GlideUtils.setImage(holder.itemView.context, "${BuildConfig.QINIU_URL}${monsterBean.icon}", iv)
                holder.itemView.findViewById<TextView>(R.id.tvName).text = monsterBean.name
            }
        }
    }
}
