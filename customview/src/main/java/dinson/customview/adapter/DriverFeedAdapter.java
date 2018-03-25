package dinson.customview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dinson.customview.bindingview.DriverView;
import dinson.customview.model._005FileInfo;

/**
 * Created by wrh on 16/2/15.
 */
public class DriverFeedAdapter extends RecyclerView.Adapter<DriverFeedAdapter.ItemHolder> {

    private Context context;
    private ArrayList<_005FileInfo> list;

    public DriverFeedAdapter(Context context, ArrayList<_005FileInfo> list ) {
        this.context = context;
        this.list = list;
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
        holder.driverView.setData(list.get(position));
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
}
