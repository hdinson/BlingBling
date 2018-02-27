package dinson.customview.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dinson.customview.R
import dinson.customview.utils.SystemBarModeUtils
import dinson.customview.weight._016parallaximgview.GyroscopeObserver
import kotlinx.android.synthetic.main.activity__016_parallax_img_view.*

class _016ParallaxImgViewActivity : AppCompatActivity() {

    private lateinit var mGyroscopeObserver: GyroscopeObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__016_parallax_img_view)
        SystemBarModeUtils.darkMode(this, true)

        mGyroscopeObserver = GyroscopeObserver()
        panorama_image_view.setGyroscopeObserver(mGyroscopeObserver)
         val imageList = arrayListOf(
             R.drawable._016_layer1, R.drawable._016_layer2, R.drawable._016_layer3,
             R.drawable._016_layer4, R.drawable._016_layer5, R.drawable._016_layer6)
        panorama_image_view.setImageList(imageList)
    }

    override fun onResume() {
        super.onResume()
        mGyroscopeObserver.register(this)
    }

    override fun onPause() {
        super.onPause()
        mGyroscopeObserver.unregister()
    }

    override fun isDestroyed(): Boolean {
        mGyroscopeObserver.destroy()
        return super.isDestroyed()
    }
}
