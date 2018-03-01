package dinson.customview.kotlin

/**
 *   @author Dinson - 2018/3/1
 */
infix fun <T> Boolean.then(value: T?) = if (this) value else null

inline fun <T> Boolean.then(function: () -> T, default: () -> T) = if (this) function() else default()

infix inline fun <reified T> Boolean.then(function: () -> T) = if (this) function() else null