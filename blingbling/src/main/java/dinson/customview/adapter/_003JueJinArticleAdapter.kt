package dinson.customview.adapter

import android.annotation.SuppressLint
import android.view.View
import dinson.customview.R
import dinson.customview.entity.gank.JueJinResponse
import dinson.customview.utils.DateUtils
import dinson.customview.utils.GlideUtils
import dinson.customview.utils.StringUtils
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_003_juejin_article.view.*


/**
 *掘金文章适配器
 */
class _003JueJinArticleAdapter(dataList: List<JueJinResponse.DBean.EntrylistBean>)
    : CommonAdapter<JueJinResponse.DBean.EntrylistBean>(dataList) {


    override fun getLayoutId(viewType: Int) = R.layout.item_003_juejin_article

    @SuppressLint("SetTextI18n")
    override fun convert(holder: CommonViewHolder, dataBean: JueJinResponse.DBean.EntrylistBean, position: Int) {
        holder.itemView.tvTitle.text = dataBean.title
        holder.itemView.tvContent.text = dataBean.content
        if (StringUtils.isEmpty(dataBean.screenshot)) {
            holder.itemView.ivScreenshot.visibility = View.GONE
        } else {
            holder.itemView.ivScreenshot.visibility = View.VISIBLE
            GlideUtils.setImage(holder.itemView.context, dataBean.screenshot, holder.itemView.ivScreenshot)
        }
        var tags = ""
        if (dataBean.tags != null && dataBean.tags.isNotEmpty()) {
            tags = dataBean.tags.joinToString("/", "  # ") { it.title }
        }
        val intTime = DateUtils.str2int(dataBean.createdAt, "yyyy-MM-dd'T'HH:mm:ss.SSS")
        val timeStr = DateUtils.convertTimeToFormat(intTime)
        holder.itemView.tvTags.text = "$timeStr # ${dataBean.user.username}$tags"
        holder.itemView.tvCollectionCount.text = dataBean.collectionCount
    }
}
