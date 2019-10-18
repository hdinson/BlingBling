package dinson.customview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dinson.customview.R
import dinson.customview._global.BaseFragment
import dinson.customview.kotlin.loge

import dinson.customview.weight._010parallaxsplash.ParallaxFragment

/**
 * 资讯界面
 */
class _003InfomationFragment : BaseFragment() {

    override fun onCreateView(original: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return original.inflate(R.layout.fragment_003_information, container, false)
    }


}
