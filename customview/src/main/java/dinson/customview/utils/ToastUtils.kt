package dinson.customview.utils

import android.view.Gravity
import android.widget.Toast
import dinson.customview.R
import dinson.customview._global.GlobalApplication


/**
 * Toast工具
 */
object ToastUtils {
    private val mToast by lazy {
        val toast = Toast.makeText(GlobalApplication.getContext(), "", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.view.setBackgroundResource(R.drawable.toast_bg)
        toast
    }

    fun showToast(message: String) {
        mToast.setText(message)
        mToast.show()
    }
}