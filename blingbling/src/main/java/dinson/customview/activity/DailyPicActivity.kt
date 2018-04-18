package dinson.customview.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import android.view.ViewAnimationUtils
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.entity.one.DailyDetail
import dinson.customview.kotlin.screenWidth
import kotlinx.android.synthetic.main.activity_daily_pic.*

class DailyPicActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_pic)

        initUI()
    }

    private fun initUI() {
        val data = intent.getParcelableExtra<DailyDetail.DataBean>(EXTRA_DATA)
        val path = intent.getStringExtra(EXTRA_PATH)

        val bitmap = BitmapFactory.decodeFile(path)
        val screenWidth = screenWidth()
        val height = screenWidth * bitmap.height / bitmap.width
        val para = ivImg.layoutParams
        para.height = height
        para.width = screenWidth
        ivImg.setImageBitmap(bitmap)
        ivImg.setOnClickListener({ onBackPressed() })

        val content = data.hp_content
        val indexOfStop = content.lastIndexOf("by")
        val indexOfQm = content.lastIndexOf("from")
        val index = if (indexOfStop > indexOfQm) indexOfStop else indexOfQm

        val subContent = content.substring(0, index)
        val subName = content.substring(index).trim { it <= ' ' }
        tvTitle.text = subName
        tvContent.text = subContent

        llLogin.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int,
                                        oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                if (top < 0) return
                v.removeOnLayoutChangeListener(this)

                //动画操作
                val viewHeight = bottom - top
                val width = right - left

                val animator = ViewAnimationUtils.createCircularReveal(v, 0, height, 0f,
                    Math.hypot(width.toDouble(), viewHeight.toDouble()).toFloat())
                animator.duration = 700
                animator.start()
            }
        })
    }

    override fun finishWithAnim(): Boolean = false

    companion object {

        private const val EXTRA_DATA = "data"
        private const val EXTRA_PATH = "path"

        fun start(context: Context, bean: DailyDetail.DataBean, imgPath: String, options: ActivityOptionsCompat) {
            val starter = Intent(context, DailyPicActivity::class.java)
            starter.putExtra(EXTRA_DATA, bean)
            starter.putExtra(EXTRA_PATH, imgPath)
            context.startActivity(starter, options.toBundle())
        }
    }
}
