package dinson.customview.http;

import dinson.customview.utils.LogUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Dinson - 2017/7/25
 */
public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T value) {
        onHandlerSuccess(value);
    }

    @Override
    public void onError(Throwable e) {
        LogUtils.i("onError() called with: [" + e.toString() + "]");
    }

    @Override
    public void onComplete() {
        LogUtils.i("onComplete");
    }

    public abstract void onHandlerSuccess(T value);
}
