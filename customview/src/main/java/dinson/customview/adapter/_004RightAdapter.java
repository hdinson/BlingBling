package dinson.customview.adapter;

import android.content.Context;

import java.util.List;

import dinson.customview.R;
import dinson.customview.entity.MonsterHunter;
import dinson.customview.entity.MonsterHunter.DataBean.MonsterBean;
import dinson.customview.utils.GlideUtils;
import dinson.customview.weight.recycleview.CommonAdapter;
import dinson.customview.weight.recycleview.CommonViewHolder;

/**
 * @author Dinson - 2017/7/21
 */
public class _004RightAdapter extends CommonAdapter<MonsterBean> {
    public _004RightAdapter(Context context, List<MonsterHunter.DataBean.MonsterBean> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return viewType == 0 ? R.layout.item_004_right_title : R.layout.item_004_right_normal;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).isTitle() ? 0 : 1;
    }

    @Override
    public void convert(CommonViewHolder holder, MonsterBean monsterBean, int position) {

        switch (getItemViewType(position)) {
            case 0:
                holder.setTvText(R.id.tvTitle, monsterBean.getFamily());
                break;
            case 1:
                GlideUtils.setImage(mContext, monsterBean.getIcon(),   holder.getView(R.id.ivImg));
                holder.setTvText(R.id.tvName, monsterBean.getName());
                break;
        }
    }
}
