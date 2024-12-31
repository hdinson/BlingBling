package dinson.customview.widget._006hms.transactor

import android.graphics.Bitmap
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import dinson.customview.widget._006hms.callback.OnMLDocumentListener

class DocumentTransactor {

    private var mDetector = MLAnalyzerFactory.getInstance().remoteDocumentAnalyzer

    private var mListener: OnMLDocumentListener? = null

    fun process(bitmap: Bitmap) {
        val frame = MLFrame.Creator().setBitmap(bitmap).create()
        detectInVisionImage(bitmap, frame)
    }

    fun stop() {
        mDetector.stop()
    }

    fun setOnMLDocumentListener(listener: OnMLDocumentListener) {
        mListener = listener
    }

    private fun detectInVisionImage(bitmap: Bitmap, image: MLFrame) {
        mDetector.asyncAnalyseFrame(image)
            .addOnSuccessListener {
                mListener?.onSuccessResult(bitmap, it)
            }
            .addOnFailureListener {
                mListener?.onFailure(it)
            }
    }
}