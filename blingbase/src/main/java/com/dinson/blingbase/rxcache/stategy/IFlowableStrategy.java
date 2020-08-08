package com.dinson.blingbase.rxcache.stategy;



import com.dinson.blingbase.rxcache.RxCache;
import com.dinson.blingbase.rxcache.data.CacheResult;

import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

import io.reactivex.Flowable;


/**
 * author : zchu
 * date   : 2017/10/11
 * desc   :
 */
public interface IFlowableStrategy {

    <T> Publisher<CacheResult<T>> flow(RxCache rxCache, String key, Flowable<T> source, Type type);
}
