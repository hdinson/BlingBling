package dinson.customview.weight.banner.holder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dinson.customview.R
import dinson.customview.entity.one.DailyDetail
import dinson.customview.utils.LogUtils


/**
 * 主界面的广告轮播图
 */
class MainBannerHolder : BannerViewHolder<DailyDetail> {

    private lateinit var mImageView: ImageView
    private lateinit var mOptions: RequestOptions
    private lateinit var mTransitionOptions: DrawableTransitionOptions

    override fun createView(context: Context): View {
        mImageView = ImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        mOptions = RequestOptions()
            .error(R.drawable.def_img).diskCacheStrategy(DiskCacheStrategy.DATA)
        mTransitionOptions = DrawableTransitionOptions().crossFade(500)

        return mImageView
    }

    override fun onBind(context: Context, position: Int, data: DailyDetail) {
        Glide.with(context).load(data.data.hp_img_url).transition(mTransitionOptions)
            .apply(mOptions).into(mImageView)
    }

}
