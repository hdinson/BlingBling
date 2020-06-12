package com.dinson.blingbase.widget.banner.holder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


/**
 * 案例
 */
class MainBannerHolder : BannerViewHolder<String> {

    private lateinit var mImageView: ImageView
//    private lateinit var mOptions: RequestOptions
//    private lateinit var mTransitionOptions: DrawableTransitionOptions

    override fun createView(context: Context): View {
        mImageView = ImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
//        mOptions = RequestOptions()
//            .error(R.drawable.picture_icon_def).diskCacheStrategy(DiskCacheStrategy.DATA)
//        mTransitionOptions = DrawableTransitionOptions().crossFade(500)

        return mImageView
    }

    override fun onBind(context: Context, position: Int, data: String) {
//        Glide.with(context).load(data.imgUrl).transition(mTransitionOptions)
//            .apply(mOptions).into(mImageView)
    }

}
