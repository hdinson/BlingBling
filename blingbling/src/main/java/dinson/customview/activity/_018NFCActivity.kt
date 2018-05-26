package dinson.customview.activity

import android.content.ComponentName
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.provider.Settings
import dinson.customview.R
import dinson.customview._global.BaseNfcActivity
import dinson.customview.kotlin.*
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
        if (isNfcEnable()) {
            tvTitle.text = getString(R.string.nfc_enable_toast_msg)
        } else {
            tvTitle.text = getString(R.string.nfc_unable_toast_msg)
            tvTitle.click { startActivity(Intent(Settings.ACTION_NFC_SETTINGS)) }
        }
    }

    /**
     * 为程序创建桌面快捷方式
     */
    private fun addShortcut() {
        val shortcut = Intent("com.android.launcher.action.INSTALL_SHORTCUT")

        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name))
        shortcut.putExtra("duplicate", false) //不允许重复创建

        //指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
        //注意: ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序
        val comp = ComponentName(this.packageName, "." + this.localClassName)
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, Intent(Intent.ACTION_MAIN).setComponent(comp))

        //快捷方式的图标
        val iconRes = Intent.ShortcutIconResource.fromContext(this, R.mipmap.app_icon)
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes)
        sendBroadcast(shortcut)
    }


    /**
     * 删除程序的快捷方式
     */
    private fun delShortcut() {
        val shortcut = Intent("com.android.launcher.action.UNINSTALL_SHORTCUT")

        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name))

        //指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
        //注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
        val appClass = this.packageName + "." + this.localClassName
        val comp = ComponentName(this.packageName, appClass)
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, Intent(Intent.ACTION_MAIN).setComponent(comp))

        sendBroadcast(shortcut)

    }
}