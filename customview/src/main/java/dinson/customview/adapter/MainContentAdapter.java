package dinson.customview.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import dinson.customview.R;
import dinson.customview.entity.ClassBean;
import dinson.customview.listener.OnItemMoveListener;
import dinson.customview.listener.OnItemTouchMoveListener;
import dinson.customview.utils.GlideUtils;
import dinson.customview.weight.recycleview.CommonAdapter;
import dinson.customview.weight.recycleview.CommonViewHolder;

import static dinson.customview.R.id.iv_one;

/**
 * 主页适配器
 */
public class MainContentAdapter extends CommonAdapter<ClassBean> implements OnItemMoveListener {

    private final OnItemTouchMoveListener mMoveListener;
    private int[] mNumberImgs = new int[]{R.drawable.n_0, R.drawable.n_1, R.drawable.n_2, R.drawable.n_3,
        R.drawable.n_4, R.drawable.n_5, R.drawable.n_6, R.drawable.n_7, R.drawable.n_8, R.drawable.n_9};


    public MainContentAdapter(Context context, List<ClassBean> dataList, OnItemTouchMoveListener touchMoveListener) {
        super(context, dataList);
        mMoveListener = touchMoveListener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_main_content;
    }

    @Override
    public void convert(CommonViewHolder holder, ClassBean classBean, int position) {

        holder.setTvText(R.id.tv_title, classBean.getTitle());
        TextView tv_title = holder.getView(R.id.tv_title);
        Typeface typeFace =Typeface.createFromAsset(mContext.getAssets(),"fonts/FZLanTingHeiS_Regular.ttf");
        tv_title.setTypeface(typeFace);
        holder.setTvText(R.id.tv_desc, classBean.getDesc());
        int[] ints = formatPosition(position + 1);
        ImageView ivImg = holder.getView(R.id.iv_img);
        GlideUtils.setCircleImage(mContext, classBean.getImgUrl(), ivImg);
        ivImg.setOnTouchListener(new ViewHolderTouchListener(holder));
        if (ints[0] == 0)
            holder.getView(iv_one).setVisibility(View.GONE);
        else
            holder.setIvSrc(iv_one, mNumberImgs[ints[0]]);
        holder.setIvSrc(R.id.iv_second, mNumberImgs[ints[1]]);
        holder.setIvSrc(R.id.iv_third, mNumberImgs[ints[2]]);
    }


    private static int[] formatPosition(int pos) {
        if (pos < 10) {
            return new int[]{0, 0, pos};
        }
        if (pos >= 10 && pos < 100) {
            int second = pos / 10;
            int first = pos % 10;
            return new int[]{0, second, first};
        }
        int third = pos / 100;
        int second = (pos % 100) / 10;
        int first = (pos % 100) % 10;
        return new int[]{third, second, first};
    }


    private class ViewHolderTouchListener implements View.OnTouchListener {
        private RecyclerView.ViewHolder mHolder;

        private ViewHolderTouchListener(RecyclerView.ViewHolder viewHolder) {
            mHolder = viewHolder;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mMoveListener.onItemTouchMove(mHolder);
                    return true;
            }
            return false;
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDataList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemSwipe2Remove(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
    }
}
