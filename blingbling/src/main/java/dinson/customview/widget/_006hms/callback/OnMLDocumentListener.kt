package dinson.customview.widget._006hms.callback

import android.graphics.Bitmap
import com.huawei.hms.mlsdk.document.MLDocument

interface OnMLDocumentListener {
    fun onSuccessResult(bitmap: Bitmap, it: MLDocument)
    fun onFailure(e: Exception)
}