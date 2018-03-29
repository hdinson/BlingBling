package dinson.customview.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.qiniu.storage.model.FileInfo
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview._global.ConstantsUtils
import dinson.customview.databinding.ActivityTestBinding
import dinson.customview.kotlin.info
import dinson.customview.model._005FileInfo
import dinson.customview.utils.AESUtils
import dinson.customview.utils.SystemBarModeUtils
import kotlinx.android.synthetic.main.activity_test.*


class TestActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_test)

        val binding = DataBindingUtil.setContentView<ActivityTestBinding>(this, R.layout.activity_test)

        //val inflate = ActivityTestBinding.inflate(layoutInflater)
        val fileInfo = FileInfo()
        fileInfo.fsize=2
        binding.dinsonFile= _005FileInfo(fileInfo,"")
        initUI()
    }

    private fun initUI() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun onEncryption(view: View) {
        val str = "jtef:/dfas[23ajfkav8293.12!@#$%^&*()_+}{:ha"
        info( str)
        val code = AESUtils.encrypt(ConstantsUtils.PACKAGE_NAME, str)
        info( code)
        val code2 = AESUtils.decrypt(ConstantsUtils.PACKAGE_NAME, code)
        info( code2)
    }

    fun onQiNiuQuery(view: View) {

    }
}