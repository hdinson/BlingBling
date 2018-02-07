package dinson.customview.weight._002qqnaviview

import android.view.View
import java.util.*


/**
 * QQ导航栏的管理类
 */
class QQNaviViewManager(vararg views: QQNaviView) {

    private lateinit var currentCheckedView: QQNaviView
    private val mList = ArrayList<QQNaviView>()
    private var mOffsetOrientation = OffsetOrientation.Horizontal

    /**
     * 设置图像的偏移方向
     */
    fun setOrientation(orientation: OffsetOrientation) {
        mOffsetOrientation = orientation
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

        }



        when {
            mList.contains(view) && currentCheckedView != view -> {
                currentCheckedView.setChecked(false)
                currentCheckedView = view
                currentCheckedView.setChecked(true)
            }

        }


        this.currentCheckedView = view
        view.setChecked(true)
    }
/*
    *//**
     * 清除当前点击的控件
     *//*
    fun clearCurrentCheckedView() {
        if (currentCheckedView != null)
            this.currentCheckedView!!.setChecked(false)
        this.currentCheckedView = null
    }

    *//**
     * 判断当前是否可以选中
     *//*
    fun isCanCheck(view: View): Boolean {
        return currentCheckedView != view
    }


    *//**
     * 初始化
     *//*
    fun initCurrentLayout() {
        if (currentCheckedView != null) {
            currentCheckedView = null
        }
    }*/
}
