package dinson.customview.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dinson.customview.model.QiNiuFileInfo;
import dinson.customview.viewmodel.DriverView;

/**
 * Created by wrh on 16/2/15.
 */
public class _005QiNiuPicAdapter extends RecyclerView.Adapter<_005QiNiuPicAdapter.ItemHolder> {

    private Context context;
    private ArrayList<QiNiuFileInfo> list;

    public _005QiNiuPicAdapter(Context context, ArrayList<QiNiuFileInfo> list ) {
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
