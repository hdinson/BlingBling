package dinson.customview.fragment

import dinson.customview._global.BaseFragment


/**
 * 懒加载
 */
abstract class ViewPagerLazyFragment : BaseFragment() {


    private var isLoaded = false

    override fun onResume() {
        super.onResume()
        if (!isLoaded) {
            lazyInit()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    abstract fun lazyInit()
}