package dinson.customview.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dinson.customview.R
import dinson.customview.entity.gank.Welfare
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_003_gank_lady_pic.view.*


/**
 *
 */
class _003GankGirlsPicAdapter(dataList: List<Welfare>)
    : CommonAdapter<Welfare>(dataList) {

    val options = RequestOptions()
        .placeholder(R.drawable.base_app_default_ic)
        .error(R.drawable.base_app_default_ic)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .optionalCenterInside().dontAnimate()

    override fun getLayoutId(viewType: Int) = R.layout.item_003_gank_lady_pic

    override fun convert(holder: CommonViewHolder, dataBean: Welfare, position: Int) {
          holder.itemView.ivGankGirlsPic.apply {
              if (dataBean.height!=0){
                  setOriginalSize(dataBean.width,dataBean.height)
              }else {
                  setOriginalSize(1, 1)
              }
            Glide.with(context).load(dataBean.url).apply(options).into(this)
        }
    }
}
