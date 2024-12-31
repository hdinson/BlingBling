package dinson.customview.activity

import android.os.Bundle
import com.dinson.blingbase.utils.SystemBarModeUtils
import dinson.customview.R
import dinson.customview._global.BaseActivity
import kotlinx.android.synthetic.main.activity_029_notification_test.*

class _029NotificationTestActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_029_notification_test)

        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }


    }
}