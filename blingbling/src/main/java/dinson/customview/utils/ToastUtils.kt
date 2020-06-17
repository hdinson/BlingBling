package dinson.customview.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.dinson.blingbase.utils.RxBling
import dinson.customview.R
import dinson.customview._global.GlobalApplication


/**
 * Toast工具
 */
@SuppressLint("ShowToast")
object ToastUtils {
    private val mToast by lazy {
        val toast = Toast.makeText(RxBling.context, "", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.view.setBackgroundResource(R.drawable.toast_bg)
        toast.view.findViewById<TextView>(android.R.id.message).apply {
            setTextColor(Color.WHITE)
            textSize = 16f
        }
        toast
    }

    fun showToast(message: String) {
        mToast.setText(message)
        mToast.show()
    }
}