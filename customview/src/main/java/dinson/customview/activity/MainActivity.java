package dinson.customview.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.adapter.MainContentAdapter;
import dinson.customview.adapter.MainHeadAdapter;
import dinson.customview.api.OneApi;
import dinson.customview.entity.ClassBean;
import dinson.customview.entity.one.DailyDetail;
import dinson.customview.entity.one.DailyList;
import dinson.customview.http.HttpHelper;
import dinson.customview.listener.MainItemTouchHelper;
import dinson.customview.listener.OnItemTouchMoveListener;
import dinson.customview.utils.CacheUtils;
import dinson.customview.utils.LogUtils;
import dinson.customview.utils.UIUtils;
import dinson.customview.weight.aspectratioview.AspectRatioRecycleView;
import dinson.customview.weight.recycleview.LinearItemDecoration;
import dinson.customview.weight.recycleview.OnItemClickListener;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements OnItemTouchMoveListener, OnItemClickListener.OnClickListener {

    private ArrayList<ClassBean> mContentData = new ArrayList<>(); //内容的数据集
    private ArrayList<DailyDetail> mHeadData = new ArrayList<>();

    private OneApi mOneApi = HttpHelper.create(OneApi.class);

    private ItemTouchHelper mTouchHelper;//处理条目移动的帮助类
    private MainHeadAdapter mMainHeadAdapter;
    private LocationManager mLocationManager;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        long start = System.currentTimeMillis();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContent();
        initHead();
        getLocation();

        long end = System.currentTimeMillis();
        LogUtils.v(String.format(Locale.CHINA, "HomeActivity started %d ms", end - start));
    }


    /**
     * 初始化内容
     */
    private void initContent() {
        mContentData.add(new ClassBean(getString(R.string.facebook_title), getString(R.string.facebook_desc),
            _001ShimmerActivity.class, getString(R.string.facebook_img)));
        mContentData.add(new ClassBean(getString(R.string.qqnavi_title), getString(R.string.qqnavi_desc),
            _002QQNaviViewActivity.class, getString(R.string.qqnavi_img)));
        mContentData.add(new ClassBean(getString(R.string.diagonal_layout_title), getString(R.string
            .diagonal_layout_desc),
            _003DiagonalLayoutActivity.class, getString(R.string.diagonal_layout_img)));
        mContentData.add(new ClassBean(getString(R.string.ganged_recycle_title), getString(R.string
            .ganged_recycle_desc),
            _004GangedRecycleViewActivity.class, getString(R.string.ganged_recycle_img)));
        mContentData.add(new ClassBean(getString(R.string.like_smile_title), getString(R.string.like_smile_desc),
            _005LikeSmileViewActivity.class, getString(R.string.like_smile_img)));
        mContentData.add(new ClassBean(getString(R.string.floatingView_title), getString(R.string.floatingView_desc),
            _006FloatingViewActivity.class, getString(R.string.floatingView_img)));
        mContentData.add(new ClassBean(getString(R.string.spotlight_title), getString(R.string.spotlight_desc),
            _007SpotlightActivity.class, getString(R.string.spotlight_img)));
        mContentData.add(new ClassBean(getString(R.string.rich_editor_title), getString(R.string.rich_editor_desc),
            _008_RichEditorActivity.class, getString(R.string.rich_editor_img)));
        mContentData.add(new ClassBean(getString(R.string.google_vr_title), getString(R.string.google_vr_desc),
            _009GoogleVRActivity.class, getString(R.string.google_vr_img)));
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
                LogUtils.e(bean.toString());
                CacheUtils.setDailyDetail(bean);
                list.add(bean);
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(o -> mMainHeadAdapter.notifyItemChanged(0), throwable -> {
                LogUtils.e(throwable.toString());
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

        LogUtils.e("正在验证权限...");
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
            .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission
            .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling 验证权限
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LogUtils.e("权限验证通过，正在验证位置服务...");
        assert mLocationManager != null;
        LogUtils.e("位置服务通过，正在请求位置...");
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }


    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            LogUtils.e("location lat:" + location.getLatitude() + ",lon:" + location.getLongitude());
            mLocationManager.removeUpdates(this);

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude()
                    , 1);
            } catch (IOException e) {
                UIUtils.showToast("位置服务不可用");
            }

            if (addresses == null || addresses.size() == 0) {
                UIUtils.showToast("位置服务无相关地址");
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList< >();
                String curAddr = "";
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                    curAddr = curAddr + address.getAddressLine(i);
                }
                if (!TextUtils.isEmpty(address.getFeatureName())
                    && !addressFragments.isEmpty()
                    && !addressFragments.get(addressFragments.size() - 1).equals(address.getFeatureName())) {
                    addressFragments.add(address.getFeatureName());
                    curAddr = curAddr + address.getFeatureName();
                }
                UIUtils.showToast("详情地址已经找到,地址:" + curAddr);
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogUtils.e("onStatusChanged");
        }

        public void onProviderEnabled(String provider) {
            LogUtils.e("onProviderEnabled");
        }

        public void onProviderDisabled(String provider) {
            LogUtils.e("onProviderDisabled");
        }
    };

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
}
