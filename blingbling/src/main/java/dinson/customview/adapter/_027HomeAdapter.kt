package dinson.customview.adapter

import dinson.customview.R
import dinson.customview.entity.av.Movie
import dinson.customview.utils.GlideUtils
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_027_home.view.*


/**
 * 联动右边的适配器
 */
class _027HomeAdapter(dataList: List<Movie>)
    : CommonAdapter<Movie>(dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_027_home

    override fun convert(holder: CommonViewHolder, bean: Movie, position: Int) {
        GlideUtils.setImage(holder.itemView.context, bean.coverUrl,
            holder.itemView.ivMoviePic)
        holder.itemView.tvMovieTitle.text = bean.title
        holder.itemView.tvMovieTime.text = bean.date
        holder.itemView.tvMovieNum.text = bean.code
    }
}
