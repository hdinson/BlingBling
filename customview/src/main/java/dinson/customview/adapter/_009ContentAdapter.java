package dinson.customview.adapter;

import android.content.Context;

import java.util.List;

import dinson.customview.R;
import dinson.customview.model._009PanoramaImageModel;
import dinson.customview.weight.recycleview.CommonAdapter;
import dinson.customview.weight.recycleview.CommonViewHolder;

/**
 * @author Dinson - 2017/7/21
 */
public class _009ContentAdapter extends CommonAdapter<_009PanoramaImageModel> {


    public _009ContentAdapter(Context context, List<_009PanoramaImageModel> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_009_panorana_content;
    }


    @Override
    public void convert(CommonViewHolder holder, _009PanoramaImageModel monsterBean, int position) {

    }
}
