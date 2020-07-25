package dinson.customview.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.CheckBox
import com.bumptech.glide.Glide
import dinson.customview.R
import dinson.customview.entity.bmob.TvShowResp
import dinson.customview.event._004CheckSelectorAllEvent
import dinson.customview.kotlin.logi
import dinson.customview.utils.StringUtils
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_004_video.view.*
import org.greenrobot.eventbus.EventBus

/**
 * 视频数据适配器
 */
class _004VideoListAdapter(dataList: MutableList<TvShowResp.TvShow>, isEditMode: Boolean)
    : CommonAdapter<TvShowResp.TvShow>(dataList), View.OnClickListener {

    override fun onClick(v: View) {
        val tag = v.tag as Int
        mSelectorList[tag] = (v as CheckBox).isChecked
        val checkIsSelectorAll = checkIsSelectorAll()
        EventBus.getDefault().post(_004CheckSelectorAllEvent(!checkIsSelectorAll,
            checkSelectorCount()))
    }

    private var mIsEditMode = false
    private val mSelectorList = ArrayList<Boolean>()
    private val mDeleteList = ArrayList<TvShowResp.TvShow>()

    init {
        mIsEditMode = isEditMode
    }

    override fun getLayoutId(viewType: Int) = R.layout.item_004_video

    @SuppressLint("SetTextI18n")
    override fun convert(holder: CommonViewHolder, bean: TvShowResp.TvShow, position: Int) {
        holder.itemView.cbVideo.apply {
            tag = position
            if (mIsEditMode) {
                visibility = View.VISIBLE
                isChecked = mSelectorList[position]
            } else {
                visibility = View.GONE
            }
            visibility = if (mIsEditMode) View.VISIBLE else View.GONE
            setOnClickListener(this@_004VideoListAdapter)
        }
        holder.itemView.tvTitle.text = bean.tvName
        holder.itemView.tvSubTitle.text = "更新时间: ${bean.updatedAt.split(" ")[0]}"
        if (StringUtils.isNotEmpty(bean.tvIcon)) {
            Glide.with(holder.itemView.context).load(bean.tvIcon)
                .into(holder.itemView.ivVideoIcon)
        }
    }

    fun toggleEditMode() {
        mIsEditMode = !mIsEditMode
        if (mIsEditMode) {
            initSelectorList()
        }
        notifyDataSetChanged()
    }

    private fun initSelectorList() {
        mSelectorList.clear()
        mDataList.indices.forEach { _ ->
            mSelectorList.add(false)
        }
    }

    private fun checkIsSelectorAll(): Boolean {
        val isSameTheFirst = mSelectorList.filterIndexed { index, _ -> index != 0 }
            .find { it != mSelectorList[0] }
        //当isSameTheFirst==null时表示全部跟第一个选中当一样，true或者false表示部分选中
        return if (isSameTheFirst == null) !mSelectorList[0] else true
    }

    private fun checkSelectorCount(): Int {
        return mSelectorList.count { it }
    }


    /**
     * 全选
     */
    fun selectorAll() {
        if (mSelectorList.isEmpty()) return
        val setValue = checkIsSelectorAll()
        (0 until mSelectorList.size).forEach {
            mSelectorList[it] = setValue
        }
        EventBus.getDefault().post(_004CheckSelectorAllEvent(setValue,
            if (setValue) mSelectorList.size else 0))
        notifyDataSetChanged()
    }

    /**
     * 删除选中
     * @return 删除了几个元素
     */
    fun deleteSelector(): Int {
        val before = mDeleteList.size

        var i = 0
        while (i < mSelectorList.size) {
            if (mSelectorList[i]) {
                mSelectorList.removeAt(i)
                mDeleteList.add(mDataList.removeAt(i))
                i--
            }
            i++
        }

        if (before == mDeleteList.size) return 0
        EventBus.getDefault().post(_004CheckSelectorAllEvent(false, 0))
        notifyDataSetChanged()
        logi { mDeleteList.joinToString(",") { it.tvName } }
        return mDeleteList.size - before
    }
}