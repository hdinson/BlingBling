package dinson.customview.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import dinson.customview.fragment._027CategoryFragment
import dinson.customview.fragment._027HomeFragment
import dinson.customview.fragment._027LadyFragment

/**
 * 联动右边的适配器
 */
class _027VpAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val mFragments =
        arrayOf(_027HomeFragment(), _027LadyFragment(), _027CategoryFragment())

    override fun getItem(index: Int): Fragment {
        return mFragments[index]
    }

    override fun getCount() = mFragments.size
}
