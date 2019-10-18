package dinson.customview.weight.banner

import android.annotation.SuppressLint
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import dinson.customview.utils.LogUtils
import dinson.customview.weight.banner.holder.BannerViewHolder


/**
 *   广播轮播图数据适配器
 */
class BannerPagerAdapter<T>(private val mDatas: List<T>,
                            private val mViewHolder: BannerViewHolder<T>,
                            private val canLoop: Boolean) : PagerAdapter() {


    companion object {
        private const val LOOPERCOUNTFACTOR = 1000
    }

    @SuppressLint("UseSparseArrays")
    private val mViewList = SparseArray<View>()
    private var mPageClickListener: BannerPageClickListener? = null


    /**
     * viewPager视图
     */
    private fun getView(position: Int, container: ViewGroup): View {
        val realPosition = position % getRealCount()
        var view = mViewList.get(realPosition)
        if (view == null) {
            view = mViewHolder.createView(container.context)
            mViewList.put(realPosition, view)
            mViewHolder.onBind(container.context, realPosition, mDatas[realPosition])
        }
        // 添加点击事件
        view.setOnClickListener { mPageClickListener?.onPageClick(it, realPosition) }
        return view
    }


    override fun getCount() = if (canLoop) getVirtualCount() else getRealCount()

    /**
     * 获取真实的Count
     */
    private fun getRealCount() = mDatas.size

    /**
     * 获取真实的Count
     */
    private fun getVirtualCount() = getRealCount() * LOOPERCOUNTFACTOR

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = getView(position, container)
        container.removeView(view)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        LogUtils.e("container: ${container .childCount}")
    }


    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/
    /**
     * 初始化Adapter和设置当前选中的Item
     *
     * @param viewPager
     */
    fun setUpViewViewPager(viewPager: ViewPager) {
        viewPager.adapter = this
        this.notifyDataSetChanged()
        val currentItem = if (canLoop) getVirtualCount() / 2 - getVirtualCount() / 2 % getRealCount() else 0
        //设置当前选中的Item
        viewPager.currentItem = currentItem
    }

    fun setPageClickListener(pageClickListener: BannerPageClickListener?) {
        mPageClickListener = pageClickListener
    }

}