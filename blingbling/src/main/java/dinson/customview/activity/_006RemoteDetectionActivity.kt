package dinson.customview.activity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.dinson.blingbase.kotlin.toasty
import com.dinson.blingbase.utils.BitmapUtils
import com.dinson.blingbase.utils.SystemBarModeUtils
import com.huawei.hms.mlsdk.document.MLDocument
import com.tbruyelle.rxpermissions2.RxPermissions
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.kotlin.loge
import dinson.customview.kotlin.logi
import dinson.customview.weight._006hms.callback.OnMLDocumentListener
import dinson.customview.weight._006hms.transactor.DocumentTransactor
import kotlinx.android.synthetic.main.activity__006_remote_detection.*

class _006RemoteDetectionActivity : BaseActivity(), OnMLDocumentListener {

    private var mUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__006_remote_detection)
        SystemBarModeUtils.darkMode(this, true)
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val startMode = intent.extras?.getString(EXTRA_TYPE, TAKE_PHOTO) ?: TAKE_PHOTO

        initFinishJumpToStart(startMode)
    }


    private fun initFinishJumpToStart(startMode: String) {
        when (startMode) {
            TAKE_PHOTO -> {
                RxPermissions(this).request(Manifest.permission.CAMERA)
                    .subscribe {
                        if (it.not()) {
                            "图像识别需要摄像机权限".toasty()
                            return@subscribe
                        }
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                            val values = ContentValues()
                            values.put(MediaStore.Images.Media.TITLE, "New Picture")
                            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
                            mUri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
                            this.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                        }
                    }
            }
            SELECT_IMAGE -> {
                val intent = Intent(Intent.ACTION_PICK, null)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                this.startActivityForResult(intent, REQUEST_SELECT_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            onBackPressed()
            return
        }
        if (requestCode == REQUEST_TAKE_PHOTO) {
            reloadAndDetectImage()
            return
        }
        if (requestCode == REQUEST_SELECT_IMAGE) {
            if (data != null) {
                mUri = data.data
                reloadAndDetectImage()
            }
            return
        }
    }

    private val mDocumentTransactor by lazy {
        val documentTransactor = DocumentTransactor()
        documentTransactor.setOnMLDocumentListener(this)
        documentTransactor
    }

    private fun reloadAndDetectImage() {
        if (mUri == null) {
            return
        }

        //todo 显示进度条
        golOverLay.clear()
        val bitmap = BitmapFactory.decodeStream(this.contentResolver.openInputStream(mUri!!))
        ivPreview.setImageBitmap(bitmap)
        mDocumentTransactor.process(bitmap)
    }


    companion object {
        private const val EXTRA_TYPE = "extra_type"
        private const val TAKE_PHOTO = "take_photo"
        private const val SELECT_IMAGE = "select_image"


        private const val REQUEST_TAKE_PHOTO = 1
        private const val REQUEST_SELECT_IMAGE = 2

        fun getTakePhotoIntent(context: Context): Intent {
            val intent = Intent(context, _006RemoteDetectionActivity::class.java)
            intent.putExtra(EXTRA_TYPE, TAKE_PHOTO)
            return intent
        }

        fun getSelectImageIntent(context: Context): Intent {
            val intent = Intent(context, _006RemoteDetectionActivity::class.java)
            intent.putExtra(EXTRA_TYPE, SELECT_IMAGE)
            return intent
        }
    }


    /**
     * 识别成功
     */
    override fun onSuccessResult(bitmap: Bitmap, it: MLDocument) {
        logi { "识别成功: ${it.stringValue}" }
    }

    /**
     * 识别失败
     */
    override fun onFailure(e: Exception) {
        loge(e::toString)
    }
}