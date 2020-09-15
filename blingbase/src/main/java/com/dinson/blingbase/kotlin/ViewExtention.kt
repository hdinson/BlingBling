package com.dinson.blingbase.kotlin

import android.animation.ValueAnimator
import android.graphics.Path
import android.graphics.PathMeasure
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import java.lang.Exception


/**
 *  View相关的扩展方法
 */
inline fun View.click(crossinline function: (view: View) -> Unit) =
    setOnClickListener { function(it) }


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
    @DrawableRes l: Int? = null,
    @DrawableRes t: Int? = null,
    @DrawableRes r: Int? = null,
    @DrawableRes b: Int? = null
) {
    val leftRes = l?.let {
        ContextCompat.getDrawable(this.context, it)?.apply {
            setBounds(0, 0, this.minimumWidth, this.minimumHeight)
        }
    }
    val topRes = t?.let {
        ContextCompat.getDrawable(this.context, it)?.apply {
            setBounds(0, 0, this.minimumWidth, this.minimumHeight)
        }
    }
    val rightRes = r?.let {
        ContextCompat.getDrawable(this.context, it)
            ?.apply { setBounds(0, 0, this.minimumWidth, this.minimumHeight) }
    }
    val bottomRes = b?.let {
        ContextCompat.getDrawable(this.context, it)?.apply {
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

fun EditText.forbiddenDoubleDot(): EditText {
    val filter = InputFilter { source, _, _, dest, _, _ ->
        return@InputFilter if (dest.contains('.') && source.toString() == ".") "" else null
    }
    val result = filters.copyOf(filters.size + 1)
    result[result.size - 1] = filter
    this.filters = result
    return this
}

fun EditText.setLimitNumber(before: Number, after: Number): EditText {
    val filter = InputFilter { source, _, _, dest, _, _ ->
        try {
            if (resources.toString() == "." && dest.toString().isEmpty()) {
                if (dest.isEmpty()) return@InputFilter "0."
                if (dest.contains('.')) return@InputFilter ""
            }
            val c = (dest.toString() + source.toString()).toFloat()
            val maxT = before max after
            val minT = before min after

            if (c > maxT.toFloat()) {
                this.setText(maxT.toString())
                this.setSelection(maxT.toString().length)
                return@InputFilter ""
            }
            if (c < minT.toFloat()) {
                this.setText(minT.toString())
                this.setSelection(minT.toString().length)
                return@InputFilter ""
            }
            return@InputFilter null
        } catch (e: Exception) {
            return@InputFilter ""
        }
    }
    this.filters = arrayOf(filter)
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

fun EditText.disableCopyAndPaste() {
    setOnLongClickListener(null)
    isLongClickable = false
    setTextIsSelectable(false)
    customSelectionActionModeCallback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?) = false

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?) = false

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false

        override fun onDestroyActionMode(mode: ActionMode?) {}
    }
}

/**
 * EditText 失去焦点并关闭软键盘
 */
fun EditText.lostFocus() {
    this.clearFocus()
    val parent = this.parent as View
    parent.isFocusable = true
    parent.isFocusableInTouchMode = true
    parent.requestFocus()
    this.context.closeKeyboard(this)
}

/**
 * EditText 监听键盘的回车键为 android:imeOptions="actionSearch"
 */
fun EditText.imeSearch(func: () -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_SEARCH
    setImeActionLabel("actionSearch", EditorInfo.IME_ACTION_SEARCH)
    this.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            lostFocus()
            func()
        }
        false
    }
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