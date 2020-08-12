package com.dinson.blingbase.rxcache.stategy;

import com.dinson.blingbase.rxcache.RxCache;
import com.dinson.blingbase.rxcache.data.CacheResult;

import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

import io.reactivex.Flowable;

public interface IFlowableStrategy {

    <T> Publisher<CacheResult<T>> flow(RxCache rxCache, String key, Flowable<T> source, Type type);
}
