package com.dinson.blingbase.rxcache.stategy;



import com.dinson.blingbase.rxcache.RxCache;
import com.dinson.blingbase.rxcache.data.CacheResult;

import java.lang.reflect.Type;

import io.reactivex.Observable;


/**
 * author : zchu
 * date   : 2017/10/11
 * desc   :
 */
public interface IObservableStrategy {

    <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, Observable<T> source, Type type);
}
