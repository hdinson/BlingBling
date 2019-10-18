package dinson.customview.adapter

import android.annotation.SuppressLint
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dinson.customview.R
import dinson.customview.entity.gank.MeiZiTuPicSet
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_003_meizitu_pic_set.view.*


/**
 * 妹子图精选，最新，最热，图集样式的适配器
 */
class _003MeiZiTuPicSetAdapter(dataList: List<MeiZiTuPicSet>)
    : CommonAdapter<MeiZiTuPicSet>(dataList) {

    val options = RequestOptions()
        .placeholder(R.drawable.base_app_default_ic)
        .error(R.drawable.base_app_default_ic)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .optionalCenterInside().dontAnimate()

    override fun getLayoutId(viewType: Int) = R.layout.item_003_meizitu_pic_set

    @SuppressLint("SetTextI18n")
    override fun convert(holder: CommonViewHolder, dataBean: MeiZiTuPicSet, position: Int) {
        Glide.with(holder.itemView.context).load(dataBean.thumb_src_min).apply(options).into(holder.itemView.ivMeiZiTuPic)
        holder.itemView.tvDesc.text = dataBean.title
        holder.itemView.tvPicNum.text = "${dataBean.img_num}P"
    }
}
