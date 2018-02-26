package dinson.customview.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dinson.customview.R
import dinson.customview.weight._016parallaximgview.GyroscopeObserver
import kotlinx.android.synthetic.main.activity__016_parallax_img_view.*

class _016ParallaxImgViewActivity : AppCompatActivity() {

    private lateinit var mGyroscopeObserver: GyroscopeObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__016_parallax_img_view)

        mGyroscopeObserver = GyroscopeObserver()
        panorama_image_view.setGyroscopeObserver(mGyroscopeObserver)
    }

    override fun onResume() {
        super.onResume()
        mGyroscopeObserver.register(this)
    }

    override fun onPause() {
        super.onPause()
        mGyroscopeObserver.unregister()
    }
}
