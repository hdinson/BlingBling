package dinson.customview.weight._026fivechess

import android.content.Context
import android.util.AttributeSet
import dinson.customview.R

class GomokuViewSettings(context: Context, attrs: AttributeSet? = null) {

    val mGomokuSize: Int

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.GomokuView)
        mGomokuSize = attributes.getInt(R.styleable.GomokuView_gomoku_size, 15)


        attributes.recycle()
    }


}