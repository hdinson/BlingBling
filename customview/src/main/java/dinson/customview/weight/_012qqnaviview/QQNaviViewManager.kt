package dinson.customview.weight._012qqnaviview

import android.view.View
import dinson.customview.utils.LogUtils
import java.util.*


/**
 * QQ导航栏的管理类
 */
class QQNaviViewManager(vararg views: QQNaviView) {

    private lateinit var currentCheckedView: QQNaviView
    private val mList = ArrayList<QQNaviView>()
    private var mIsHorizontal = true

    /**
     * 设置图像的偏移方向
     */
    fun setOrientation(isHorizontal: Boolean) {
        mIsHorizontal = isHorizontal
    }

    init {
        views.forEach { mList.add(it) }
        mList[0].apply {
            currentCheckedView = this
            setChecked(true)
        }
    }

    /**
     * 设置当前点击的控件
     */
    fun setCheckedView(view: QQNaviView) {
        if (currentCheckedView != view) {
            if (!mList.contains(view)) mList.add(view)
            currentCheckedView.setChecked(false)
            view.setChecked(true)
            currentCheckedView=view
            val pos = mList.indexOf(view)
            mList.forEachIndexed { index, it ->
                if (index==pos)return@forEachIndexed
                LogUtils.e("index:$index")
                if (index < pos) {
                    if (mIsHorizontal) {
                        it.setOffsetOrientation(OffsetOrientation.RIGHT)
                    } else {
                        it.setOffsetOrientation(OffsetOrientation.BOTTOM)
                    }
                } else {
                    if (mIsHorizontal) {
                        it.setOffsetOrientation(OffsetOrientation.LEFT)
                    } else {
                        it.setOffsetOrientation(OffsetOrientation.TOP)
                    }

                }

            }
        }
    }


    /**
     * 判断当前是否可以选中
     */
    fun isCanCheck(view: View): Boolean {
        return currentCheckedView != view
    }

}
