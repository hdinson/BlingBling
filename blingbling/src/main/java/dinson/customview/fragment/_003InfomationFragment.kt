package dinson.customview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import dinson.customview.R
import dinson.customview._global.BaseFragment
import dinson.customview.utils.SystemBarModeUtils

import kotlinx.android.synthetic.main.fragment_003_information.*
import java.util.ArrayList

/**
 * 资讯界面
 */
class _003InfomationFragment : BaseFragment() {

    companion object {
        const val Android_Column = "5597838ee4b08a686ce2319d"
        const val Flutter_Column = "5a96291f6fb9a0535b535438"
        const val Dart_Column = "5a962a8051882571faa854ed"
        const val Android_Jetpack_Column = "5afd356b6fb9a0535b53543c"
        const val RxJava_Column  = "55a11a3ee4b0e8740af11997"
        const val Python_Column  = "559a7227e4b08a686d25744f"
    }

    override fun onCreateView(original: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return original.inflate(R.layout.fragment_003_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()

        SystemBarModeUtils.setPaddingTop(view.context,tlInformation)
        tlInformation.setupWithViewPager(vpInformationPage)
        tlInformation.tabMode = TabLayout.MODE_SCROLLABLE
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFrag(_003JueJinFlutterFragment.newInstance(Android_Column), "Android")
        adapter.addFrag(_003JueJinFlutterFragment.newInstance(Flutter_Column), "flutter")
        adapter.addFrag(_003JueJinFlutterFragment.newInstance(Dart_Column), "Dart")
        adapter.addFrag(_003JueJinFlutterFragment.newInstance(Android_Jetpack_Column), "Jetpack")
        adapter.addFrag(_003JueJinFlutterFragment.newInstance(RxJava_Column), "RxJava")
        adapter.addFrag(_003JueJinFlutterFragment.newInstance(Python_Column), "Python")
        vpInformationPage.adapter = adapter
        vpInformationPage.offscreenPageLimit = adapter.count
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
