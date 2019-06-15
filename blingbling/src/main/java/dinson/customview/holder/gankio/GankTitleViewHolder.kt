package dinson.customview.holder.gankio

import android.support.v7.widget.RecyclerView
import android.view.View
import dinson.customview.entity.gank.ProjectsTitle
import dinson.customview.weight.recycleview.multitype.MultiTypeViewHolder
import kotlinx.android.synthetic.main.item_003_gank_title.view.*


class GankTitleViewHolder(itemView: View) : MultiTypeViewHolder<ProjectsTitle>(itemView) {

    override fun convert(holder: RecyclerView.ViewHolder, t: ProjectsTitle, position: Int) {
        holder.itemView.tvTitle.text = t.title
    }
}
