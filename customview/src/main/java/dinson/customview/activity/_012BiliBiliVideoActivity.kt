package dinson.customview.activity

import android.content.Context
import android.content.Intent
import dinson.customview._global.BaseActivity

class _012BiliBiliVideoActivity : BaseActivity() {

    companion object {
        private val EXTRA_TITLE = "title"
        private val EXTRA_URL = "url"
        fun start(context: Context, title: String, url: String) {
            val intent = Intent(context, _012BiliBiliVideoActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_URL, url)
            context.startActivity(intent)
        }
    }

    /* override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity__012_bili_bili_video)

         val mVideoPath = intent.getStringExtra(EXTRA_URL)
         if (StringUtils.isEmpty(mVideoPath)) {
             UIUtils.showToast("视频地址错误")
             finish()
             return
         }
         initUI(mVideoPath)
     }

     private fun initUI(mVideoPath: String) {
         LogUtils.e(mVideoPath)

         // init player
 //        IjkMediaPlayer.loadLibrariesOnce(null)
 //        IjkMediaPlayer.native_profileBegin("libijkplayer.so")

         ijkVideoView.setVideoPath(mVideoPath)
         ijkVideoView.setAsFillParent()
         ijkVideoView.start()
     }

     private var mBackPressed = false
     override fun onBackPressed() {
         mBackPressed = true
         super.onBackPressed()
     }

     override fun onStop() {
         super.onStop()
         if (mBackPressed || !ijkVideoView.isBackgroundPlayEnabled) {
             ijkVideoView.stopPlayback()
             ijkVideoView.release(true)
             ijkVideoView.stopBackgroundPlay()
         } else {
             ijkVideoView.enterBackground()
         }
         IjkMediaPlayer.native_profileEnd()
     }
 */
}
