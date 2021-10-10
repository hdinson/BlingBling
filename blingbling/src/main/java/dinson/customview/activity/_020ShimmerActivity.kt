package dinson.customview.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.utils.glide.BlurTransformation
import dinson.customview.widget._020shimmerlayout.ShimmerFrameLayout
import kotlinx.android.synthetic.main.activity__020_shimmer.*

class _020ShimmerActivity : BaseActivity() {
    private lateinit var mPresetButtons: Array<Button>
    private var mCurrentPreset = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__020_shimmer)

        //设置背景（高斯模糊）
        val options = RequestOptions().transform(BlurTransformation(this, 23))
        // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
        Glide.with(this).load(R.drawable._020_bg).apply(options).into(ivImg)

        mPresetButtons = arrayOf(presetButton0, presetButton1, presetButton2, presetButton3, presetButton4)
        for (i in mPresetButtons.indices) {
            mPresetButtons[i].setOnClickListener { _ -> selectPreset(i) }
        }
    }

    override fun onStart() {
        super.onStart()
        selectPreset(0)
    }

    public override fun onResume() {
        super.onResume()
        shimmerContainer.startShimmerAnimation()
    }

    public override fun onPause() {
        shimmerContainer.stopShimmerAnimation()
        super.onPause()
    }

    /**
     * Select one of the shimmer animation presets.
     *
     * @param preset index of the shimmer animation preset
     */
    @SuppressLint("SetTextI18n")
    private fun selectPreset(preset: Int) {
        if (mCurrentPreset == preset) {
            return
        }
        if (mCurrentPreset >= 0) {
            mPresetButtons [mCurrentPreset].setBackgroundResource(R.color.transparent)
        }
        mCurrentPreset = preset
        mPresetButtons [mCurrentPreset].setBackgroundResource(R.color.transparent40)

        // Save the state of the animation
        val isPlaying = shimmerContainer.isAnimationStarted

        // Reset all parameters of the shimmer animation
        shimmerContainer.useDefaults()

        when (preset) {
            0 ->
                // Default
                tvTitle.text = "Default"
            1 -> {
                // Slow and reverse
                shimmerContainer.duration = 5000
                shimmerContainer.repeatMode = ObjectAnimator.REVERSE
                tvTitle.text = "Slow and reverse"
            }
            2 -> {
                // Thin, straight and transparent
                shimmerContainer.baseAlpha = 0.1f
                shimmerContainer.dropoff = 0.1f
                shimmerContainer.tilt = 0f
                tvTitle.text = "Thin, straight and transparent"
            }
            3 -> {
                // Sweep angle 90
                shimmerContainer.angle = ShimmerFrameLayout.MaskAngle.CW_90
                tvTitle.text = "Sweep angle 90"
            }
            4 -> {
                // Spotlight
                shimmerContainer.baseAlpha = 0f
                shimmerContainer.duration = 2000
                shimmerContainer.dropoff = 0.1f
                shimmerContainer.intensity = 0.35f
                shimmerContainer.maskShape = ShimmerFrameLayout.MaskShape.RADIAL
                tvTitle.text = "Spotlight"
            }
            else -> tvTitle.text = "Default"
        }

        // Setting a value on the shimmer layout stops the animation. Restart it, if necessary.
        if (isPlaying) {
            shimmerContainer.startShimmerAnimation()
        }
    }
}
