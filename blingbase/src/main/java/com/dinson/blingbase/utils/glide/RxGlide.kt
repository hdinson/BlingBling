package com.dinson.blingbase.utils.glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dinson.blingbase.R

/**
 * glide工具类
 */
object RxGlide {
    fun setAvatarImageWithBorder(context: Context, url: String, into: ImageView,
                                 defLoadAndError: Int = R.drawable.avatar_default) {
        val transforms = Glide.with(context)
            .load(defLoadAndError)
            .circleCrop()
            .transform(GlideCircleWithBorder())
        Glide.with(context)
            .load(url)
            .thumbnail(transforms)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .transform(GlideCircleWithBorder())
            .into(into)
    }

    fun setAvatarCircle(context: Context, url: String, into: ImageView,
                        defLoadAndError: Int = R.drawable.avatar_default) {
        val transforms = Glide.with(context)
            .load(defLoadAndError)
            .circleCrop()
        Glide.with(context)
            .load(url)
            .thumbnail(transforms)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .into(into)
    }

    /**
     * 加载图片
     */
    fun loadImage(context: Context, url: String, def: Int, into: ImageView) {
        val transforms = Glide.with(context)
            .load(def)
        Glide.with(context)
            .load(url)
            .thumbnail(transforms)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(into)
    }
}