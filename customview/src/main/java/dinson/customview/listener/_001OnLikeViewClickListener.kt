package dinson.customview.listener

import android.widget.CheckBox
import dinson.customview.entity.wanandroid.WanAndArticle

/**
 *  玩安卓收藏点击监听
 */
interface _001OnLikeViewClickListener {
    fun onClick(likeView: CheckBox, dataBean: WanAndArticle)
}