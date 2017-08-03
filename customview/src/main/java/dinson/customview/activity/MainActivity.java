package dinson.customview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

import dinson.customview.R;
import dinson.customview._globle.BaseActivity;
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
import dinson.customview.weight.AspectRatioView.AspectRatioRecycleView;
import dinson.customview.weight.recycleview.LinearItemDecoration;
import dinson.customview.weight.recycleview.OnItemClickListener;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements OnItemTouchMoveListener, OnItemClickListener.OnClickListener {

    private RecyclerView mRvContent;
    private AspectRatioRecycleView mRvHead;

    private ArrayList<ClassBean> mContentDatas = new ArrayList<>(); //内容的数据集
    private ArrayList<DailyDetail> mHeadDatas = new ArrayList<>();

    final int MAX_HEAD_PIC = 10;
    //private ArrayList<View> mViewPagerViews = new ArrayList<>(); //viewpager的数据集
    private OneApi mOneApi = HttpHelper.getRetrofit().create(OneApi.class);
    //private MainPagerAdapter mPagerAdapter = new MainPagerAdapter(mViewPagerViews);


    int needLoadPagerId;
    private ItemTouchHelper mTouchHelper;//处理条目移动的帮助类
    private MainHeadAdapter mMainHeadAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        long start = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContent();
        initHead();

        long end = System.currentTimeMillis();
        LogUtils.v(String.format(Locale.CHINA, "HomeActivity started %d ms", end - start));
    }

    /**
     * 初始化内容
     */
    private void initContent() {
        mContentDatas.add(new ClassBean(getString(R.string.facebook_title), getString(R.string.facebook_desc),
            _001ShimmerActivity.class, getString(R.string.facebook_img)));
        mContentDatas.add(new ClassBean(getString(R.string.qqnavi_title), getString(R.string.qqnavi_desc),
            _002QQNaviViewActivity.class, getString(R.string.qqnavi_img)));
        mContentDatas.add(new ClassBean(getString(R.string.diagonal_layout_title), getString(R.string.diagonal_layout_desc),
            _003DiagonalLayoutActivity.class, getString(R.string.diagonal_layout_img)));
        mContentDatas.add(new ClassBean(getString(R.string.ganged_recycle_title), getString(R.string.ganged_recycle_desc),
            _004GangedRecycleViewActivity.class, getString(R.string.ganged_recycle_img)));
        mContentDatas.add(new ClassBean(getString(R.string.test_layout_title), getString(R.string.test_layout_desc),
            TestActivity.class, getString(R.string.test_layout_img)));


        mRvContent = (RecyclerView) findViewById(R.id.rv_content);
        MainContentAdapter mMainAdapter = new MainContentAdapter(this, mContentDatas, this);
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
        mRvHead = (AspectRatioRecycleView) findViewById(R.id.rv_head);
        mRvHead.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mMainHeadAdapter = new MainHeadAdapter(this, mHeadDatas);
        mRvHead.setAdapter(mMainHeadAdapter);

        Flowable.just("")
            .flatMap(new Function<Object, Flowable<DailyList>>() {
                @Override
                public Flowable<DailyList> apply(Object o) throws Exception {
                    DailyList heardCache = CacheUtils.getMainHeardCache();
                    return heardCache == null ? mOneApi.getDaily() : Flowable.just(heardCache);
                }
            })
            .flatMap(new Function<DailyList, Flowable<Integer>>() {
                @Override
                public Flowable<Integer> apply(DailyList dailyList) throws Exception {
                    CacheUtils.setMainHeardCache(dailyList);
                    //SPUtils.setLastPostId(MainActivity.this, dailyList.getData().get(0));
                    return Flowable.fromIterable(dailyList.getData());
                }
            })
            .flatMap(new Function<Integer, Flowable<DailyDetail>>() {
                @Override
                public Flowable<DailyDetail> apply(Integer integer) throws Exception {
                    DailyDetail detail = CacheUtils.getDailyDetail(integer);
                    return detail == null ? mOneApi.getDetail(integer) : Flowable.just(detail);
                }
            })
            .filter(new Predicate<DailyDetail>() {
                @Override
                public boolean test(DailyDetail integer) throws Exception {
                    return integer.getData() == null;
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<DailyDetail>() {
                @Override
                public void accept(DailyDetail dailyDetail) throws Exception {
                  LogUtils.e("-------------"+dailyDetail.getData().getHp_author());
                }
            });


            /*.map(new Function<Integer, ArrayList<DailyDetail>>() {
                @Override
                public ArrayList<DailyDetail> apply(Integer integer) throws Exception {
                    ArrayList<DailyDetail> result = new ArrayList<>();
                    for (int i = 0, j = integer; i < MAX_HEAD_PIC; i++, j--) {
                        DailyDetail dailyDetail = CacheUtils.getDailyDetail(j);
                        if (dailyDetail == null) continue;
                        result.add(dailyDetail);
                    }
                    return result;
                }
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<ArrayList<DailyDetail>>() {
                @Override
                public void accept(ArrayList<DailyDetail> dailyDetails) throws Exception {
                    mHeadDatas.addAll(dailyDetails);
                    mMainHeadAdapter.notifyItemChanged(0);
                    LogUtils.d(mHeadDatas.size() + "---?");
                }
            });
              /*  subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<DailyList>() {
                @Override
                public void onHandlerSuccess(DailyList value) {

                }
            });*/


        //获取首页的最新id，根据id查找对应的缓存
       /* final int lastPostId = SPUtils.getLastPostId(this);
        if (lastPostId == 0) return;*/


        /*Observable.just(lastPostId)
            .map(new Function<Integer, ArrayList<DailyDetail>>() {
                @Override
                public ArrayList<DailyDetail> apply(Integer integer) throws Exception {
                    ArrayList<DailyDetail> result = new ArrayList<DailyDetail>();
                    for (int i = 0, j = integer; i < MAX_HEAD_PIC; i++, j--) {
                        DailyDetail dailyDetail = CacheUtils.getDailyDetail(j);
                        if (dailyDetail == null) continue;
                        result.add(dailyDetail);
                    }
                    return result;
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<ArrayList<DailyDetail>>() {
                @Override
                public void accept(ArrayList<DailyDetail> dailyDetails) throws Exception {
                    mHeadDatas.addAll(dailyDetails);
                    mMainHeadAdapter.notifyItemChanged(0);
                    LogUtils.d(mHeadDatas.size() + "---?");
                }
            });*/


        /*Observable.range(lastPostId, MAX_HEAD_PIC)
            .map(new Function<Integer, DailyDetail>() {
                @Override
                public DailyDetail apply(Integer integer) throws Exception {
                    int id = lastPostId * 2 - integer;
                    return CacheUtils.getDailyDetail(id);
                }
            })
            .filter(new Predicate<DailyDetail>() {
                @Override
                public boolean test(DailyDetail dailyDetail) throws Exception {
                    return dailyDetail != null;
                }
            })
            .collect(new Callable<ArrayList<DailyDetail>>() {
                @Override
                public ArrayList<DailyDetail> call() throws Exception {
                    return mHeadDatas;
                }
            }, new BiConsumer<ArrayList<DailyDetail>, DailyDetail>() {
                @Override
                public void accept(ArrayList<DailyDetail> list, DailyDetail bean) throws Exception {
                    list.add(bean);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<ArrayList<DailyDetail>>() {
                @Override
                public void accept(ArrayList<DailyDetail> dailyDetails) throws Exception {
                    LogUtils.e(dailyDetails.size() + "---");
                }
            });*/


    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(this, mContentDatas.get(position).getName()));
    }

    @Override
    public void onItemLongClick(View view, int position) {
    }

    @Override
    public void onItemTouchMove(RecyclerView.ViewHolder viewHolder) {
        mTouchHelper.startDrag(viewHolder);
    }


    //******************************************************************************************************************************


    /*private void initHeardList() {
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
    }*/


    /*private void initUI() {
        *//*mRvHead.addOnPageChangeListener(mPageChangeListener);// 设置页面滑动监听*//*
        *//*mRvHead.setAdapter(mPagerAdapter);*//*
    }*/


    /*private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
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


            if (position == mRvHead.getAdapter().getCount() - 1) {// 滑动到最后一页
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
    };*/

    /*private void initListViews(final DailyDetail bean) {
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

                        return Glide.with(MainActivity.this).load(s).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get().getPath();

                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String path) throws Exception {

                                LogUtils.e("CurrentItem:" + mRvHead.getCurrentItem() + " imgUrl:" +
                                        bean.getData().getHp_img_url() + "  imgPath:" + path);

                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        MainActivity.this, iv, "dailyPic");
                                DailyPicActivity.start(MainActivity.this, bean.getData(), path, options);
                            }
                        });
            }
        });

        mViewPagerViews.add(iv);// 添加view
    }*/
    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        *//*mRvHead.removeOnPageChangeListener(mPageChangeListener);*//*
    }*/


}
