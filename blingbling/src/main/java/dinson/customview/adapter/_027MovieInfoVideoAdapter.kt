package dinson.customview.adapter

import dinson.customview.R
import dinson.customview.entity.av.MovieVideo
import dinson.customview.utils.GlideUtils
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_027_movie_info_video.view.*


/**
 * 电影详情中播放的视频item
 */
class _027MovieInfoVideoAdapter(dataList: List<MovieVideo.ResponseBean.VideosBean>)
    : CommonAdapter<MovieVideo.ResponseBean.VideosBean>(dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_027_movie_info_video

    override fun convert(holder: CommonViewHolder, bean: MovieVideo.ResponseBean.VideosBean, position: Int) {
        GlideUtils.setImage(holder.itemView.context, bean.preview_url,
            holder.itemView.ivVideoPic)
    }
}
