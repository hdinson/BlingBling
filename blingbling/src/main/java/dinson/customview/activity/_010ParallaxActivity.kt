package dinson.customview.activity

import android.os.Bundle
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.fragment._010LoginFragment
import kotlinx.android.synthetic.main.activity__010__parallax.*

class _010ParallaxActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__010__parallax)

        parallax_container.setUp(R.layout.layout_010_splash1, R.layout.layout_010_splash2)
        parallax_container.notifyDataSetChanged(_010LoginFragment.newInstance(R.layout.layout_010_splash3))
    }

    override fun setWindowBackgroundColor(): Int = R.color._010_window_bg
}
