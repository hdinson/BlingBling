package com.dinson.blingbase.network.template;

/**
 * 单例模板
 * Created by ywd on 2019/5/31.
 */

public abstract class SingletonTemplate<T> {
    private T mInstance;

    protected abstract T create();

    public final T get() {
        synchronized (this) {
            if (mInstance == null) {
                mInstance = create();
            }
            return mInstance;
        }
    }
}
