package com.dinson.blingbase.rxcache.stategy;

import com.dinson.blingbase.rxcache.RxCache;
import com.dinson.blingbase.rxcache.RxCacheHelper;
import com.dinson.blingbase.rxcache.data.CacheResult;

import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * 仅加载缓存
 */
public final class OnlyCacheStrategy implements IStrategy  {

    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, Observable<T> source, Type type) {
        return RxCacheHelper.loadCache(rxCache, key, type,false);
    }

    @Override
    public <T> Publisher<CacheResult<T>> flow(RxCache rxCache, String key, Flowable<T> source, Type type) {
        return RxCacheHelper.loadCacheFlowable(rxCache, key, type,false);
    }
}
