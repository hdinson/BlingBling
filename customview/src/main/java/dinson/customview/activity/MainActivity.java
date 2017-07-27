package dinson.customview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import dinson.customview.R;
import dinson.customview._globle.BaseActivity;
import dinson.customview.adapter.MainAdapter;
import dinson.customview.adapter.MainPagerAdapter;
import dinson.customview.api.OneApi;
import dinson.customview.entity.ClassBean;
import dinson.customview.entity.one.DailyDetail;
import dinson.customview.entity.one.DailyList;
import dinson.customview.http.BaseObserver;
import dinson.customview.http.HttpHelper;
import dinson.customview.listener.MainItemTouchHelper;
import dinson.customview.listener.OnItemTouchMoveListener;
import dinson.customview.utils.CacheUtils;
import dinson.customview.utils.GlideUtils;
import dinson.customview.utils.LogUtils;
import dinson.customview.weight.recycleview.LinearItemDecoration;
import dinson.customview.weight.recycleview.OnItemClickListener;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements OnItemTouchMoveListener, OnItemClickListener.OnClickListener {

    RecyclerView mRvList;
    final int MAX_HEARD_PIC = 3;
    private ArrayList<ClassBean> mDatas = new ArrayList<>(); //内容的数据集
    private ArrayList<View> mViewPagerViews = new ArrayList<>(); //viewpager的数据集
    private OneApi mOneApi = HttpHelper.getRetrofit().create(OneApi.class);
    private ViewPager mViewPager;
    private MainPagerAdapter mPagerAdapter = new MainPagerAdapter(mViewPagerViews);


    int needLoadPagerId;
    private ItemTouchHelper mTouchHelper;//处理条目移动的帮助类
    private MainAdapter mMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initData();
        initUI();


        initHeardList();
    }

    private void initHeardList() {
        DailyList result = CacheUtils.getMainHeardCache();

        if (result != null && result.getData() != null && result.getData().size() > 0) {
            setHeardList(result);
            return;
        }


        Observable<DailyList> daily = mOneApi.getDaily();
        daily.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<DailyList>() {
                    @Override
                    public void onHandlerSuccess(DailyList value) {
                        CacheUtils.setMainHeardCache(value);
                        setHeardList(value);
                    }
                });
    }

    private void setHeardList(DailyList result) {
        needLoadPagerId = result.getData().get(0);
        ArrayList<DailyDetail> container = new ArrayList<>();
        ArrayList<Observable<DailyDetail>> observableList = new ArrayList<>();
        for (int i = 0; i < MAX_HEARD_PIC; i++) {
            DailyDetail bean = CacheUtils.getDailyDetail(needLoadPagerId);
            if (bean == null) {
                observableList.add(mOneApi.getDetail(needLoadPagerId));
            } else {
                container.add(bean);
            }
            needLoadPagerId--;
        }

        if (container.size() > 0) {
            for (DailyDetail dailyDetail : container) {
                initListViews(dailyDetail);
            }
            mPagerAdapter.setListViews(mViewPagerViews);// 重构adapter对象
            mPagerAdapter.notifyDataSetChanged();// 刷新
        }
        if (observableList.size() > 0) {
            Observable<DailyDetail>[] arr = new Observable[observableList.size()];
            for (int i = 0; i < observableList.size(); i++) {
                arr[i] = observableList.get(i);
            }
            getHearDetailFromServer(arr);
        }
    }

    public void getHearDetailFromServer(Observable<DailyDetail>[] arrs) {
        Observable.mergeArray(arrs).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<DailyDetail>() {
                    @Override
                    public void onHandlerSuccess(DailyDetail value) {
                        CacheUtils.setDailyDetail(value);
                        initListViews(value);
                        mPagerAdapter.setListViews(mViewPagerViews);// 重构adapter对象
                        mPagerAdapter.notifyDataSetChanged();// 刷新
                    }
                });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mDatas.add(new ClassBean(getString(R.string.facebook_title), getString(R.string.facebook_desc),
                _001ShimmerActivity.class, getString(R.string.facebook_img)));
        mDatas.add(new ClassBean(getString(R.string.qqnavi_title), getString(R.string.qqnavi_desc),
                _002QQNaviViewActivity.class, getString(R.string.qqnavi_img)));
        mDatas.add(new ClassBean(getString(R.string.diagonal_layout_title), getString(R.string.diagonal_layout_desc),
                _003DiagonalLayoutActivity.class, getString(R.string.diagonal_layout_img)));
        mDatas.add(new ClassBean(getString(R.string.ganged_recycle_title), getString(R.string.ganged_recycle_desc),
                _004GangedRecycleViewActivity.class, getString(R.string.ganged_recycle_img)));
        mDatas.add(new ClassBean(getString(R.string.test_layout_title), getString(R.string.test_layout_desc),
                TestActivity.class, getString(R.string.test_layout_img)));
    }

    /**
     * 初始化
     */
    private void initUI() {
        mRvList = (RecyclerView) findViewById(R.id.rvList);
        mMainAdapter = new MainAdapter(this, mDatas, this);
        mTouchHelper = new ItemTouchHelper(new MainItemTouchHelper(mMainAdapter));

        mTouchHelper.attachToRecyclerView(mRvList);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.addItemDecoration(new LinearItemDecoration(this));
        mRvList.setAdapter(mMainAdapter);
        mRvList.addOnItemTouchListener(new OnItemClickListener(this, mRvList, this));

        mViewPager = (ViewPager) findViewById(R.id.vp_heard);
        mViewPager.addOnPageChangeListener(mPageChangeListener);// 设置页面滑动监听
        mViewPager.setAdapter(mPagerAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(this, mDatas.get(position).getName()));
    }

    @Override
    public void onItemLongClick(View view, int position) {
    }

    /**
     * 页面监听事件
     */
    private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
        public void onPageSelected(int position) {// 页面选择响应函数
            // 如果需要实现页面滑动时动态添加 请在此判断arg0的值
            // 当然此方式在必须在初始化ViewPager的时候给的页数必须>2
            // 因为给1页的话 ViewPager是响应不了此函数的
            // 例：
            LogUtils.i("pos:" + position + " needLoadId:" + needLoadPagerId);

            DailyDetail dailyDetail = CacheUtils.getDailyDetail(needLoadPagerId);
            if (dailyDetail != null) {
                initListViews(dailyDetail);// listViews添加数据
                mPagerAdapter.setListViews(mViewPagerViews);// 重构adapter对象  这是一个很重要
                mPagerAdapter.notifyDataSetChanged();// 刷新
                needLoadPagerId--;
                return;
            }


            if (position == mViewPager.getAdapter().getCount() - 1) {// 滑动到最后一页
                mOneApi.getDetail(needLoadPagerId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<DailyDetail>() {
                            @Override
                            public void onHandlerSuccess(DailyDetail value) {
                                CacheUtils.setDailyDetail(value);
                                initListViews(value);// listViews添加数据
                                mPagerAdapter.setListViews(mViewPagerViews);// 重构adapter对象  这是一个很重要
                                mPagerAdapter.notifyDataSetChanged();// 刷新
                                needLoadPagerId--;
                            }
                        });
            }

            //UIUtils.showToast("翻到了第" + (position + 1) + "页");
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {// 滑动中。。。

        }

        public void onPageScrollStateChanged(int state) {// 滑动状态改变
        }
    };

    /**
     * listViews添加ImageView对象
     */
    private void initListViews(final DailyDetail bean) {
        final ImageView iv = new ImageView(this);// 构造textView对象
        // 设置布局
        iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setTransitionName("dailayPic");
        iv.setId(R.id.iv_img);
        // 设置图片
        GlideUtils.setImage(this, bean.getData().getHp_img_url(), iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Single.just(bean.getData().getHp_img_url()).map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {

                        String path = Glide.with(MainActivity.this).load(s).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get().getPath();

                        return path;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String path) throws Exception {

                                LogUtils.e("CurrentItem:" + mViewPager.getCurrentItem() + " imgUrl:" +
                                        bean.getData().getHp_img_url() + "  imgPath:" + path);

                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        MainActivity.this, iv, "dailyPic");
                                DailyPicActivity.start(MainActivity.this, bean.getData(), path, options);
                            }
                        });
            }
        });

        mViewPagerViews.add(iv);// 添加view
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(mPageChangeListener);
    }

    @Override
    public void onItemTouchMove(RecyclerView.ViewHolder viewHolder) {
        mTouchHelper.startDrag(viewHolder);
    }


}
