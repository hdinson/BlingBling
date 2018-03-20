package dinson.customview.adapter

import android.content.Context
import android.widget.CheckBox
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import dinson.customview.R
import dinson.customview.activity._001WanAndroidWebActivity
import dinson.customview.api.WanAndroidApi
import dinson.customview.entity.wanandroid.WanAndArticle
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.*
import dinson.customview.utils.DateUtils
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder
import java.util.concurrent.TimeUnit

/**
 *玩安卓 "我的喜欢" 列表适配器
 */
class _001WanAndroidLikeListAdapter(context: Context, dataList: List<WanAndArticle>)
    : CommonAdapter<WanAndArticle>(context, dataList) {

    private val mWanAndroidApi = HttpHelper.create(WanAndroidApi::class.java)

    override fun getLayoutId(viewType: Int) = R.layout.item_001_wan_android_main

    override fun convert(holder: CommonViewHolder, dataBean: WanAndArticle, position: Int) {
        holder.setTvText(R.id.tvTitle, dataBean.title)

        val chapterStr = if (dataBean.superChapterName == null) dataBean.chapterName
        else "${dataBean.superChapterName}/${dataBean.chapterName}"
        holder.setTvText(R.id.tvChapter, chapterStr)

        val tag = holder.getView<TextView>(R.id.tvTag)
        if (dataBean.tags != null && dataBean.tags.isNotEmpty()) {
            tag.show()
            tag.text = dataBean.tags[0].name
        } else {
            tag.hide(true)
        }

        holder.setTvText(R.id.tvTime, DateUtils.long2Str(dataBean.publishTime, "MM/dd HH:mm"))

        val likeView = holder.getView<CheckBox>(R.id.likeView)
        likeView.isChecked = true
        RxView.clicks(likeView).throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                verbose(likeView.isChecked then "添加收藏" ?: "取消收藏")
                post2Server(dataBean, position)
            }

        holder.rootView.click {
            _001WanAndroidWebActivity.start(mContext, dataBean.link)
        }
    }

    private fun post2Server(dataBean: WanAndArticle, position: Int) {
        mWanAndroidApi.delCollectFromCollectList(dataBean.id).compose(RxSchedulers.io_main())
            .subscribe({
                mDataList.remove(dataBean)
                notifyItemRemoved(position)
                when (mDataList.size) {
                    0 -> notifyDataSetChanged()
                    position -> Unit
                    else -> notifyItemRangeChanged(position, mDataList.size - position)
                }
            }, {
                error(it.toString())
            })
    }
}
