package dinson.customview.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.dinson.blingbase.kotlin.toasty
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.kotlin.logd
import kotlinx.android.synthetic.main.activity__004_bili_bili_video.*
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager


class _004BiliBiliVideoActivity : BaseActivity() {

    companion object {
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_URL = "url"
        fun start(context: Context, title: String, url: String) {
            val intent = Intent(context, _004BiliBiliVideoActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_URL, url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__004_bili_bili_video)
        /* requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE*/

        val mVideoPath = intent.getStringExtra(EXTRA_URL)
        if (mVideoPath.isNullOrEmpty()) {
            "视频地址错误".toasty()
            finish()
            return
        }
        initUI(mVideoPath)
    }

    private var mOrientationUtils: OrientationUtils? = null


    private fun initUI(mVideoPath: String) {
        logd { mVideoPath }

        videoPlayer.setUp(mVideoPath, true, "")
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)//EXO模式
        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.mipmap.ic_launcher)
        videoPlayer.thumbImageView = imageView
        //增加title
        videoPlayer.titleTextView.visibility = View.VISIBLE
        //设置返回键
        videoPlayer.backButton.visibility = View.VISIBLE
        //设置旋转
        mOrientationUtils = OrientationUtils(this, videoPlayer)
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.fullscreenButton.setOnClickListener { mOrientationUtils?.resolveByClick() }
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true)
        //设置返回按键功能
        videoPlayer.backButton.setOnClickListener { onBackPressed() }
        videoPlayer.startPlayLogic()
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        videoPlayer.onVideoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        mOrientationUtils?.releaseListener()
    }

    override fun onBackPressed() {
        //先返回正常状态
        if (mOrientationUtils?.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.fullscreenButton.performClick()
            return
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null)
        super.onBackPressed()
    }
}
