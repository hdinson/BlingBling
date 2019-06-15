package dinson.customview.download;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import dinson.customview.api.DownloadServiceApi;
import dinson.customview.download.model.DownloadInfo;
import dinson.customview.download.model.DownloadState;
import dinson.customview.download.subscribers.ProgressDownSubscriber;
import dinson.customview.download.utils.DbDownUtil;
import dinson.customview.http.manager.DownloadInterceptor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * http下载处理类
 */
public class DownloadManager {
    /*记录下载数据*/
    private Set<DownloadInfo> downInfos;
    /*回调sub队列*/
    private HashMap<String, ProgressDownSubscriber> subMap;
    /*单利对象*/
    private volatile static DownloadManager INSTANCE;
    /*数据库类*/
    private DbDownUtil db;

    private DownloadManager() {
        downInfos = new HashSet<>();
        subMap = new HashMap<>();
        db = DbDownUtil.getInstance();
    }

    /**
     * 获取单例
     */
    public static DownloadManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DownloadManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DownloadManager();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 开始下载
     */
    public void startDown(final DownloadInfo info) {
        /*正在下载不处理*/
        if (info == null || subMap.get(info.getUrl()) != null) {
            subMap.get(info.getUrl()).setDownInfo(info);
            return;
        }
        /*添加回调处理类*/
        ProgressDownSubscriber subscriber = new ProgressDownSubscriber(info);
        /*记录回调sub*/
        subMap.put(info.getUrl(), subscriber);
        /*获取service，多次请求公用一个sercie*/
        DownloadServiceApi httpService;
        if (downInfos.contains(info)) {
            httpService = info.getService();
        } else {
            DownloadInterceptor interceptor = new DownloadInterceptor(subscriber);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //手动创建一个OkHttpClient并设置超时时间
            builder.connectTimeout(info.getConnectonTime(), TimeUnit.SECONDS);
            builder.addInterceptor(interceptor);

            Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://192.168.8.8")
                .build();
            httpService = retrofit.create(DownloadServiceApi.class);
            info.setService(httpService);
            downInfos.add(info);
        }
        /*得到rx对象-上一次下載的位置開始下載*/
        httpService.download("bytes=" + info.getReadLength() + "-", info.getUrl())
                /*指定线程*/
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
                   /*失败后的retry配置*/
            //.retryWhen(new RetryWhenNetworkException())
                /*读取下载写入文件*/
            .map(responseBody -> {
                writeCaches(responseBody, new File(info.getSavePath()), info);
                return info;
            })
                /*回调线程*/
            .observeOn(AndroidSchedulers.mainThread())
                /*数据回调*/
            .subscribe(subscriber);

    }


    /**
     * 停止下载
     */
    public void stopDown(DownloadInfo info) {
        if (info == null) return;
        info.setState(DownloadState.STOP);
        info.getListener().onStop();
        if (subMap.containsKey(info.getUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getUrl());
            subscriber.unSubscribe();
            subMap.remove(info.getUrl());
        }
        /*保存数据库信息和本地文件*/
        db.insertOrReplace(info);
    }


    /**
     * 暂停下载
     *
     * @param info
     */
    public void pause(DownloadInfo info) {
        if (info == null) return;
        info.setState(DownloadState.PAUSE);
        info.getListener().onPause();
        if (subMap.containsKey(info.getUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getUrl());
            subscriber.unSubscribe();
            subMap.remove(info.getUrl());
        }
        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
        db.insertOrReplace(info);
    }

    /**
     * 停止全部下载
     */
    public void stopAllDown() {
        for (DownloadInfo downInfo : downInfos) {
            stopDown(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }

    /**
     * 暂停全部下载
     */
    public void pauseAll() {
        for (DownloadInfo downInfo : downInfos) {
            pause(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }


    /**
     * 返回全部正在下载的数据
     *
     * @return
     */
    public Set<DownloadInfo> getDownInfos() {
        return downInfos;
    }

    /**
     * 移除下载数据
     *
     * @param info
     */
    public void remove( DownloadInfo info) {
        subMap.remove(info.getUrl());
        downInfos.remove(info);
    }


    /**
     * 写入文件
     *
     * @param file
     * @param info
     * @throws IOException
     */
    public void writeCaches(ResponseBody responseBody, File file,  DownloadInfo info) {
        try {
            RandomAccessFile randomAccessFile = null;
            FileChannel channelOut = null;
            InputStream inputStream = null;
            try {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                long allLength = 0 == info.getCountLength() ? responseBody.contentLength() : info.getReadLength() +
                    responseBody
                        .contentLength();

                inputStream = responseBody.byteStream();
                randomAccessFile = new RandomAccessFile(file, "rwd");
                channelOut = randomAccessFile.getChannel();
                MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                    info.getReadLength(), allLength - info.getReadLength());
                byte[] buffer = new byte[1024 * 4];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    mappedBuffer.put(buffer, 0, len);
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
