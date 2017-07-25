package dinson.customview.activity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import dinson.customview.R;
import dinson.customview._globle.BaseActivity;
import dinson.customview.api.OneApi;
import dinson.customview.api.QzBusApi;
import dinson.customview.http.BaseObserver;
import dinson.customview.http.HttpHelper;
import dinson.customview.http.LoggingInterceptor;
import dinson.customview.utils.LogUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class TestActivity extends BaseActivity {

    private TextView mTvDesc;
    //手动创建一个OkHttpClient并设置超时时间
    OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(new LoggingInterceptor());//添加拦截器 日志;

    QzBusApi mApi = new Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl("http://wap.bus.qzjtjt.com")
            .client(builder.build())
            .build().create(QzBusApi.class);
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initUI();

    }

    private void initUI() {
        mTvDesc = (TextView) findViewById(R.id.tv_desc);
        mEditText = (EditText) findViewById(R.id.et_route);





    }


    public void doPost(View view) {


        //  qiniuyun();


        // qzBus();


          jsoupTest();


    }

    private void jsoupTest() {

        Observable.just("http://www.dinson.win/")
                .map(new Function<String, Document>() {
                    @Override
                    public Document apply(String s) throws Exception {
                        Document document = Jsoup.connect(s).get();
                        return document;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Document>() {
                    @Override
                    public void accept(Document document) throws Exception {
                        mTvDesc.setText("");
                        mTvDesc.append(document.title());
                        mTvDesc.append(document.body().toString());
                    }
                });
    }

    private void qzBus() {


        Call<String> call = mApi.queryRoute(mEditText.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mTvDesc.setText(response.body());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                LogUtils.i("onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    private void qiniuyun() {
        String url = "dinson-blog:mh_ic_323.png"; // 编码前
        String url_encode = Base64.encodeToString(url.getBytes(), Base64.DEFAULT).trim();//  编码后

        String token = "QBox " + getToken("/delete/" + url_encode + "\n");
        mTvDesc.append("url_encode : " + url_encode + "\ntoken : " + token);

        OneApi oneApi = HttpHelper.getRetrofit().create(OneApi.class);
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
            // TODO: handle exception
        }

        return reString;
    }
}
