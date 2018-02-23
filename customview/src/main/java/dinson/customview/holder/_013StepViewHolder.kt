package dinson.customview.holder

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import dinson.customview.R
import dinson.customview.weight._013stepview.Position
import dinson.customview.weight._013stepview.State
import dinson.customview.weight._013stepview.StepViewHolder

/**
 * @author Dinson - 2018/2/22
 */
class _013StepViewHolder : StepViewHolder<String> {
    override fun onBind(context: Context, data: String, pos: Position, state: State): View {
        val textView = TextView(context)
        textView.text = data
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16f)
        textView.setTextColor(ContextCompat.getColor(context, R.color.white))
        return textView
    }
}
