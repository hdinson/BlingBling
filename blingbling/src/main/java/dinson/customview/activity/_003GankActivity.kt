package dinson.customview.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dinson.blingbase.utils.SystemBarModeUtils
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.fragment._003GirlFragment
import dinson.customview.fragment._003InfomationFragment
import kotlinx.android.synthetic.main.activity__003_gank.*

class _003GankActivity : BaseActivity() {

    private val mFragments = arrayListOf<Fragment>(
        _003InfomationFragment(), _003GirlFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__003_gank)

        SystemBarModeUtils.darkMode(this, true)
        vpGankContent.adapter = MainAdapter(supportFragmentManager, mFragments)
        vpGankContent.offscreenPageLimit = 5
        alphaIndicator.setViewPager(vpGankContent)
        vpGankContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageSelected(p0: Int) {
            }
        })

    }

    private inner class MainAdapter(fm: FragmentManager, val fragments: List<Fragment>)
        : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

}
