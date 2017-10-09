package dinson.customview.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.adapter.MainContentAdapter;
import dinson.customview.adapter.MainHeadAdapter;
import dinson.customview.api.OneApi;
import dinson.customview.api.XinZhiWeatherApi;
import dinson.customview.entity.ClassBean;
import dinson.customview.entity.HomeWeather;
import dinson.customview.entity.one.DailyDetail;
import dinson.customview.entity.one.DailyList;
import dinson.customview.http.BaseObserver;
import dinson.customview.http.HttpHelper;
import dinson.customview.listener.MainItemTouchHelper;
import dinson.customview.listener.OnItemTouchMoveListener;
import dinson.customview.model.HomeWeatherModelUtil;
import dinson.customview.utils.CacheUtils;
import dinson.customview.utils.LogUtils;
import dinson.customview.utils.TypefaceUtils;
import dinson.customview.weight.IconFontTextView;
import dinson.customview.weight.aspectratioview.AspectRatioRecycleView;
import dinson.customview.weight.recycleview.LinearItemDecoration;
import dinson.customview.weight.recycleview.OnItemClickListener;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements OnItemTouchMoveListener, OnItemClickListener.OnClickListener {

    private ArrayList<ClassBean> mContentData = new ArrayList<>(); //内容的数据集
    private ArrayList<DailyDetail> mHeadData = new ArrayList<>();

    private OneApi mOneApi = HttpHelper.create(OneApi.class);

    private ItemTouchHelper mTouchHelper;//处理条目移动的帮助类
    private MainHeadAdapter mMainHeadAdapter;
    private AMapLocationClient mAMapLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContent();
        initHead();
        getLocation();
    }


    /**
     * 初始化内容
     */
    private void initContent() {
        mContentData.add(new ClassBean(getString(R.string.facebook_title), getString(R.string.facebook_desc),
            _001ShimmerActivity.class, getString(R.string.facebook_img)));
        mContentData.add(new ClassBean(getString(R.string.qqnavi_title), getString(R.string.qqnavi_desc),
            _002QQNaviViewActivity.class, getString(R.string.qqnavi_img)));
        mContentData.add(new ClassBean(getString(R.string.diagonal_layout_title), getString(R.string.diagonal_layout_desc),
            _003DiagonalLayoutActivity.class, getString(R.string.diagonal_layout_img)));
        mContentData.add(new ClassBean(getString(R.string.ganged_recycle_title), getString(R.string.ganged_recycle_desc),
            _004GangedRecycleViewActivity.class, getString(R.string.ganged_recycle_img)));
        mContentData.add(new ClassBean(getString(R.string.like_smile_title), getString(R.string.like_smile_desc),
            _005LikeSmileViewActivity.class, getString(R.string.like_smile_img)));
        mContentData.add(new ClassBean(getString(R.string.floatingView_title), getString(R.string.floatingView_desc),
            _006FloatingViewActivity.class, getString(R.string.floatingView_img)));
        mContentData.add(new ClassBean(getString(R.string.spotlight_title), getString(R.string.spotlight_desc),
            _007SpotlightActivity.class, getString(R.string.spotlight_img)));
        mContentData.add(new ClassBean(getString(R.string.rich_editor_title), getString(R.string.rich_editor_desc),
            _008RichEditorActivity.class, getString(R.string.rich_editor_img)));
        mContentData.add(new ClassBean(getString(R.string.google_vr_title), getString(R.string.google_vr_desc),
            _009GoogleVRActivity.class, getString(R.string.google_vr_img)));
        mContentData.add(new ClassBean(getString(R.string.parallax_animation_title), getString(R.string.parallax_animation_desc),
            _010ParallaxActivity.class, getString(R.string.parallax_animation_img)));
        mContentData.add(new ClassBean(getString(R.string.test_layout_title), getString(R.string.test_layout_desc),
            TestActivity.class, getString(R.string.test_layout_img)));
        RecyclerView mRvContent = (RecyclerView) findViewById(R.id.rv_content);
        MainContentAdapter mMainAdapter = new MainContentAdapter(this, mContentData, this);
        mTouchHelper = new ItemTouchHelper(new MainItemTouchHelper(mMainAdapter));

        mTouchHelper.attachToRecyclerView(mRvContent);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new LinearItemDecoration(this));
        mRvContent.setAdapter(mMainAdapter);
        mRvContent.addOnItemTouchListener(new OnItemClickListener(this, mRvContent, this));

    }


    /**
     * 初始化首页one的数据
     */
    private void initHead() {
        AspectRatioRecycleView mRvHead = (AspectRatioRecycleView) findViewById(R.id.rv_head);
        mRvHead.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mMainHeadAdapter = new MainHeadAdapter(this, mHeadData);
        mRvHead.setAdapter(mMainHeadAdapter);
        mRvHead.addOnItemTouchListener(new OnItemClickListener(this, mRvHead, (OnItemClickListener.OnClickListener)
            (view, position) -> Single.just(mHeadData.get(position).getData().getHp_img_url())
                .map(s -> Glide.with(MainActivity.this).load(s).downloadOnly(Target.SIZE_ORIGINAL
                    , Target.SIZE_ORIGINAL).get().getPath())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(path -> {
                    LogUtils.e("CurrentItem:" + position + " imgUrl:" +
                        mHeadData.get(position).getData().getHp_img_url() + "  imgPath:" + path);

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this, view, "dailyPic");
                    DailyPicActivity.start(MainActivity.this, mHeadData.get(position).getData(), path, options);
                })));

        DailyList heardCache = CacheUtils.getMainHeardCache();
        Flowable.fromPublisher(heardCache == null ? mOneApi.getDaily() : Flowable.just(heardCache))
            .flatMap(dailyList -> {
                CacheUtils.setMainHeardCache(dailyList);
                return Flowable.fromIterable(dailyList.getData());
            })
            .flatMap(integer -> {
                DailyDetail detail = CacheUtils.getDailyDetail(integer);
                return detail == null ? mOneApi.getDetail(integer) : Flowable.just(detail);
            })
            .filter(integer -> integer.getData() != null)
            .collect(() -> mHeadData, (list, bean) -> {
                LogUtils.d(bean.toString());
                CacheUtils.setDailyDetail(bean);
                list.add(bean);
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(o -> mMainHeadAdapter.notifyItemChanged(0), throwable -> {
                LogUtils.d(throwable.toString());
                ViewStub layout = (ViewStub) findViewById(R.id.vs_content);
                layout.inflate();
                ImageView iv_img = (ImageView) findViewById(R.id.iv_img);
                iv_img.setImageResource(R.drawable.def_img);
            });
    }

    /**
     * 获取天气数据
     */
    private void getLocation() {
        mAMapLocationClient = new AMapLocationClient(this.getApplicationContext());
        AMapLocationClientOption locationOption = getAmapOption();
        mAMapLocationClient.setLocationOption(locationOption);//设置定位参数
        mAMapLocationClient.setLocationListener(locationListener);//设置定位监听
        mAMapLocationClient.startLocation();
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getAmapOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选，
        // 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 销毁定位
     */
    private void destroyLocation() {
        if (null != mAMapLocationClient) {
            mAMapLocationClient.onDestroy();
            mAMapLocationClient = null;
        }
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = location -> {
        // TODO: 2017/9/27 定位权限判断
        destroyLocation();//只定位一次
        if (null == location) return;
        if (location.getErrorCode() != 0) return; //errCode等于0代表定位成功

        Observable.just(location.getCity())
            .flatMap(city -> {
                HomeWeather cache = CacheUtils.getHomeWeatherCache(city);
                if (cache == null)
                    return HttpHelper.create(XinZhiWeatherApi.class).getWeather(city);
                return Observable.just(cache);
            })
            .map(homeWeather -> {
                CacheUtils.setHomeWeatherCache(homeWeather);
                return homeWeather;
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<HomeWeather>() {
                @Override
                public void onHandlerSuccess(HomeWeather weather) {
                    initWeatherLayout(weather);//设置数据
                }
            });
    };

    /**
     * 初始化天气布局
     *
     * @param weather 天气实体
     */
    private void initWeatherLayout(HomeWeather weather) {
        LogUtils.d("." + weather.toString());
        View weatherLayout = findViewById(R.id.weatherLayout);
        weatherLayout.setVisibility(View.VISIBLE);
        HomeWeather.ResultsBean resultsBean = weather.getResults().get(0);
        IconFontTextView if_weather = (IconFontTextView) findViewById(R.id.if_weather);
        if_weather.setText(HomeWeatherModelUtil.getWeatherFont(resultsBean.getNow().getCode()));
        TextView tv_weather = (TextView) findViewById(R.id.tv_weather);
        tv_weather.setTypeface(TypefaceUtils.get(getApplicationContext(), "fonts/FZLanTingHeiS_Regular.ttf"));
        tv_weather.setText(String.format("%s℃", resultsBean.getNow().getTemperature()));

        ObjectAnimator alpha = ObjectAnimator.ofFloat(weatherLayout, "alpha", 0f, 1.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(weatherLayout, "scaleX", 0f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(weatherLayout, "scaleY", 0f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator());
        set.play(alpha).with(scaleX).with(scaleY);
        set.start();
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(this, mContentData.get(position).getName()));
    }

    @Override
    public void onItemTouchMove(RecyclerView.ViewHolder viewHolder) {
        mTouchHelper.startDrag(viewHolder);
    }

    @Override
    public boolean finishWithAnim() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }
}
