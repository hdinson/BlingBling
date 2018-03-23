package dinson.customview.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.qiniu.storage.model.FileInfo
import dinson.customview.R
import dinson.customview.utils.GlideUtils
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder

/**
 *玩安卓列表适配器
 */
class _005MainPicAdapter(context: Context, dataList: List<FileInfo>, val mBaseUrl: String)
    : CommonAdapter<FileInfo>(context, dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_005_main_pic

    override fun convert(holder: CommonViewHolder, dataBean: FileInfo, position: Int) {

        if (dataBean.mimeType.contains("image")) {
            GlideUtils.setImage(mContext, mBaseUrl + dataBean.key, holder.getView(R.id.ivImg))
        } else {
            Glide.with(mContext).load(R.mipmap.ic_launcher).into(holder.getView(R.id.ivImg))
        }
    }
}