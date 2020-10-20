package dinson.customview.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dinson.customview.R
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import dinson.customview.entity.gank.GankGirlImg
import kotlinx.android.synthetic.main.item_003_gank_lady_pic.view.*


/**
 *
 */
class _003GankGirlsPicAdapter(dataList: MutableList<GankGirlImg>)
    : CommonAdapter<GankGirlImg>(dataList) {

    val options = RequestOptions()
        .placeholder(R.drawable.base_app_default_ic)
        .error(R.drawable.base_app_default_ic)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .optionalCenterInside().dontAnimate()

    override fun getLayoutId(viewType: Int) = R.layout.item_003_gank_lady_pic

    override fun convert(holder: CommonViewHolder, bean: GankGirlImg, position: Int) {
          holder.itemView.ivGankGirlsPic.apply {
              if (bean.height!=0){
                  setOriginalSize(bean.width,bean.height)
              }else {
                  setOriginalSize(1, 1)
              }
            Glide.with(context).load(bean.url).apply(options).into(this)
        }
    }
}
