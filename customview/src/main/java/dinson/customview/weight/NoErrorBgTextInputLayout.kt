package dinson.customview.weight

import android.content.Context
import android.graphics.PorterDuff
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import dinson.customview.R


/**
 * 设置error背景不变色的TextInputLayout
 * 使用TextInputLayout中的setError功能时,不只会出现错误提示,而且会将EditText的背景渲染成提示文字的颜色
 */
class NoErrorBgTextInputLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : TextInputLayout(context, attrs, defStyleAttr) {


    override fun setError(error: CharSequence?) {
        super.setError(error)
        recoverEditTextBackGround()
    }

    /**
     * 将背景颜色重置
     */
    private fun recoverEditTextBackGround() {
        editText?.background?.mutate()?.setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.DST_IN)
    }
}