package dinson.customview.widget.refreshview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import dinson.customview.R;

/**
 * 一个支持网络错误重试，无数据页（可自定义），无网络界面（可自定义）的上拉加载更多，下拉刷新控件
 * <p>
 * SwipeRefreshLayout + recyclerView
 */
public class CustomRefreshView extends FrameLayout
    implements SwipeRefreshLayout.OnRefreshListener {

    private View mEmptyView;
    //private TextView mEmptyText;

    private View mErrorView;

    private FrameLayout blankView;

    private BaseFooterView mFootView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView.LayoutManager mLayoutManager;
    public OnLoadListener mListener;
    private DataObserver mDataObserver;
    private WrapperAdapter mWrapperAdapter;

    private boolean isEmptyViewShowing;
    private boolean isLoadingMore;
    public boolean isLoadMoreEnable;
    private boolean isRefreshEnable;
    private boolean hasNoMore;

    private int lastVisiblePosition = 0;
    private Context context;


    public CustomRefreshView(Context context) {
        this(context, null);
    }

    public CustomRefreshView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRefreshView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setupSwipeRecyclerView();
    }

    private void setupSwipeRecyclerView() {

        isEmptyViewShowing = false;
        isRefreshEnable = true;
        isLoadingMore = false;
        isLoadMoreEnable = true;
        hasNoMore = false;

        mFootView = new SimpleFooterView(getContext());
        //将this传入，加载失败自动回调load more
        mFootView.setCustomRefreshView(this);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_refresh_layout, this);
        mRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout);

        blankView = view.findViewById(R.id.blank_view);
        LayoutParams params = new LayoutParams(-1, -1);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        blankView.setLayoutParams(params);

        //默认下拉刷新ui颜色
        mRefreshLayout.setColorSchemeColors(Color.parseColor("#000000"));
        recyclerView = view.findViewById(R.id.RecyclerView);
        //默认线性布局
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mLayoutManager = recyclerView.getLayoutManager();

        mRefreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 禁止加载更多、正在下拉刷新、正在加载更多 return
                // isLoadingMore为框架控制，当需要禁止加载时操作isLoadMoreEnable(public)
                if (!isLoadMoreEnable || isRefreshing() || isLoadingMore || hasNoMore) {
                    return;
                }
                mLayoutManager = recyclerView.getLayoutManager();
                if (mLayoutManager instanceof LinearLayoutManager) {
                    lastVisiblePosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                }
                if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager layoutManager = ((StaggeredGridLayoutManager) mLayoutManager);
                    layoutManager.invalidateSpanAssignments();
                    int[] temp = layoutManager.findLastVisibleItemPositions(null);
                    lastVisiblePosition = temp[0];
                    for (int i : temp) {
                        if (i > lastVisiblePosition)
                            lastVisiblePosition = i;
                    }
                }
                int childCount = mWrapperAdapter == null ? 0 : mWrapperAdapter.getItemCount();
                if (lastVisiblePosition >= childCount - 1 && !isLoadingMore) {
                    if (mListener != null) {
                        isLoadingMore = true;
                        mListener.onLoadMore();
                    }
                }
            }
        });
    }


    /**
     * 禁止下拉刷新
     *
     * @param refreshEnable 是否支持刷新
     */
    public void setRefreshEnable(boolean refreshEnable) {
        isRefreshEnable = refreshEnable;
        mRefreshLayout.setEnabled(isRefreshEnable);
    }

    public boolean getRefreshEnable() {
        return isRefreshEnable;
    }


    /**
     * 设置是否加载更多
     *
     * @param loadMoreEnable 是否加载更多
     */
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        if (!loadMoreEnable) {
            stopLoadingMore();
        }
        isLoadMoreEnable = loadMoreEnable;
    }

    public boolean getLoadMoreEnable() {
        return isLoadMoreEnable;
    }


    /**
     * 是否下拉刷新中
     *
     * @return 是否下拉刷新中
     */
    public boolean isRefreshing() {
        return mRefreshLayout.isRefreshing();
    }


    /**
     * 是否加载更多中
     *
     * @return 是否加载更多中
     */
    public boolean isLoadingMore() {
        return isLoadingMore;
    }


    /**
     * 空识图是否展示中
     *
     * @return 空识图是否展示中
     */
    public boolean isEmptyViewShowing() {
        return isEmptyViewShowing;
    }


    /**
     * 获取SwipeRefreshLayout，更改属性
     *
     * @return swipeRefreshLayout
     */
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mRefreshLayout;
    }


    /**
     * 获取recyclerView，更改属性
     *
     * @return RecyclerView
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }


    /**
     * 设置listener
     *
     * @param listener listener
     */
    public void setOnLoadListener(OnLoadListener listener) {
        mListener = listener;
    }


    /**
     * 设置footerView
     *
     * @param footerView footerView
     */
    public void setFooterView(BaseFooterView footerView) {
        this.mFootView = footerView;
    }


    /**
     * 设置默认空视图，无数据的情况下
     *
     * @param s : 文案显示
     */
    public void setEmptyView(String s) {
        if (blankView.getChildCount() > 0) {
            blankView.removeAllViews();
        }
        if (mEmptyView == null) {
            mEmptyView = LayoutInflater.from(context).inflate(R.layout.widget_refresh_empty_view, null);
            //mEmptyText = (TextView) mEmptyView.findViewById(R.id.module_base_empty_text);
        }
        blankView.addView(mEmptyView);


        //mEmptyText.setText(s);
        if (mDataObserver != null) {
            mDataObserver.onChanged();
        }
    }

    /**
     * 设置默认错误视图，当网络出现问题
     */
    public void setErrorView() {
        if (blankView.getChildCount() > 0) {
            blankView.removeAllViews();
        }
        if (mErrorView == null) {
            mErrorView = LayoutInflater.from(context).inflate(R.layout.widget_refresh_error_view, null);
            mErrorView.findViewById(R.id.module_base_id_fail_retry).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //重试
                    setRefreshing(true);
                }
            });
        }
        blankView.addView(mErrorView);

        if (mDataObserver != null) {
            mDataObserver.onChanged();
        }
    }

    public void setCreateView(View createView) {
        if (blankView.getChildCount() > 0) {
            blankView.removeAllViews();
        }
        blankView.addView(createView);

        if (mDataObserver != null) {
            mDataObserver.onChanged();
        }

    }

    /**
     * setAdapter，footerView的样式变化交给包装类展示
     *
     * @param adapter Adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            if (mDataObserver == null) {
                mDataObserver = new DataObserver();
            }
            mWrapperAdapter = new WrapperAdapter(adapter);
            recyclerView.setAdapter(mWrapperAdapter);
            adapter.registerAdapterDataObserver(mDataObserver);
            mDataObserver.onChanged();
        }
    }


    /**
     * 共用一个完成状态：下拉刷新或上拉加载完成，用于控制footerView-ui
     */
    public void complete() {
        mRefreshLayout.setRefreshing(false);
        stopLoadingMore();
    }


    /**
     * set refreshing
     * 首次加载时
     *
     * @param refreshing 是否加载中
     */
    public void setRefreshing(boolean refreshing) {
        mRefreshLayout.setRefreshing(refreshing);
        if (refreshing && !isLoadingMore && mListener != null) {
            mListener.onRefresh();
        }
    }

    /**
     * 停止加载更多
     */
    public void stopLoadingMore() {
        isLoadingMore = false;
        if (mWrapperAdapter != null) {
            mWrapperAdapter.notifyItemRemoved(mWrapperAdapter.getItemCount());
        }
    }

    /**
     * SwipeRefreshLayout.OnRefreshListener的实现
     */
    @Override
    public void onRefresh() {
        if (mListener != null) {
            isLoadingMore = false;
            hasNoMore = false;
            //重置footerView的样式（防止滑动最后一条时用户又下拉刷新）
            if (mFootView != null) {
                mFootView.onLoadingMore();
            }
            mListener.onRefresh();
        }
    }


    /**
     * 恢复footerView的正常状态
     */
    public void onLoadingMore() {
        if (mFootView != null) {
            mFootView.onLoadingMore();
        }
    }

    /**
     * footerView ui-没有更多数据
     */
    public void onNoMore() {
        if (mFootView != null) {
            hasNoMore = true;
            mFootView.onNoMore();
        }
    }

    /**
     * footerView ui-加载出错
     * 备注：error之后，pager自行减1
     */
    public void onError() {
        if (mFootView != null) {
            mFootView.onError();
        }
    }

    /**
     * WrapperAdapter ：包装类
     * 扩展实体adapter，控制footerView展示
     */
    private class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_FOOTER = 0x100;

        RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;

        private WrapperAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
            this.mInnerAdapter = adapter;
        }

        private boolean isLoadMoreItem(int position) {
            return isLoadMoreEnable && position == getItemCount() - 1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (TYPE_FOOTER == viewType && mFootView != null) {
                //当出现nomore或onerror，主动改变footerView样式
//                mFootView.onLoadingMore();
                return new FooterViewHolder(mFootView);
            }
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (isLoadMoreItem(position)) {
                return;
            }
            mInnerAdapter.onBindViewHolder(holder, position);
        }


        @Override
        public int getItemViewType(int position) {
            if (isLoadMoreItem(position)) {
                return TYPE_FOOTER;
            } else {
                return mInnerAdapter.getItemViewType(position);
            }
        }

        @Override
        public int getItemCount() {
            int count = mInnerAdapter == null ? 0 : mInnerAdapter.getItemCount();

            if (count == 0) {
                return 0;
            }
            return isLoadMoreEnable ? count + 1 : count;
        }

        @Override
        public long getItemId(int position) {
            return mInnerAdapter.getItemId(position);
        }

        @Override
        public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            mInnerAdapter.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            if (mInnerAdapter.hasObservers()) {
                mInnerAdapter.unregisterAdapterDataObserver(observer);
            }
        }

    }


    private class FooterViewHolder extends RecyclerView.ViewHolder {
        private FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 监控数据变化，用来控制无数据界面展示
     */
    class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            handlerDataChange();
        }

        @Override
        public void onChanged() {
            super.onChanged();
            handlerDataChange();
        }

        private void handlerDataChange() {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter != null) {
                if (adapter.getItemCount() == 0) {
                    isEmptyViewShowing = true;
                    recyclerView.setVisibility(View.GONE);
                    blankView.setVisibility(VISIBLE);
                } else {
                    isEmptyViewShowing = false;
                    blankView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mWrapperAdapter != null) {
            mWrapperAdapter.unregisterAdapterDataObserver(mDataObserver);
        }
        super.onDetachedFromWindow();
    }

    public interface OnLoadListener {

        void onRefresh();

        void onLoadMore();
    }
}
