package dinson.customview._global;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class BaseNfcActivity extends BaseActivity {

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;

    /**
     * 启动Activity，界面可见时
     */
    @Override
    protected void onStart() {
        super.onStart();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //一旦截获NFC消息，就会通过PendingIntent调用窗口
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
    }

    /**
     * 获得焦点，按钮可以点击
     */
    @Override
    public void onResume() {
        super.onResume();
        //设置处理优于所有其他NFC的处理
        if (mNfcAdapter != null)
            Observable.timer(500, TimeUnit.MILLISECONDS).subscribe(aLong ->
                mNfcAdapter.enableForegroundDispatch(BaseNfcActivity.this,
                    mPendingIntent, null, null));

    }


    /**
     * 暂停Activity，界面获取焦点，按钮可以点击
     */
    @Override
    public void onPause() {
        super.onPause();
        //恢复默认状态
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }
}
