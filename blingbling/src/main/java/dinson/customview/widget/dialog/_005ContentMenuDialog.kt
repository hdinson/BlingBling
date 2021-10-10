package dinson.customview.widget.dialog

import android.app.Dialog
import android.content.Context
import androidx.core.content.ContextCompat
import android.widget.LinearLayout
import android.widget.TextView
import dinson.customview.R
import com.dinson.blingbase.kotlin.click
import com.dinson.blingbase.kotlin.screenWidth

/**
 * 微信长按后出来的菜单
 */
class _005ContentMenuDialog(context: Context, theme: Int = R.style.BaseDialogTheme) : Dialog(context, theme) {
    private val mTextSize = 16
    private val mPadding = intArrayOf(60, 35, 50, 35)

    private val mContainer: LinearLayout = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        setBackgroundResource(R.drawable.shape_rec_r5)
        backgroundTintList = ContextCompat.getColorStateList(getContext(), R.color.white)
        dividerDrawable = ContextCompat.getDrawable(context, R.drawable.line_horizontal)
        showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
    }

    init {
        setContentView(mContainer)
        //设置是否可取消
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    fun setDatas(item: Array<String>, listener: OnItemClickListener?) {
        for (i in item.indices) {
            val textView = TextView(context)
            val layoutParams = LinearLayout.LayoutParams(context.screenWidth() / 5 * 4, LinearLayout.LayoutParams.WRAP_CONTENT)
            textView.layoutParams = layoutParams
            textView.textSize = mTextSize.toFloat()
            textView.setPadding(mPadding[0], mPadding[1], mPadding[2], mPadding[3])
            textView.text = item[i]
            listener?.apply { textView.click { this.onItemClick(i) } }
            mContainer.addView(textView)
        }
    }
}
