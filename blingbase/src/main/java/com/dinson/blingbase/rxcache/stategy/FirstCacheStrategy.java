package com.dinson.blingbase.rxcache.stategy;


import com.dinson.blingbase.rxcache.CacheTarget;
import com.dinson.blingbase.rxcache.RxCache;
import com.dinson.blingbase.rxcache.RxCacheHelper;
import com.dinson.blingbase.rxcache.data.CacheResult;

import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * 优先缓存
 */
public final class FirstCacheStrategy implements IStrategy  {
    private boolean isSync;

    public FirstCacheStrategy() {
        isSync = false;
    }

    public FirstCacheStrategy(boolean isSync) {
        this.isSync = isSync;
    }

    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, Observable<T> source, Type type) {
        Observable<CacheResult<T>> cache = RxCacheHelper.loadCache(rxCache, key, type,true);
        Observable<CacheResult<T>> remote;
        if (isSync) {
            remote =  RxCacheHelper.loadRemoteSync(rxCache, key, source, CacheTarget.MemoryAndDisk,false);
        } else {
            remote =  RxCacheHelper.loadRemote(rxCache, key, source, CacheTarget.MemoryAndDisk,false);
        }
        return cache.switchIfEmpty(remote);
    }

    @Override
    public <T> Publisher<CacheResult<T>> flow(RxCache rxCache, String key, Flowable<T> source, Type type) {
        Flowable<CacheResult<T>> cache = RxCacheHelper.loadCacheFlowable(rxCache, key, type,true);
        Flowable<CacheResult<T>> remote;
        if (isSync) {
            remote =RxCacheHelper.loadRemoteSyncFlowable(rxCache, key, source, CacheTarget.MemoryAndDisk,false);
        } else {
            remote =RxCacheHelper.loadRemoteFlowable(rxCache, key, source, CacheTarget.MemoryAndDisk,false);
        }
        return cache.switchIfEmpty(remote);
    }
}
