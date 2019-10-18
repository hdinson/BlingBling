package dinson.customview.activity

import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.utils.TypefaceUtils
import dinson.customview.weight._007spotlight.CustomTarget
import dinson.customview.weight._007spotlight.SimpleTarget
import dinson.customview.weight._007spotlight.Spotlight
import kotlinx.android.synthetic.main.activity__007_spotlight.*

class _007SpotlightActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__007_spotlight)

        initUI()
    }

    private fun initUI() {
        val typeface = TypefaceUtils.getAppleFont(this)
        tvTitle.typeface = typeface
        tvDis.typeface = typeface
        tvType.typeface = typeface
        tvName.typeface = typeface
        tvContent.typeface = typeface
        cancel.setOnClickListener { onBackPressed() }
    }


    private var isSpotLightShowed = false
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (!hasFocus || isSpotLightShowed) return
        val radius = Math.hypot(ivImg.width.toDouble(), ivImg.height.toDouble()).toFloat() / 2f
        val firstTarget = SimpleTarget.Builder(this).setPoint(ivImg)
            .setRadius(radius)
            .setTitle("御三家")
            .setDescription("小龟宝可梦")
            .build()

        val oneLocation = IntArray(2)
        tvType.getLocationInWindow(oneLocation)
        val oneX = oneLocation[0] + tvType.width / 4f * 3
        val oneY = oneLocation[1] + tvType.height / 2f
        val secondTarget = SimpleTarget.Builder(this).setPoint(oneX, oneY)
            .setRadius(100f)
            .setTitle("水系")
            .setDescription("特性：激流")
            .build()

        val thirdTarget = CustomTarget.Builder(this).setPoint(cancel)
            .setRadius(100f)
            .setView(R.layout.layout_007_spotlight_custom)
            .build()

        Spotlight.with(this)
            .setDuration(1000L)
            .setAnimation(DecelerateInterpolator(2f))
            .setTargets(firstTarget, secondTarget, thirdTarget)
            .setOnSpotlightStartedListener { isSpotLightShowed = true }
            .start()
    }

    override fun onBackPressed() {
        if (!isSpotLightShowed) return
        super.onBackPressed()
    }

    override fun setWindowBackgroundColor(): Nothing? = null
}
