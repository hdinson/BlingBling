package dinson.customview.adapter

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dinson.customview.R
import dinson.customview.entity.gank.Welfare
import dinson.customview.utils.svg.GlideApp
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_003_gank_lady_pic.view.*

/**
 *玩安卓 "我的喜欢" 列表适配器
 */
class _003GankLadyPicAdapter(dataList: List<Welfare>)
    : CommonAdapter<Welfare>(dataList) {

    val options = RequestOptions()
        .placeholder(R.drawable.base_app_default_ic)
        .error(R.drawable.base_app_default_ic)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .optionalCenterInside().dontAnimate()

    override fun getLayoutId(viewType: Int) = R.layout.item_003_gank_lady_pic

    override fun convert(holder: CommonViewHolder, dataBean: Welfare, position: Int) {
        holder.itemView.ivLadyPic.apply {
            /*val params = layoutParams
            params.height = dataBean.height
            layoutParams = params*/
            GlideApp.with(context).load(dataBean.url).apply(options).into(this)

        }
    }

}
