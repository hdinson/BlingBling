package dinson.customview.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_SHORT
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.kotlin.loge
import dinson.customview.utils.SystemBarModeUtils
import dinson.customview.weight._016parallaximgview.GyroscopeObserver
import kotlinx.android.synthetic.main.activity__016_parallax_img_view.*

class _016ParallaxImgViewActivity : BaseActivity() {

    private   var mGyroscopeObserver: GyroscopeObserver?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__016_parallax_img_view)
        SystemBarModeUtils.darkMode(this, true)

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE)) {
            loge("当前设备不支持陀螺仪")
            Snackbar.make(parallaxView, "当前设备不支持陀螺仪", LENGTH_SHORT).show()
        }

        mGyroscopeObserver = GyroscopeObserver()
        mGyroscopeObserver?.apply { parallaxView.setGyroscopeObserver(this) }
        val imageList = arrayListOf(
            R.drawable._016_layer1, R.drawable._016_layer2, R.drawable._016_layer3,
            R.drawable._016_layer4, R.drawable._016_layer5, R.drawable._016_layer6)
        parallaxView.setImageList(imageList)
    }

    override fun onResume() {
        super.onResume()
        mGyroscopeObserver?.register(this)
    }

    override fun onPause() {
        super.onPause()
        mGyroscopeObserver?.unregister()
    }

    override fun isDestroyed(): Boolean {
        mGyroscopeObserver?.destroy()
        return super.isDestroyed()
    }
}
