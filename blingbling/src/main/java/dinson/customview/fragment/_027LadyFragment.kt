package dinson.customview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dinson.customview.R

/**
 * 优优界面
 */
class _027LadyFragment : ViewPagerLazyFragment() {

    override fun lazyInit() {


    }
    override fun onCreateView(original: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return original.inflate(R.layout.fragment_027_lady, container, false)
    }
}
