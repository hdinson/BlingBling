package dinson.customview.activity

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.provider.Settings
import dinson.customview.R
import dinson.customview._global.BaseNfcActivity
import dinson.customview.kotlin.NfcIsEnable
import dinson.customview.kotlin.click
import dinson.customview.kotlin.debug
import dinson.customview.kotlin.then
import dinson.customview.utils.NfcUtils
import dinson.customview.utils.SystemBarModeUtils
import kotlinx.android.synthetic.main.activity__018_nfc.*


/**
 * NFC工具界面
 */
class _018NFCActivity : BaseNfcActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__018_nfc)

        initUI()
        checkNfcEnable()
    }

    private fun initUI() {
        SystemBarModeUtils.darkMode(this, true)
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        //1.获取Tag对象
        val detectedTag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        val techList = detectedTag.techList
        techList.forEach { debug(it) }
        //2.获取Ndef的实例
        val ndef = Ndef.get(detectedTag)
        tvDesc.append("${ndef.type} Maxsize:${ndef.maxSize}bytes")
        val (b, s) = NfcUtils.writeUri(detectedTag, "http://www.dinson.win")
        tvDesc.append("\n$s")
    }


    private fun checkNfcEnable() {
        //检查模块是否开启
        NfcIsEnable(this).then({
            tvTitle.text = getString(R.string.nfc_enable_toast_msg)
        }, {
            tvTitle.text = getString(R.string.nfc_unable_toast_msg)
            tvTitle.click { startActivity(Intent(Settings.ACTION_NFC_SETTINGS)) }
        })
    }
}