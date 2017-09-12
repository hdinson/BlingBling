package dinson.customview.download.subscribers;


import java.lang.ref.SoftReference;

import dinson.customview.download.DownloadManager;
import dinson.customview.download.listener.DownloadProgressListener;
import dinson.customview.download.listener.HttpDownOnNextListener;
import dinson.customview.download.model.DownInfo;
import dinson.customview.download.model.DownloadState;
import dinson.customview.download.utils.DbDownUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 断点下载处理类Subscriber
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by WZG on 2016/7/16.
 */
public class ProgressDownSubscriber<T> implements DownloadProgressListener, Observer<T> {
    //弱引用结果回调
    private SoftReference<HttpDownOnNextListener> mSubscriberOnNextListener;
    /*下载数据*/
    private DownInfo downInfo;


    public ProgressDownSubscriber(DownInfo downInfo) {
        this.mSubscriberOnNextListener = new SoftReference<>(downInfo.getListener());
        this.downInfo = downInfo;
    }


    public void setDownInfo(DownInfo downInfo) {
        this.mSubscriberOnNextListener = new SoftReference<>(downInfo.getListener());
        this.downInfo = downInfo;
    }


    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onError(e);
        }
        DownloadManager.getInstance().remove(downInfo);
        downInfo.setState(DownloadState.ERROR);
        DbDownUtil.getInstance().update(downInfo);
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onComplete() {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onComplete();
        }
        DownloadManager.getInstance().remove(downInfo);
        downInfo.setState(DownloadState.FINISH);
        DbDownUtil.getInstance().update(downInfo);
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */

    private Disposable d;
    @Override
    public void onSubscribe(Disposable d) {
        this.d=d;


        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onStart();
        }
        downInfo.setState(DownloadState.START);
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext(t);
        }
    }

    @Override
    public void update(long read, long count, boolean done) {
        if (downInfo.getCountLength() > count) {
            read = downInfo.getCountLength() - count + read;
        } else {
            downInfo.setCountLength(count);
        }
        downInfo.setReadLength(read);
        if (mSubscriberOnNextListener.get() != null) {
            /*接受进度消息，造成UI阻塞，如果不需要显示进度可去掉实现逻辑，减少压力*/
            Observable.just(read).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*//*
                        if (downInfo.getState() == DownloadState.PAUSE || downInfo.getState() == DownloadState.STOP) return;
                        downInfo.setState(DownloadState.DOWN);
                        mSubscriberOnNextListener.get().updateProgress(aLong, downInfo.getCountLength());
                    }
                }/*new <Long>() {
                        @Override
                        public void call(Long aLong) {
                      *//*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*//*
                            if(downInfo.getState()==DownState.PAUSE||downInfo.getState()==DownState.STOP)return;
                            downInfo.setState(DownState.DOWN);
                            mSubscriberOnNextListener.get().updateProgress(aLong,downInfo.getCountLength());
                        }
                    }*/);
        }
    }

    public void unSubscribe() {

        d.dispose();
    }
}