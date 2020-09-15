package com.dinson.blingbase.widget.recycleview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.annotation.StringDef
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * 线性布局分割线
 */
class LinearItemDecoration(private val b: Builder) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(o: Rect, view: View, rv: RecyclerView, state: RecyclerView.State) {
        val poi = rv.getChildAdapterPosition(view)
        val c = rv.adapter?.itemCount ?: 0
        val manager = rv.layoutManager
        if (manager !is LinearLayoutManager) {
            throw  IllegalArgumentException("LinearItemDecoration only used on LinearLayout.");
        }

        if (manager.orientation == LinearLayoutManager.VERTICAL) {
            val childType: Int = rv.layoutManager?.getItemViewType(view) ?: 0
            val dLeft = b.getMapLeft()[childType]
            val dTop = b.getMapTop()[childType]
            val dRight = b.getMapRight()[childType]
            val dBottom = b.getMapBottom()[childType]

            //垂直方向的RecycleView，上下都有图片的情况下，不显示上边的图片
            when {
                dBottom != null -> {
                    o.top = getStartDrawableSize(true, poi)
                    val tEnd = getEndDrawableSize(true, poi, c)
                    o.bottom = if (dBottom.second == 0) dBottom.first.intrinsicHeight + tEnd
                    else dBottom.second + tEnd
                }
                dTop != null -> {
                    o.bottom = getEndDrawableSize(true, poi, c)
                    val tStart = getStartDrawableSize(true, poi)
                    o.top = if (dTop.second == 0) dTop.first.intrinsicHeight + tStart
                    else dTop.second + tStart
                }
                else -> {
                    o.top = getStartDrawableSize(true, poi)
                    o.bottom = getEndDrawableSize(true, poi, c)
                }
            }
            if (dLeft != null) {
                o.left = if (dLeft.second == 0) dLeft.first.intrinsicWidth else dLeft.second
            }
            if (dRight != null) {
                o.right =
                    if (dRight.second == 0) dRight.first.intrinsicWidth else dRight.second
            }
        } else {
            val childType: Int = rv.layoutManager?.getItemViewType(view) ?: 0
            val dLeft = b.getMapLeft()[childType]
            val dTop = b.getMapTop()[childType]
            val dRight = b.getMapRight()[childType]
            val dBottom = b.getMapBottom()[childType]

            //水平方向的RecycleView，左右都有图片的情况下，不显示左边的图片
            when {
                dRight != null -> {
                    o.left = getStartDrawableSize(false, poi)
                    val tEnd = getEndDrawableSize(false, poi, c)
                    o.right = if (dRight.second == 0) dRight.first.intrinsicWidth + tEnd
                    else dRight.second + tEnd
                }
                dLeft != null -> {
                    o.right = getEndDrawableSize(false, poi, c)
                    val tStart = getStartDrawableSize(false, poi)
                    o.left = if (dLeft.second == 0) dLeft.first.intrinsicWidth + tStart
                    else dLeft.second + tStart
                }
                else -> {
                    o.left = getStartDrawableSize(false, poi)
                    o.right = getEndDrawableSize(false, poi, c)
                }
            }
            if (dTop != null) {
                o.top = if (dTop.second == 0) dTop.first.intrinsicHeight else dTop.second
            }
            if (dBottom != null) {
                o.bottom =
                    if (dBottom.second == 0) dBottom.first.intrinsicHeight else dBottom.second
            }
        }
    }


    private fun getStartDrawableSize(isV: Boolean, poi: Int): Int {
        return when {
            poi != 0 -> 0
            b.getStartDrawable() == null -> 0
            b.getStartDrawable()!!.second != 0 -> b.getStartDrawable()!!.second
            else -> {
                val first = b.getStartDrawable()!!.first
                if (isV) first.intrinsicHeight else first.intrinsicWidth
            }
        }
    }

    private fun getEndDrawableSize(isV: Boolean, poi: Int, c: Int): Int {
        return when {
            poi != c - 1 -> 0
            b.getEndDrawable() == null -> 0
            b.getEndDrawable()!!.second != 0 -> b.getEndDrawable()!!.second
            else -> {
                val first = b.getEndDrawable()!!.first
                if (isV) first.intrinsicHeight else first.intrinsicWidth
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val manager = parent.layoutManager
        if (manager !is LinearLayoutManager) return
        if (manager.orientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }


    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val manager = parent.layoutManager ?: return
        val leftO: Int = parent.paddingLeft
        val rightO: Int = parent.width - parent.paddingRight

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val childViewType = manager.getItemViewType(child)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val poi = parent.getChildAdapterPosition(child)
            val itemCount = parent.adapter?.itemCount ?: 0

            val tStart = getStartDrawableSize(true, poi)
            val sLeft = manager.getLeftDecorationWidth(child)
            val sTop = manager.getTopDecorationHeight(child) - tStart
            val sRight = manager.getRightDecorationWidth(child)
            val tEnd = getEndDrawableSize(true, poi, itemCount)
            val sBottom = manager.getBottomDecorationHeight(child) - tEnd

            val bottomD = b.getMapBottom()[childViewType]
            if (bottomD != null) {
                val tTop = child.bottom + params.bottomMargin
                val tBottom = tTop + sBottom
                bottomD.first.setBounds(child.left, tTop, child.right, tBottom)
                bottomD.first.draw(c)
            }
            val topD = b.getMapTop()[childViewType]
            if (topD != null) {
                val tBottom = child.top - params.topMargin
                val tTop = tBottom - sTop
                topD.first.setBounds(child.left, tTop, child.right, tBottom)
                topD.first.draw(c)
            }
            val rightD = b.getMapRight()[childViewType]
            if (rightD != null) {
                val tLeft = child.right + params.rightMargin
                val tRight = tLeft + sRight
                rightD.first.setBounds(
                    tLeft, child.top - sTop - params.topMargin,
                    tRight, child.bottom + sBottom + params.bottomMargin
                )
                rightD.first.draw(c)
            }
            val leftD = b.getMapLeft()[childViewType]
            if (leftD != null) {
                val tRight = child.left - params.leftMargin
                val tLeft = tRight - sLeft
                leftD.first.setBounds(
                    tLeft, child.top - sTop - params.topMargin,
                    tRight, child.bottom + sBottom + params.bottomMargin
                )
                leftD.first.draw(c)
            }

            if (poi == 0 && b.getStartDrawable() != null) {
                val first = b.getStartDrawable()!!.first
                val tBottom = child.top - params.topMargin - sTop
                val tTop = tBottom - tStart
                first.setBounds(leftO, tTop, rightO, tBottom)
                first.draw(c)
            }
            if (poi == itemCount - 1 && b.getEndDrawable() != null) {
                val first = b.getEndDrawable()!!.first
                val tTop = child.bottom + params.bottomMargin + sBottom
                val tBottom = tTop + tEnd
                first.setBounds(leftO, tTop, rightO, tBottom)
                first.draw(c)
            }
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val manager = parent.layoutManager ?: return

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val childViewType = manager.getItemViewType(child)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val poi = parent.getChildAdapterPosition(child)
            val itemCount = parent.adapter?.itemCount ?: 0


            val tStart = getStartDrawableSize(false, poi)
            val sLeft = manager.getLeftDecorationWidth(child) - tStart
            val sTop = manager.getTopDecorationHeight(child)
            val tEnd = getEndDrawableSize(false, poi, itemCount)
            val sRight = manager.getRightDecorationWidth(child) - tEnd
            val sBottom = manager.getBottomDecorationHeight(child)


            val rightD = b.getMapRight()[childViewType]
            if (rightD != null) {
                val tLeft = child.right + params.rightMargin
                val tRight = tLeft + sRight
                rightD.first.setBounds(tLeft, child.top, tRight, child.bottom)
                rightD.first.draw(c)
            }
            val leftD = b.getMapLeft()[childViewType]
            if (leftD != null) {
                val tRight = child.left - params.leftMargin
                val tLeft = tRight - sLeft
                leftD.first.setBounds(tLeft, child.top, tRight, child.bottom)
                leftD.first.draw(c)
            }
            val topD = b.getMapTop()[childViewType]
            if (topD != null) {
                val tBottom = child.top - params.topMargin
                val tTop = tBottom - sTop
                topD.first.setBounds(
                    child.left - sLeft - params.leftMargin, tTop,
                    child.right + sRight + params.rightMargin, tBottom
                )
                topD.first.draw(c)
            }
            val bottomD = b.getMapBottom()[childViewType]
            if (bottomD != null) {
                val tTop = child.bottom + params.bottomMargin
                val tBottom = tTop + sBottom
                bottomD.first.setBounds(
                    child.left - sLeft - params.leftMargin, tTop,
                    child.right + sRight + params.rightMargin, tBottom
                )
                bottomD.first.draw(c)
            }

            if (poi == 0 && b.getStartDrawable() != null) {
                val first = b.getStartDrawable()!!.first
                val tRight = child.left - params.leftMargin - sLeft
                val tLeft = tRight - tStart
                first.setBounds(tLeft, child.top - sTop, tRight, child.bottom + sBottom)
                first.draw(c)
            }
            if (poi == itemCount - 1 && b.getEndDrawable() != null) {
                val first = b.getEndDrawable()!!.first
                val tLeft = child.right + params.rightMargin + sRight
                val tRight = tLeft + tEnd
                first.setBounds(tLeft, child.top - sTop, tRight, child.bottom + sBottom)
                first.draw(c)
            }
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
    @StringDef(Direction.TOP, Direction.LEFT, Direction.RIGHT, Direction.BOTTOM)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Direction {
        companion object {
            const val LEFT = "left"
            const val TOP = "top"
            const val RIGHT = "right"
            const val BOTTOM = "bottom"
        }
    }

    class Builder(val context: Context) {

        private val mDividerViewTypeMapTop = HashMap<Int, Pair<Drawable, Int>>()
        private val mDividerViewTypeMapRight = HashMap<Int, Pair<Drawable, Int>>()
        private val mDividerViewTypeMapLeft = HashMap<Int, Pair<Drawable, Int>>()
        private val mDividerViewTypeMapBottom = HashMap<Int, Pair<Drawable, Int>>()
        private var mStartDrawable: Pair<Drawable, Int>? = null
        private var mEndDrawable: Pair<Drawable, Int>? = null

        internal fun getStartDrawable() = mStartDrawable
        internal fun getEndDrawable() = mEndDrawable
        internal fun getMapTop() = mDividerViewTypeMapTop
        internal fun getMapRight() = mDividerViewTypeMapRight
        internal fun getMapLeft() = mDividerViewTypeMapLeft
        internal fun getMapBottom() = mDividerViewTypeMapBottom


        fun start(@DrawableRes id: Int, spacePx: Int = 0): Builder {
            ContextCompat.getDrawable(context, id)?.apply {
                mStartDrawable = Pair(this, spacePx)
            }
            return this
        }

        fun start(drawable: Drawable, spacePx: Int = 0) {
            mStartDrawable = Pair(drawable, spacePx)
        }

        fun end(@DrawableRes id: Int, spacePx: Int = 0): Builder {
            ContextCompat.getDrawable(context, id)?.apply {
                mEndDrawable = Pair(this, spacePx)
            }
            return this
        }

        fun end(drawable: Drawable, spacePx: Int = 0) {
            mEndDrawable = Pair(drawable, spacePx)
        }


        fun type(
            @Direction direction: String,
            @DrawableRes id: Int,
            spacePx: Int = 0, viewType: Int = 0
        ): Builder {
            ContextCompat.getDrawable(context, id)?.apply {
                putToMap(viewType, direction, this, spacePx)
            }
            return this
        }


        fun type(
            @Direction direction: String,
            drawable: Drawable,
            spacePx: Int = 0, viewType: Int = 0
        ): Builder {
            putToMap(viewType, direction, drawable, spacePx)
            return this
        }

        private fun putToMap(vt: Int, @Direction dr: String, drawable: Drawable, px: Int) {
            when (dr) {
                Direction.LEFT -> mDividerViewTypeMapLeft[vt] = Pair(drawable, px)
                Direction.TOP -> mDividerViewTypeMapTop[vt] = Pair(drawable, px)
                Direction.RIGHT -> mDividerViewTypeMapRight[vt] = Pair(drawable, px)
                Direction.BOTTOM -> mDividerViewTypeMapBottom[vt] = Pair(drawable, px)
            }
        }

        fun build(): LinearItemDecoration {
            return LinearItemDecoration(this)
        }
    }
}