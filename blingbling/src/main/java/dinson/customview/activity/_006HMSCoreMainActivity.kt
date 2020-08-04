package dinson.customview.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.dinson.blingbase.utils.SystemBarModeUtils
import com.dinson.blingbase.widget.recycleview.RvItemClickSupport
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._006HomeItemAdapter
import dinson.customview.entity._006HomeItem
import kotlinx.android.synthetic.main.activity__006_hms_core_main.*

class _006HMSCoreMainActivity : BaseActivity() {

    private val mDataList by lazy { createDataList() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__006_hms_core_main)

        SystemBarModeUtils.setPaddingTop(this, actTopBar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val adapter = _006HomeItemAdapter(mDataList)
        rvContent.adapter = adapter
        rvContent.layoutManager = GridLayoutManager(this, 3)
        RvItemClickSupport.addTo(rvContent).setOnItemClickListener { _, _, position ->
            startActivity(mDataList[position].intent)
        }
    }

    private fun createDataList(): ArrayList<_006HomeItem> {
        val arr = ArrayList<_006HomeItem>()
        val takePhotoIntent = _006RemoteDetectionActivity.getTakePhotoIntent(this)
        arr.add(_006HomeItem("文档识别(拍照)", R.drawable._006_ic_document,
            false, takePhotoIntent))

        val selectImageIntent = _006RemoteDetectionActivity.getSelectImageIntent(this)
        arr.add(_006HomeItem("文档识别(相册)", R.drawable._006_ic_document,
            false, selectImageIntent))

        return arr
    }
}