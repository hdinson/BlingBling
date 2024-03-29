package com.dinson.blingbase.rxcache.stategy;

import com.dinson.blingbase.rxcache.RxCache;
import com.dinson.blingbase.rxcache.data.CacheResult;
import com.dinson.blingbase.rxcache.data.ResultFrom;

import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 仅加载网络，不缓存
 */
public final class NoneStrategy implements IStrategy  {

    private NoneStrategy() {
    }

    static final NoneStrategy INSTANCE = new NoneStrategy();

    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, final String key, Observable<T> source, Type type) {
        return source.map(new Function<T, CacheResult<T>>() {
            @Override
            public CacheResult<T> apply(@NonNull T t) throws Exception {
                return new CacheResult<>(ResultFrom.Remote, key, t);
            }
        });
    }

    @Override
    public <T> Publisher<CacheResult<T>> flow(RxCache rxCache, final String key, Flowable<T> source, Type type) {
        return source.map(new Function<T, CacheResult<T>>() {
            @Override
            public CacheResult<T> apply(@NonNull T t) throws Exception {
                return new CacheResult<>(ResultFrom.Remote, key, t);
            }
        });
    }
}
