package dinson.customview._global

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter

import java.util.concurrent.TimeUnit

import io.reactivex.Observable

open class BaseNfcActivity : BaseActivity() {

    private var mNfcAdapter: NfcAdapter? = null
    private lateinit var mPendingIntent: PendingIntent

    /**
     * 启动Activity，界面可见时
     */
    override fun onStart() {
        super.onStart()
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        //一旦截获NFC消息，就会通过PendingIntent调用窗口
        mPendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass), 0)
    }

    /**
     * 获得焦点，按钮可以点击
     */
    public override fun onResume() {
        super.onResume()
        //设置处理优于所有其他NFC的处理
        Observable.timer(500, TimeUnit.MILLISECONDS).subscribe { _ ->
            mNfcAdapter?.enableForegroundDispatch(this@BaseNfcActivity,
                mPendingIntent, null, null)
        }
    }

    /**
     * 暂停Activity，界面获取焦点，按钮可以点击
     */
    public override fun onPause() {
        super.onPause()
        //恢复默认状态
        mNfcAdapter?.disableForegroundDispatch(this)
    }
}
