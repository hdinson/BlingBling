package dinson.customview.activity;

import android.os.Bundle;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class TestActivity extends BaseActivity   {

    private TextView mTvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);

        mTvDesc = (TextView) findViewById(R.id.tv_dis);
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
}
