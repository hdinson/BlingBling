package dinson.customview.kotlin

import android.animation.ValueAnimator
import android.graphics.Path
import android.graphics.PathMeasure
import android.support.annotation.DimenRes
import android.support.v4.view.ViewCompat
import android.view.View

/**
 *  View相关的扩展方法
 */

fun View.click(function: () -> Unit) = setOnClickListener { function() }

fun View.halfWidth() = width / 2f

fun View.halfHeight() = height / 2f

infix fun View.setElevationResource(@DimenRes id: Int) {
    ViewCompat.setElevation(this, context.getDimensFloat(id))
}


fun View.hide(isGone: Boolean = false) {
    visibility = isGone then View.GONE ?: View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.createArcPath(offsetX: Float, offsetY: Float): Path {
    val startX = translationX
    val startY = translationY
    val dY = offsetY - translationY
    val pointX = (dY < 0) then offsetX ?: startX
    val pointY = (dY < 0) then startY ?: offsetY
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