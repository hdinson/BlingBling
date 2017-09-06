package dinson.customview.weight.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 单一布局通用数据适配器
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder>   {
    private final String TAG = "LGRecycleViewAdapter";
    //存储监听回调

    public List<T> mDataList;
    public Context mContext;


    public interface ItemClickListener {
        void onItemClicked(View view, int position);
    }

    public CommonAdapter(Context context, List<T> dataList) {
        this.mDataList = dataList;
        this.mContext = context;
    }

    /**
     * 获取列表控件的视图id(由子类负责完成)
     *
     * @param viewType
     * @return
     */
    public abstract int getLayoutId(int viewType);

    //更新itemView视图(由子类负责完成)
    public abstract void convert(CommonViewHolder holder, T t, int position);

    public T getItem(int position) {
        if (mDataList == null || position < 0 || mDataList.size() <= position)
            return null;
        return mDataList.get(position);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutId(viewType);
        CommonViewHolder viewHolder = CommonViewHolder.getViewHolder(parent, layoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, final int position) {
        T itemModel = getItem(position);
        convert(holder, itemModel, position);//更新itemView视图
    }

    @Override
    public int getItemCount() {
        if (mDataList == null)
            return 0;
        return mDataList.size();
    }

    public void destroyAdapter() {
        if (mDataList != null)
            mDataList.clear();
        mDataList = null;
    }

}
