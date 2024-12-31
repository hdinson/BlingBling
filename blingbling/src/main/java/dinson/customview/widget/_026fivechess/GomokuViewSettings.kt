package dinson.customview.widget._026fivechess

import android.content.Context
import android.util.AttributeSet
import dinson.customview.R

class GomokuViewSettings(context: Context, attrs: AttributeSet? = null) {

    val mGomokuLineColor: Int

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.GomokuView)
        mGomokuLineColor = attributes.getInt(R.styleable.GomokuView_gomoku_line_color, R.color.black)


        attributes.recycle()
    }


}