package dinson.customview.holder

import android.content.Context
import android.view.View
import android.widget.TextView
import dinson.customview.weight._013stepview.StepViewHolder

/**
 * @author Dinson - 2018/2/22
 */
class _013StepViewHolder : StepViewHolder<String> {
    override fun onBind(context: Context, data: String): View {
        val textView = TextView(context)
        textView.text = data
        return textView
    }
}
