package dinson.customview.holder.gankio

import android.view.View
import dinson.customview.R
import dinson.customview.entity.gank.ProjectsInfo
import dinson.customview.entity.gank.ProjectsTitle
import dinson.customview.weight.recycleview.multitype.MultiType
import dinson.customview.weight.recycleview.multitype.MultiTypeViewHolder
import dinson.customview.weight.recycleview.multitype.TypeFactory

class GankIOTypeFactory : TypeFactory {

    companion object {
        const val ITEM_CONTENT = R.layout.item_003_gank_content
        const val ITEM_TITLE = R.layout.item_003_gank_title
    }

    override fun createViewHolder(type: Int, itemView: View): MultiTypeViewHolder<MultiType> {
        return when (type) {
            ITEM_CONTENT -> GankContentViewHolder(itemView)
            ITEM_TITLE -> GankTitleViewHolder(itemView)
            else -> GankTitleViewHolder(itemView)
        }
    }

    override fun createViewType(bean: MultiType) = when (bean) {
        is ProjectsInfo -> ITEM_CONTENT
        is ProjectsTitle -> ITEM_TITLE
        else -> ITEM_TITLE
    }
}

