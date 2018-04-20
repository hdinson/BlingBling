package dinson.customview.adapter

import android.content.Context
import android.widget.CheckBox
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import dinson.customview.R
import dinson.customview.activity._001WanAndroidWebActivity
import dinson.customview.entity.wanandroid.WanAndArticle
import dinson.customview.kotlin.click
import dinson.customview.kotlin.hide
import dinson.customview.kotlin.show
import dinson.customview.kotlin.verbose
import dinson.customview.listener._001OnLikeViewClickListener
import dinson.customview.utils.DateUtils
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder
import java.util.concurrent.TimeUnit

/**
 *玩安卓列表适配器
 */
class _001WanAndroidMainListAdapter(context: Context,
                                    dataList: List<WanAndArticle>,
                                    private val likeClickListener: _001OnLikeViewClickListener)
    : CommonAdapter<WanAndArticle>(context, dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_001_wan_android_main

    override fun convert(holder: CommonViewHolder, dataBean: WanAndArticle, position: Int) {
        holder.setTvText(R.id.tvTitle, dataBean.title)
        holder.setTvText(R.id.tvChapter, "${dataBean.superChapterName}/${dataBean.chapterName}")
        val tag = holder.getView<TextView>(R.id.tvTag)
        if (dataBean.tags != null && dataBean.tags.isNotEmpty()) {
            tag.show()
            tag.text = dataBean.tags[0].name
        } else {
            tag.hide(true)
        }
        holder.setTvText(R.id.tvTime, DateUtils.long2Str(dataBean.publishTime, "MM/dd"))
        val likeView = holder.getView<CheckBox>(R.id.likeView)
        likeView.isChecked = dataBean.isCollect

        RxView.clicks(likeView).throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                verbose(if (likeView.isChecked) "添加收藏" else "取消收藏")
                //post2Server(likeView.isChecked, dataBean)
                likeClickListener.onClickLikeView(likeView, dataBean, position)
            }

        holder.rootView.click {
            _001WanAndroidWebActivity.start(mContext, dataBean.link)
        }
    }
}