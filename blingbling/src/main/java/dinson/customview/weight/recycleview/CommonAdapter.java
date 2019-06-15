package dinson.customview.weight.recycleview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 单一布局通用数据适配器
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {
    public List<T> mDataList;
    private SparseArray<CommonViewHolder> mViewHolder = new SparseArray<>();

    public CommonAdapter(  List<T> dataList) {
        this.mDataList = dataList;
    }

    /**
     * 获取列表控件的视图id(由子类负责完成)
     */
    public abstract int getLayoutId(int viewType);

    //更新itemView视图(由子类负责完成)
    public abstract void convert(CommonViewHolder holder, T t, int position);

    public T getItem(int position) {
        if (mDataList == null || position < 0 || mDataList.size() <= position)
            return null;
        return mDataList.get(position);
    }

    @NotNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        int layoutId = getLayoutId(viewType);
        return CommonViewHolder.getViewHolder(parent, layoutId);
    }

    @Override
    public void onBindViewHolder(@NotNull CommonViewHolder holder, final int position) {
        T itemModel = getItem(position);
        mViewHolder.put(position, holder);
        convert(holder, itemModel, position);//更新itemView视图
    }

    @Override
    public int getItemCount() {
        if (mDataList == null)
            return 0;
        return mDataList.size();
    }


    public CommonViewHolder getCommonViewHolder(int position) {
        return mViewHolder.get(position);
    }
}
