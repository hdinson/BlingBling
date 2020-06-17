package dinson.customview.adapter

import dinson.customview.R
import dinson.customview.entity.av.MovieInfo
import dinson.customview.utils.GlideUtils
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_027_actresses.view.*


/**
 * 女演员（名字和图片）
 */
class _027ActressesAdapter(dataList: List<MovieInfo.Actress>)
    : CommonAdapter<MovieInfo.Actress>(dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_027_actresses

    override fun convert(holder: CommonViewHolder, bean: MovieInfo.Actress, position: Int) {
        GlideUtils.setImage(holder.itemView.context, bean.imageUrl, holder.itemView.ivActressPic)
        holder.itemView.tvActressName.text = bean.name
    }
}
