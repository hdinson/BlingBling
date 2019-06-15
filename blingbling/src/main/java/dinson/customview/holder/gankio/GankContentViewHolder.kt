package dinson.customview.holder.gankio

import android.support.v7.widget.RecyclerView
import android.view.View
import dinson.customview.entity.gank.ProjectsInfo
import dinson.customview.weight.recycleview.multitype.MultiTypeViewHolder
import kotlinx.android.synthetic.main.item_003_gank_content.view.*


class GankContentViewHolder(itemView: View) : MultiTypeViewHolder<ProjectsInfo>(itemView) {

    override fun convert(holder: RecyclerView.ViewHolder, t: ProjectsInfo, position: Int) {
        holder.itemView.tvTitle.text = t.desc
        holder.itemView.tvSubTitle.text = t.url
    }
}
