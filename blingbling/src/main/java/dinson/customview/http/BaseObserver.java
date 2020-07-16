package dinson.customview.http;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static dinson.customview.kotlin.LogExtentionKt.loge;

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
        onFinal();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        loge(false, () -> "onError() called with: [" + e.toString() + "]");
        onFinal();
    }

    @Override
    public void onComplete() {
    }

    public abstract void onHandlerSuccess(@NotNull T value);

    /**
     * 不管网络是否请求成功或者失败都会回调
     */
    public void onFinal() {
    }
}
