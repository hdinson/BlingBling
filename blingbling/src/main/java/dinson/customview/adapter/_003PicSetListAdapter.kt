package dinson.customview.adapter

import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dinson.customview.R
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_003_pic_set_list.view.*

/**
 * 妹子图 图集
 */
class _003PicSetListAdapter(dataList: List<Uri>)
    : CommonAdapter<Uri>(dataList) {

    private val options = RequestOptions()
        .placeholder(R.drawable.base_app_default_ic)
        .error(R.drawable.base_app_default_ic)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .dontAnimate()

    override fun getLayoutId(viewType: Int) = R.layout.item_003_pic_set_list

    override fun convert(holder: CommonViewHolder, dataBean: Uri, position: Int) {
        Glide.with(holder.itemView.ivPicture.context)
            .load(dataBean).apply(options).into(holder.itemView.ivPicture)
    }
}
