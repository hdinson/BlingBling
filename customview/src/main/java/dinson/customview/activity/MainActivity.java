package dinson.customview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Callable;

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
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements OnItemTouchMoveListener, OnItemClickListener.OnClickListener {

    private RecyclerView mRvContent;
    private AspectRatioRecycleView mRvHead;

    private ArrayList<ClassBean> mContentData = new ArrayList<>(); //内容的数据集
    private ArrayList<DailyDetail> mHeadData = new ArrayList<>();

    private OneApi mOneApi = HttpHelper.getRetrofit().create(OneApi.class);

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
        mContentData.add(new ClassBean(getString(R.string.facebook_title), getString(R.string.facebook_desc),
            _001ShimmerActivity.class, getString(R.string.facebook_img)));
        mContentData.add(new ClassBean(getString(R.string.qqnavi_title), getString(R.string.qqnavi_desc),
            _002QQNaviViewActivity.class, getString(R.string.qqnavi_img)));
        mContentData.add(new ClassBean(getString(R.string.diagonal_layout_title), getString(R.string.diagonal_layout_desc),
            _003DiagonalLayoutActivity.class, getString(R.string.diagonal_layout_img)));
        mContentData.add(new ClassBean(getString(R.string.ganged_recycle_title), getString(R.string.ganged_recycle_desc),
            _004GangedRecycleViewActivity.class, getString(R.string.ganged_recycle_img)));
        mContentData.add(new ClassBean(getString(R.string.test_layout_title), getString(R.string.test_layout_desc),
            TestActivity.class, getString(R.string.test_layout_img)));

        mRvContent = (RecyclerView) findViewById(R.id.rv_content);
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
        mRvHead = (AspectRatioRecycleView) findViewById(R.id.rv_head);
        mRvHead.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mMainHeadAdapter = new MainHeadAdapter(this, mHeadData);
        mRvHead.setAdapter(mMainHeadAdapter);
        mRvHead.addOnItemTouchListener(new OnItemClickListener(this, mRvHead, new OnItemClickListener.OnClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                Single.just(mHeadData.get(position).getData().getHp_img_url())
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(String s) throws Exception {
                            return Glide.with(MainActivity.this).load(s).downloadOnly(Target.SIZE_ORIGINAL
                                , Target.SIZE_ORIGINAL).get().getPath();
                        }
                    })
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String path) throws Exception {

                            LogUtils.e("CurrentItem:" + position + " imgUrl:" +
                                mHeadData.get(position).getData().getHp_img_url() + "  imgPath:" + path);

                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                MainActivity.this, view, "dailyPic");
                            DailyPicActivity.start(MainActivity.this, mHeadData.get(position).getData(), path, options);
                        }
                    });
            }
        }));


        DailyList heardCache = CacheUtils.getMainHeardCache();
        Flowable.fromPublisher(heardCache == null ? mOneApi.getDaily() : Flowable.just(heardCache))
            .flatMap(new Function<DailyList, Flowable<Integer>>() {
                @Override
                public Flowable<Integer> apply(DailyList dailyList) throws Exception {
                    CacheUtils.setMainHeardCache(dailyList);
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
                    return integer.getData() != null;
                }
            })
            .collect(new Callable<ArrayList<DailyDetail>>() {
                @Override
                public ArrayList<DailyDetail> call() throws Exception {
                    return mHeadData;
                }
            }, new BiConsumer<ArrayList<DailyDetail>, DailyDetail>() {
                @Override
                public void accept(ArrayList<DailyDetail> list, DailyDetail bean) throws Exception {
                    LogUtils.e(bean.toString());
                    CacheUtils.setDailyDetail(bean);
                    list.add(bean);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    mMainHeadAdapter.notifyItemChanged(0);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    LogUtils.e(throwable.toString());
                    ViewStub layout = (ViewStub) findViewById(R.id.vs_content);
                    layout.inflate();
                    ImageView iv_img = (ImageView) findViewById(R.id.iv_img);
                    iv_img.setImageResource(R.drawable.def_img);
                }
            });
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(this, mContentData.get(position).getName()));
    }

    @Override
    public void onItemTouchMove(RecyclerView.ViewHolder viewHolder) {
        mTouchHelper.startDrag(viewHolder);
    }
}
