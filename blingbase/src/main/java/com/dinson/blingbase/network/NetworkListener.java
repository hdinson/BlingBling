package com.dinson.blingbase.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;

import com.dinson.blingbase.network.netcallback.NetworkCallbackImpl;
import com.dinson.blingbase.network.template.SingletonTemplate;


/**
 * 网络监听
 * Created by ywd on 2019/6/5.
 */

public class NetworkListener {
    private Context context;
    private NetworkCallbackImpl networkCallback;

    /**
     * 私有化构造方法
     */
    private NetworkListener() {
    }

    private static final SingletonTemplate<NetworkListener> INSTANCE = new SingletonTemplate<NetworkListener>() {
        @Override
        protected NetworkListener create() {
            return new NetworkListener();
        }
    };

    public static NetworkListener getInstance() {
        return INSTANCE.get();
    }

    public Context getContext() {
        return context;
    }

    /**
     * 初始化
     *
     * @param context context
     */
    @SuppressLint("MissingPermission")
    public void init(Context context) {
        this.context = context;

        networkCallback = new NetworkCallbackImpl();
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        NetworkRequest request = builder.build();
        ConnectivityManager connMgr = (ConnectivityManager) NetworkListener.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            connMgr.registerNetworkCallback(request, networkCallback);
        }
    }

    /**
     * 注册
     *
     * @param observer 观察者(Activity/Fragment)
     */
    public void registerObserver(Object observer) {
        networkCallback.registerObserver(observer);
    }

    /**
     * 解除注册
     *
     * @param observer 观察者(Activity/Fragment)
     */
    public void unRegisterObserver(Object observer) {
        networkCallback.unRegisterObserver(observer);
    }

}
