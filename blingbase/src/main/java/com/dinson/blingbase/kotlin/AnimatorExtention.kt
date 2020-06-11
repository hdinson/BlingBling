package com.dinson.blingbase.kotlin

import android.animation.Animator
import android.animation.AnimatorSet

/**
 *  Animator扩展方法
 */
@Suppress("unused")
fun AnimatorSet.playWith(vararg items: Animator) = apply {
    playTogether(items.toMutableList())
}

@Suppress("unused")
fun Animator.playWith(animator: Animator): Animator {
    if (animator is AnimatorSet)
        return animator.playWith(this)
    else if (this is AnimatorSet)
        return playWith(animator)
    return AnimatorSet().playWith(this, animator)
}

@Suppress("unused")
fun Animator.onEnd(callback: (Animator?) -> Unit) = Keeper(this).onEnd(callback)

@Suppress("unused")
fun Animator.onStart(callback: (Animator?) -> Unit) = Keeper(this).onStart(callback)

@Suppress("unused")
fun Animator.onCancel(callback: (Animator?) -> Unit) = Keeper(this).onCancel(callback)

@Suppress("unused")
fun Animator.onRepeat(callback: (Animator?) -> Unit) = Keeper(this).onRepeat(callback)

@Suppress("unused")
class Keeper(private val animator: Animator) {
    private val executor = ListenerExecutor()

    init {
        animator.addListener(executor)
    }

    fun onEnd(callback: (Animator?) -> Unit) = apply {
        executor.end = callback
    }

    fun onStart(callback: (Animator?) -> Unit) = apply {
        executor.start = callback
    }

    fun onCancel(callback: (Animator?) -> Unit) = apply {
        executor.cancel = callback
    }

    fun onRepeat(callback: (Animator?) -> Unit) = apply {
        executor.repeat = callback
    }

    fun animator() = animator
}


private class ListenerExecutor : Animator.AnimatorListener {
    var end: ((Animator?) -> Unit)? = null
    var start: ((Animator?) -> Unit)? = null
    var cancel: ((Animator?) -> Unit)? = null
    var repeat: ((Animator?) -> Unit)? = null

    override fun onAnimationCancel(animation: Animator?) {
        cancel?.invoke(animation)
    }

    override fun onAnimationEnd(animation: Animator?) {
        end?.invoke(animation)
    }

    override fun onAnimationRepeat(animation: Animator?) {
        repeat?.invoke(animation)
    }

    override fun onAnimationStart(animation: Animator?) {
        start?.invoke(animation)
    }
}