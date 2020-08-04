package com.dinson.blingbase.widget.banner.transformer

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs


class CoverModeTransformer(viewPager: ViewPager) : ViewPager.PageTransformer {

    init {
        viewPager.offscreenPageLimit = 2
    }

    companion object {
        private val mScaleMin = 0.9f    //Y周收缩比例
    }

    private var reduceX = -1f
    override fun transformPage(view: View, pos: Float) {
        if (reduceX == -1f) {
            //由于左右边的缩小而减小的x的大小的一半
            reduceX = (1.0f - mScaleMin) * view.width / 2.0f
        }
        when {
            pos < -1f -> {     //(0,-1)
                view.translationX = reduceX * (-1 - 2 * pos)
                view.scaleX = mScaleMin
                view.scaleY = mScaleMin
            }
            pos <= 1f -> {     //[-1,1]
                val scale = (1 - abs(pos)) * (1 - mScaleMin) + mScaleMin
                val translationX = reduceX * -pos
                view.translationX = translationX
                view.scaleX = scale
                view.scaleY = scale
            }
            else -> {
                view.translationX = reduceX * (1 - 2 * pos)
                view.scaleX = mScaleMin
                view.scaleY = mScaleMin
            }
        }
    }
}

