package com.dinson.blingbase.kotlin

import android.animation.ValueAnimator
import android.graphics.Path
import android.graphics.PathMeasure
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout


/**
 *  View相关的扩展方法
 */
fun View.click(function: (view: View) -> Unit) = setOnClickListener { function(it) }

fun Thread.run(function: () -> Unit) {

}

fun View.longClick(function: (view: View) -> Boolean) = setOnLongClickListener { function(it) }

fun View.halfWidth() = width / 2f

fun View.halfHeight() = height / 2f

infix fun View.setElevationResource(@DimenRes id: Int) {
    ViewCompat.setElevation(this, context.getDimensFloat(id))
}


fun View.hide(isGone: Boolean = false) {
    visibility = if (isGone) View.GONE else View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.createArcPath(offsetX: Float, offsetY: Float): Path {
    val startX = translationX
    val startY = translationY
    val dY = offsetY - translationY
    val pointX = if (dY < 0) offsetX else startX
    val pointY = if (dY < 0) startY else offsetY
    return Path().apply {
        moveTo(startX, startY)
        quadTo(pointX, pointY, offsetX, offsetY)
    }
}

fun View.getArcListener(path: Path): ValueAnimator.AnimatorUpdateListener {
    val point = FloatArray(2)
    val pathMeasure = PathMeasure(path, false)
    return ValueAnimator.AnimatorUpdateListener { animation ->
        val value = animation.animatedFraction
        pathMeasure.getPosTan(pathMeasure.length * value, point, null)
        translationX = point[0]
        translationY = point[1]
    }
}

fun TextView.setCompoundDrawablesRes(
    @DrawableRes left: Int? = null,
    @DrawableRes top: Int? = null,
    @DrawableRes right: Int? = null,
    @DrawableRes bottom: Int? = null
) {
    val leftRes = left?.let {
        this.context.getDrawable(it)?.apply {
            setBounds(0, 0, this.minimumWidth, this.minimumHeight)
        }
    }
    val topRes = top?.let {
        this.context.getDrawable(it)?.apply {
            setBounds(0, 0, this.minimumWidth, this.minimumHeight)
        }
    }
    val rightRes = right?.let {
        this.context.getDrawable(it)
            ?.apply { setBounds(0, 0, this.minimumWidth, this.minimumHeight) }
    }
    val bottomRes = bottom?.let {
        this.context.getDrawable(it)?.apply {
            setBounds(0, 0, this.minimumWidth, this.minimumHeight)
        }
    }
    setCompoundDrawables(leftRes, topRes, rightRes, bottomRes)
}


/******************************************************************************************************/
/**                             TextView                                                             **/
/******************************************************************************************************/

fun TextView.onTextChanged(callback: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit) =
    TvKeeper(this).onTextChanged(callback)

fun TextView.afterTextChanged(callback: (s: Editable?) -> Unit) =
    TvKeeper(this).afterTextChanged(callback)

fun TextView.beforeTextChanged(callback: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit) =
    TvKeeper(this).beforeTextChanged(callback)

class TvKeeper(private val tv: TextView) {
    private val executor = TextWatcherExecutor()

    init {
        tv.addTextChangedListener(executor)
    }

    fun afterTextChanged(callback: ((Editable?) -> Unit)) = apply {
        executor.after = callback
    }

    fun beforeTextChanged(callback: (CharSequence?, Int, Int, Int) -> Unit) = apply {
        executor.before = callback
    }

    fun onTextChanged(callback: (CharSequence?, Int, Int, Int) -> Unit) = apply {
        executor.textChange = callback
    }

    fun view() = tv
}

private class TextWatcherExecutor : TextWatcher {
    var after: ((Editable?) -> Unit)? = null
    var before: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    var textChange: ((CharSequence?, Int, Int, Int) -> Unit)? = null

    override fun afterTextChanged(s: Editable?) {
        after?.invoke(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        before?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textChange?.invoke(s, start, count, count)
    }
}

/******************************************************************************************************/
/**                             EditText                                                             **/
/******************************************************************************************************/


fun EditText.forbiddenSpace(): EditText {
    val filter = InputFilter { source, _, _, _, _, _ ->
        return@InputFilter if (source == " ") "" else null
    }
    val result = filters.copyOf(filters.size + 1)
    result[result.size - 1] = filter
    this.filters = result
    return this
}


fun EditText.forbiddenEnter(): EditText {
    val filter = InputFilter { source, _, _, _, _, _ ->
        return@InputFilter if (source.toString().contentEquals("\n")) "" else null
    }
    val result = filters.copyOf(filters.size + 1)
    result[result.size - 1] = filter
    this.filters = result
    return this
}

fun EditText.onlyMoney(): EditText {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            val temp: String = s.toString()
            val posDot = temp.indexOf(".")
            if (posDot <= 0) return
            if (temp.length - posDot - 1 > 2) {
                s.delete(posDot + 3, posDot + 4)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    })
    return this
}

fun EditText.addEmptySelectTrue(): EditText {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            this@addEmptySelectTrue.isSelected = s.toString()
                .isEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    })
    return this
}


/******************************************************************************************************/
/**                             CollapsingToolbarLayout                                              **/
/******************************************************************************************************/


fun CollapsingToolbarLayout.rmScrollFlag() {
    if (this.layoutParams !is AppBarLayout.LayoutParams) return
    val layoutParams = this.layoutParams as AppBarLayout.LayoutParams
    val scrollFlags = layoutParams.scrollFlags xor AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
    layoutParams.scrollFlags = scrollFlags
    this.layoutParams = layoutParams
}

fun CollapsingToolbarLayout.addScrollFlag() {
    if (this.layoutParams !is AppBarLayout.LayoutParams) return
    val layoutParams = this.layoutParams as AppBarLayout.LayoutParams
    val scrollFlags = layoutParams.scrollFlags or AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
    layoutParams.scrollFlags = scrollFlags
    this.layoutParams = layoutParams
}