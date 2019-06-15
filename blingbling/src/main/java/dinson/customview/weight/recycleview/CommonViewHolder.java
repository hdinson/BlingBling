package dinson.customview.weight.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 单一布局通用viewholder
 */
public class CommonViewHolder extends RecyclerView.ViewHolder {
    public CommonViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 加载layoutId视图并用LGViewHolder保存
     */
    static CommonViewHolder getViewHolder(ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new CommonViewHolder(itemView);
    }
}
