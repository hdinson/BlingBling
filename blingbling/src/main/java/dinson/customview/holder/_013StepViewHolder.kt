package dinson.customview.holder

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import dinson.customview.BuildConfig
import com.dinson.blingbase.kotlin.dip
import dinson.customview.utils.GlideUtils
import dinson.customview.widget._013stepview.Position
import dinson.customview.widget._013stepview.State
import dinson.customview.widget._013stepview.StepViewHolder

/**
 * 步骤holder
 */
class _013StepViewHolder : StepViewHolder<String> {
    override fun onBind(context: Context, data: String, pos: Position, state: State): View {
        val textView = TextView(context)
        textView.text = data
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        textView.setTextColor(Color.parseColor("#9d9d9d"))

        //最后一张加一个图片
        if (pos == Position.End) {
            val llContainer = LinearLayout(context)
            llContainer.orientation = LinearLayout.VERTICAL
            val params = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            params.bottomMargin =  dip(16)
            val ivImg = ImageView(context)
            ivImg.adjustViewBounds = true
            GlideUtils.setImage(context, "${BuildConfig.QINIU_URL}FoxNtdwL6G0wdXUos7WNhswu1M_f.webp", ivImg)
            llContainer.addView(ivImg, params)
            llContainer.addView(textView)
            return llContainer
        }

        return textView
    }
}
