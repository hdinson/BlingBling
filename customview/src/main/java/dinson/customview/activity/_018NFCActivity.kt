package dinson.customview.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.tbruyelle.rxpermissions2.RxPermissions
import dinson.customview.R
import dinson.customview._global.BaseNfcActivity
import dinson.customview.kotlin.NfcIsEnable
import dinson.customview.kotlin.then
import dinson.customview.utils.SystemBarModeUtils
import kotlinx.android.synthetic.main.activity__018_nfc.*

class _018NFCActivity : BaseNfcActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__018_nfc)
        SystemBarModeUtils.darkMode(this, true)
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Toast.makeText(this, NfcIsEnable(this) then "nfc已开启" ?: "nfc未开启", Toast.LENGTH_SHORT).show()
        RxPermissions(this).request(Manifest.permission.NFC)
            .subscribe { aBoolean -> Toast.makeText(this, aBoolean then "已同意nfc权限" ?: "不同意", Toast.LENGTH_SHORT).show() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }
}
