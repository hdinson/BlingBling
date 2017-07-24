package dinson.customview.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dinson.customview.utils.LogUtils;

/**
 * Created by DINSON on 2017/7/2.
 */

public class MainPagerAdapter extends PagerAdapter {

    private ArrayList<View> listViews;// content

    private int size;// 页数

    public MainPagerAdapter(ArrayList<View> listViews) {// 构造函数
        // 初始化viewpager的时候给的一个页面
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
    }

    public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据  这个可是重点啊
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
    }

    @Override
    public int getCount() {// 返回数量
        return size;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(listViews.get(position % size));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {
            container.addView(listViews.get(position % size), 0);
        } catch (Exception e) {
            LogUtils.e("exception：" + e.getMessage());
        }
        return listViews.get(position % size);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
