package com.dinson.blingbase.widget.recycleview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import java.lang.RuntimeException


/**
 * 线性布局分割线
 */
@Suppress("unused")
open class LinearSpaceDecoration(private val b: Builder) : ItemDecoration() {

    override fun getItemOffsets(o: Rect, view: View, parent: RecyclerView, state: State) {
        val manager = parent.layoutManager
        if (manager==null||manager !is LinearLayoutManager){
            throw RuntimeException("LayoutManager must be LinearLayoutManager")
        }
        val orientation = manager.orientation
        val poi = parent.getChildAdapterPosition(view)
        val c = parent.adapter?.itemCount ?: 0

        if (orientation == VERTICAL) {
            o.top = if (poi == 0 && b.sameSpace()) b.t + b.b + b.start else b.t + b.start
            o.bottom = if (poi == c - 1 && b.sameSpace()) b.t + b.b + b.end else b.b + b.end
            o.left = if (b.sameSpace()) b.l + b.r else b.l
            o.right = if (b.sameSpace()) b.l + b.r else b.r
        } else {
            o.left = if (poi == 0 && b.sameSpace()) b.l + b.r + b.start else b.l + b.start
            o.right = if (poi == c - 1 && b.sameSpace()) b.l + b.r + b.end else b.r + b.end
            o.top = if (b.sameSpace()) b.t + b.b else b.t
            o.bottom = if (b.sameSpace()) b.t + b.b else b.b
        }
    }

    open class Builder {

        internal var t = 0
        internal var l = 0
        internal var r = 0
        internal var b = 0
        internal var start = 0
        internal var end = 0

        private var isSameSpace = true
        internal fun sameSpace() = isSameSpace

        fun space(px: Int): Builder {
            t = px / 2
            b = px / 2
            r = px / 2
            l = px / 2
            isSameSpace = true
            return this
        }

        fun spaceTB(px: Int): Builder {
            t = px / 2
            b = px / 2
            isSameSpace = true
            return this
        }

        fun spaceRL(px: Int): Builder {
            r = px / 2
            l = px / 2
            isSameSpace = true
            return this
        }

        fun space(lPx: Int, tPx: Int, rPx: Int, bPx: Int): Builder {
            t = tPx
            r = rPx
            l = lPx
            b = bPx
            isSameSpace = false
            return this
        }

        fun startSpace(sPx: Int): Builder {
            start = sPx
            return this
        }

        fun endSpace(ePx: Int): Builder {
            end = ePx
            return this
        }

        open fun build(): LinearSpaceDecoration {
            return LinearSpaceDecoration(this)
        }
    }
}