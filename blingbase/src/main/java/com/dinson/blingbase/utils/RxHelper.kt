package com.dinson.blingbase.utils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


object RxHelper {
    /**
     * 倒计时
     * @param time 从第几秒开始倒计时
     */
    fun countdown(time: Int): Observable<Int> {
        var countTime = time
        if (countTime < 0) {
            countTime = 0
        }
        return Observable.interval(0, 1, TimeUnit.SECONDS)
            .map { countTime - it.toInt() }
            .take((countTime + 1).toLong())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 无限倒计时
     * @param time 从第几秒开始计时 为负数时，自增到0后正计时
     */
    fun timer(time: Int): Observable<Int> {
        return Observable.interval(0, 1, TimeUnit.SECONDS)
            .map { time - it.toInt() }
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
    }
}