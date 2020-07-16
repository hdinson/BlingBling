package dinson.customview.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jakewharton.rxbinding2.view.RxView
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview._global.ConstantsUtils
import dinson.customview.fragment._025CaculateDateFragment
import dinson.customview.fragment._025DaysMatterFragment
import dinson.customview.fragment._025OnTheDaysFragment
import com.dinson.blingbase.kotlin.click
import dinson.customview.kotlin.logi
import dinson.customview.utils.SystemBarModeUtils
import kotlinx.android.synthetic.main.activity__025_days_matter.*
import java.io.File
import java.util.concurrent.TimeUnit

class _025DaysMatterActivity : BaseActivity() {

    private val mFragments = arrayListOf<Fragment>(
        _025DaysMatterFragment(), _025OnTheDaysFragment(), _025CaculateDateFragment()
    )
    private val mTitleList = arrayOf("倒数日", "历史上的今天", "日期计算器")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__025_days_matter)

        SystemBarModeUtils.setPaddingTop(this, topLayout)

        vpDaysMatter.adapter = MainAdapter(supportFragmentManager, mFragments)
        vpDaysMatter.offscreenPageLimit = 5
        alphaIndicator.setViewPager(vpDaysMatter)
        tvTitle.text = mTitleList[0]
        ibAddSchedule.click { _025AddScheduleActivity.start(this) }
        vpDaysMatter.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageSelected(p0: Int) {
                tvTitle.text = mTitleList[p0]
                ibAddSchedule.visibility = if (p0 == 0) View.VISIBLE else View.GONE
            }
        })

        RxView.clicks(shareApk).throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                shareApk()
            }.addToManaged()

    }

    private inner class MainAdapter(fm: FragmentManager, val fragments: List<Fragment>)
        : FragmentPagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

    private fun shareApk() {
        val path = applicationInfo.sourceDir
        val apkFile = File(path)


        val targetFile = File("${ConstantsUtils.SDCARD_PRIVATE}$packageName.apk")
        apkFile.copyRecursively(targetFile, true) { _, _ ->
            logi { "CopyRecursively Error  !!" }
            return@copyRecursively OnErrorAction.SKIP
        }

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "*/*"

        val uriForFile = FileProvider.getUriForFile(this, "$packageName.provider", targetFile)
        intent.putExtra(Intent.EXTRA_STREAM, uriForFile)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        startActivity(intent)
    }
}
