package dinson.customview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dinson.customview.entity.MonsterHunter.DataBean.MonsterBean;

/**
 * @author Dinson - 2017/7/21
 */
/*public class _004RightAdapter extends CommonAdapter<MonsterBean> {
    public _004RightAdapter(Context context, List<MonsterHunter.DataBean.MonsterBean> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return viewType == 0 ? R.layout.item_004_right_title : R.layout.item_004_right_normal;
    }

    @Override
    public int getItemViewType(int position) {
        return  mDataList.get(position).isTitle()? 0 : 1;
    }

    @Override
    public void convert(CommonViewHolder holder, MonsterBean monsterBean, int position) {

        switch (getItemViewType(position)) {
            case 0:
                holder.setTvText(R.id.tv_title,monsterBean.getFamily());
                break;
            case 1:
                GlideUtils.setImage(mContext, monsterBean.getIcon(), (ImageView) holder.getView(R.id.iv_img));
                holder.setTvText(R.id.tv_name,monsterBean.getName());
                break;
        }
    }
}*/

public class _004RightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final ArrayList<MonsterBean> mMonsterBeens;
    private final Context mContext;

    private static final int TYPE_TITLE = 598;
    private static final int TYPE_NORMAL = 214;


    public _004RightAdapter(Context context, ArrayList<MonsterBean> datas) {
        mContext = context;
        mMonsterBeens = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_TITLE){
            // TODO: 2017/7/22  
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mMonsterBeens.size() == 0 ? 0 : mMonsterBeens.size();
    }

    private class TitleHolder extends RecyclerView.ViewHolder {
        public TitleHolder(View itemView) {
            super(itemView);
        }
    }
}
