package dinson.customview.activity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import dinson.customview.R;
import dinson.customview._globle.BaseActivity;
import dinson.customview.api.OneApi;
import dinson.customview.http.HttpHelper;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends BaseActivity {

    private TextView mTvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initUI();

    }

    private void initUI() {
        mTvDesc = (TextView) findViewById(R.id.tv_desc);


    }


    public void doPost(View view) {


        String url = "dinson-blog:mh_ic_323.png"; // 编码前
        String url_encode = Base64.encodeToString(url.getBytes(), Base64.DEFAULT).trim();//  编码后

        String token ="QBox "+ getToken("/delete/"+url_encode+"\n");
        mTvDesc.append("url_encode : "+url_encode +"\ntoken : "+token);

        OneApi oneApi = HttpHelper.getRetrofit().create(OneApi.class);
        Observable<String> observable = oneApi.postDelete(url_encode  ,token.trim());
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        mTvDesc.append(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTvDesc.append(e.toString()+"\n");
                    }

                    @Override
                    public void onComplete() {
                        mTvDesc.append("onComplete\n");
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
        mTvDesc.append("sha1 : "+sha1 + "\n");

        String base64 = Base64.encodeToString(sha1.getBytes(), Base64.DEFAULT);
        mTvDesc.append("base64 : "+base64 + "\n");

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
