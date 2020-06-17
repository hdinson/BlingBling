package dinson.customview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dinson.customview.R
import dinson.customview._global.BaseFragment
import com.dinson.blingbase.kotlin.logi
import dinson.customview.utils.SystemBarModeUtils
import kotlinx.android.synthetic.main.fragment_003_girl.*
import java.util.ArrayList

/**
 * 妹子界面
 */
class _003GirlFragment : BaseFragment() {

    override fun onCreateView(original: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return original.inflate(R.layout.fragment_003_girl, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        SystemBarModeUtils.setPaddingTop(view.context,tlGirl)
        tlGirl.setupWithViewPager(vpGirlPage)
        tlGirl.tabMode = TabLayout.MODE_SCROLLABLE
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFrag(_003GirlGankFragment(), "Gank")
        adapter.addFrag(_003GirlMeiZiTuTopFragment(), "精选")
        adapter.addFrag(_003GirlMeiZiTuNewFragment(), "最新")
        adapter.addFrag(_003GirlMeiZiTuHotFragment(), "最热")
        adapter.addFrag(_003GirlMeiZiTuSelfieFragment(), "自拍")
        adapter.addFrag(_003GirlMeiZiTuStreetFragment(), "街拍")
        vpGirlPage.adapter = adapter
        vpGirlPage.offscreenPageLimit = adapter.count
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount() = mFragmentList.size

        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()


        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

}
