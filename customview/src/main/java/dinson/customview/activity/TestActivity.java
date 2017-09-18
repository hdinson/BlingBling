package dinson.customview.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.api.OneApi;
import dinson.customview.api.XinZhiWeatherApi;
import dinson.customview.api.wifi;
import dinson.customview.download.DownloadManager;
import dinson.customview.download.listener.HttpDownOnNextListener;
import dinson.customview.download.model.DownloadInfo;
import dinson.customview.download.model.DownloadState;
import dinson.customview.http.BaseObserver;
import dinson.customview.http.HttpHelper;
import dinson.customview.utils.UIUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class TestActivity extends BaseActivity {

    private TextView mTvDesc;
    //private static final String URL = "http://shouji.baidu.com/";
    private static final String URL = "file:///android_asset/test.html";
    private WebView webView;
    public String tag = "MainActivity";
    private Context mContext;
    private DownloadManager manager;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);
        // 进行全屏

        mTvDesc = (TextView) findViewById(R.id.tv_dis);
        manager = DownloadManager.getInstance();

    }


    public void doGetNetTime(View view) {

       /* Observable.just("http://www.baidu.com")
            .map(s -> {
                URL url = new URL("http://www.baidu.com");
                URLConnection uc = url.openConnection();//生成连接对象
                uc.connect(); //发出连接
                return uc.getDate();//取得网站日期时间
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(aLong -> mTvDesc.setText(DateUtils.long2Str(aLong) + " - " + aLong));*/


        HttpHelper.create(XinZhiWeatherApi.class).getWeather("24.879364:118.643059")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<JsonObject>() {
                @Override
                public void onHandlerSuccess(JsonObject value) {
                    mTvDesc.setText(value.toString());
                }
            });
    }


    public void doDown(View view) {


        String Url = "http://ondlsj2sn.bkt.clouddn.com/FnRKaCfPjrnDwWQWkqF8MFJDCTca.jpg";

        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "YVsZJj88m.jpg");
        DownloadInfo apkApi = new DownloadInfo(Url);
        apkApi.setState(DownloadState.START);
        apkApi.setSavePath(outputFile.getAbsolutePath());


        /*下载回调*/
        HttpDownOnNextListener<DownloadInfo> httpProgressOnNextListener = new HttpDownOnNextListener<DownloadInfo>() {
            @Override
            public void onNext(DownloadInfo baseDownEntity) {
                mTvDesc.append("提示：下载完成\n");
            }

            @Override
            public void onStart() {
                mTvDesc.append("提示:开始下载\n");
            }

            @Override
            public void onComplete() {
                mTvDesc.append("提示：下载结束\n");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mTvDesc.append("失败:" + e.toString());
            }


            @Override
            public void onPuase() {
                super.onPuase();
                mTvDesc.append("提示:暂停\n");
            }

            @Override
            public void onStop() {
                super.onStop();
            }

            @Override
            public void updateProgress(long readLength, long countLength) {
                mTvDesc.append("提示:下载中\n");
                mTvDesc.append("文件大小：" + countLength + "\n");
                mTvDesc.append("写入大小：" + readLength + "\n");
            }
        };


        apkApi.setListener(httpProgressOnNextListener);
        manager.startDown(apkApi);


        // DownManager.getInstance().download(new _009PanoramaImageModel("111",));


        /*DownLoadEntity entity = new DownLoadEntity("http://ondlsj2sn.bkt.clouddn.com/FqCreoHlRPVcwdNuyFSrLwYuF9wI
        .jpg",
            ConstantsUtils.SDCARD_ROOT + "test1.jpg");

        DownLoadManager.getInstance().downLoad(entity, "dflasd", new DownLoadBackListener() {
            @Override
            public void onStart(double percent) {
                mTvDesc.append("开始下载\n");
            }

            @Override
            public void onCancel() {
                mTvDesc.append("下载取消\n");

            }

            @Override
            public void onDownLoading(double percent) {
                mTvDesc.append("正在下载中...\n");

            }

            @Override
            public void onCompleted() {
                mTvDesc.append("开始完成\n");

            }

            @Override
            public void onError(DownLoadEntity downLoadEntity, Throwable throwable) {
                mTvDesc.append("开始错误\n");
            }
        });*/


    }

    ///////////////////////////////////////以下看情况删////////////////////////////////////////////////////////
    public void doPost(View view) {
        //  qiniuyun();
        // qzBus();
        // jsoupTest();
        //AlarmTest();
        Observable.just("")
            .map(new Function<String, String>() {

                @Override
                public String apply(String s) throws Exception {
                    SystemClock.sleep(200000);
                    return "";
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe();
    }


    private void jsoupTest() {
        Observable.just("http://www.dinson.win/")
            .map(s -> {
                Document document = Jsoup.connect(s).get();
                return document;
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(document -> {
                mTvDesc.setText("");
                mTvDesc.append(document.title());
                mTvDesc.append(document.body().toString());
            });
    }


    private void qiniuyun() {
        String url = "dinson-blog:mh_ic_323.png"; // 编码前
        String url_encode = Base64.encodeToString(url.getBytes(), Base64.DEFAULT).trim();//  编码后

        String token = "QBox " + getToken("/delete/" + url_encode + "\n");
        mTvDesc.append("url_encode : " + url_encode + "\ntoken : " + token);

        OneApi oneApi = HttpHelper.create(OneApi.class);
        Observable<String> observable = oneApi.postDelete(url_encode, token.trim());
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<String>() {
                @Override
                public void onHandlerSuccess(String value) {
                    mTvDesc.append(value);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    mTvDesc.append(e.toString() + "\n");
                }
            });
    }


    public String getToken(String url) {
/*# 假设有如下的管理请求：
AccessKey = "Pxox-Sf-S5bz21TEX6gB6d9x7H05hZLkJtTMJI3y"
SecretKey = "CGBwKTzZTcaGcD0NZFocIrhKG5uJNL1yqEwYXdvN"
url = "http://ondlsj2sn.bkt.clouddn.com/FpUVRI5ACAX7YkTOXLuD7mP_3BPg.webp"
#则待签名的原始字符串是：
signingStr = "/move/bmV3ZG9jczpmaW5kX21hbi50eHQ=/bmV3ZG9jczpmaW5kLm1hbi50eHQ=\n"
#签名字符串是：
sign = "157b18874c0a1d83c4b0802074f0fd39f8e47843"
注意：签名结果是二进制数据，此处输出的是每个字节的十六进制表示，以便核对检查。
#编码后的签名字符串是：
encodedSign = "FXsYh0wKHYPEsIAgdPD9OfjkeEM="
#最终的管理凭证是：
accessToken = "MY_ACCESS_KEY:FXsYh0wKHYPEsIAgdPD9OfjkeEM="*/


        String AccessKey = "Pxox-Sf-S5bz21TEX6gB6d9x7H05hZLkJtTMJI3y";
        String SecretKey = "CGBwKTzZTcaGcD0NZFocIrhKG5uJNL1yqEwYXdvN";

        String sha1 = hmac_sha1(url, SecretKey);
        mTvDesc.append("sha1 : " + sha1 + "\n");

        String base64 = Base64.encodeToString(sha1.getBytes(), Base64.DEFAULT);
        mTvDesc.append("base64 : " + base64 + "\n");

        return AccessKey + ":" + base64;


    }

    private String hmac_sha1(String key, String datas) {
        String reString = "";

        try {
            byte[] data = key.getBytes("UTF-8");
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            //用给定密钥初始化 Mac 对象
            mac.init(secretKey);

            byte[] text = datas.getBytes("UTF-8");
            //完成 Mac 操作
            byte[] text1 = mac.doFinal(text);

            reString = Base64.encodeToString(text1, Base64.DEFAULT);

        } catch (Exception e) {
        }

        return reString;
    }


    public void doGetLocation(View view) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                //位置信息变化时触发
                mTvDesc.append("\n纬度：" + location.getLatitude());
                mTvDesc.append("\n经度：" + location.getLongitude());
                mTvDesc.append("\n海拔：" + location.getAltitude());
                mTvDesc.append("\n时间：" + location.getTime());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //GPS状态变化时触发

                mTvDesc.append("\nGPS状态变化");

            }

            @Override
            public void onProviderEnabled(String provider) {
                //GPS禁用时触发
                mTvDesc.append("\nGPS禁用");
            }

            @Override
            public void onProviderDisabled(String provider) {
                //GPS开启时触发
                mTvDesc.append("\nGPS开启");
            }
        };
        /**
         * 绑定监听
         * 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种，前者是GPS,后者是GPRS以及WIFI定位
         * 参数2，位置信息更新周期.单位是毫秒
         * 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
         * 参数4，监听
         * 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
            .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission
            .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        UIUtils.showToast("开始定位");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


    }

    /**
     * 通过WIFI获取定位信息
     */
    public   void fromWIFILocation(View view){
       //WIFI的MAC地址
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String wifiAddress = manager.getConnectionInfo().getBSSID();
        //根据WIFI信息定位

        wifi wifi = new wifi(wifiAddress, "8", "0");
        HttpHelper.create(OneApi.class).fromWIFILocation(wifi).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<JsonObject>() {
                @Override
                public void onHandlerSuccess(JsonObject value) {
                    mTvDesc.append(value.toString());
                }
            });

    }

}
