package dinson.customview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import dinson.customview.bindingview.DriverView;
import dinson.customview.model.SizeModel;
import dinson.customview.model._005FileInfo;

/**
 * Created by wrh on 16/2/15.
 */
public class DriverFeedAdapter extends RecyclerView.Adapter<DriverFeedAdapter.ItemHolder> {

    private Context context;
    private ArrayList<_005FileInfo> list;
    private List<SizeModel> mSizeModel;

    public DriverFeedAdapter(Context context, ArrayList<_005FileInfo> list, List<SizeModel> sizeModel) {
        this.context = context;
        this.list = list;
        mSizeModel = sizeModel;
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DriverView view = new DriverView(context);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.driverView.setData(list.get(position) );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        DriverView driverView;

        public ItemHolder(View itemView) {
            super(itemView);
            driverView = (DriverView) itemView;
        }
    }
    /*private Context mContext;
    private List<_005FileInfo> mSizeModel;

    public DriverFeedAdapter(Context context, List<GankIoModel.ResultsEntity> data) {
        this.mContext = context;
        this.mData = data;
    }

    public void setSizeModel(List<SizeModel> sizeModel) {
        this.mSizeModel = sizeModel;
    }

    @Override
    protected ItemHolder onAdapterCreateViewHolder(ViewGroup viewGroup, int viewType) {
        DriverView driverView = new DriverView(mContext);
        ItemHolder itemHolder = new ItemHolder(driverView);
        return itemHolder;
    }

    @Override
    protected void onAdapterBindViewHolder(ItemHolder viewHolder, int position) {
        viewHolder.driverView.setData(mData.get(position), mSizeModel.get(position));
    }

    @Override
    protected View onLoadMoreCreateView(ViewGroup viewGroup) {
        return LayoutInflater.from(mContext).inflate(R.layout.view_common_loadmore, viewGroup, false);
    }

    @Override
    public int getAdapterItemCount() {
        return mData.size();
    }

    @Override
    public boolean hasHeader() {
        return false;
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        DriverView driverView;

        public ItemHolder(View itemView) {
            super(itemView);
            driverView = (DriverView) itemView;
        }
    }*/
}
