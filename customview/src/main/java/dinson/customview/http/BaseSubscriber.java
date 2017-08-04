package dinson.customview.http;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import dinson.customview.utils.LogUtils;

/**
 * @author Dinson - 2017/8/4
 */
public abstract class BaseSubscriber<T> implements Subscriber<T> {

    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onNext(T t) {
        onHandlerSuccess(t);
    }

    @Override
    public void onError(Throwable t) {
        LogUtils.i("onError() called with: [" + t.toString() + "]");
    }

    @Override
    public void onComplete() {
        LogUtils.i("onComplete");
    }

    public abstract void onHandlerSuccess(T t);
}
