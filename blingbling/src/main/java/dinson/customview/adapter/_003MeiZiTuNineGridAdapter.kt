package dinson.customview.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.View
import com.dinson.blingbase.utils.TypefaceUtil
import dinson.customview.R
import dinson.customview.entity.gank.MeiZiTuNineGrid


import dinson.customview.weight.MessagePicturesLayout
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import dinson.customview._global.ConstantsUtils.APP_FONT_PATH
import kotlinx.android.synthetic.main.item_003_meizitu_nine_grid.view.*


/**
 * 妹子图精选，最新，最热，图集样式的适配器
 */
class _003MeiZiTuNineGridAdapter(dataList: MutableList<MeiZiTuNineGrid>)
    : CommonAdapter<MeiZiTuNineGrid>(dataList) {

    private var mCallback: MessagePicturesLayout.Callback? = null
    private var mTypeFace: Typeface? = null

    override fun getLayoutId(viewType: Int) = R.layout.item_003_meizitu_nine_grid

    @SuppressLint("SetTextI18n")
    override fun convert(holder: CommonViewHolder, dataBean: MeiZiTuNineGrid, position: Int) {
        if (mTypeFace == null) {
            mTypeFace = TypefaceUtil.getFontFromAssets(holder.itemView.context, APP_FONT_PATH)
        }
        holder.itemView.tvYear.visibility = if (dataBean.isShowYear) View.VISIBLE else View.GONE
        holder.itemView.tvYear.typeface = mTypeFace
        holder.itemView.tvMonth.typeface = mTypeFace
        holder.itemView.tvDay.typeface = mTypeFace
        val split = dataBean.time.split("-")
        if (split.size == 3) {
            holder.itemView.tvYear.text = "${split[0]}年"
            holder.itemView.tvMonth.text = "${split[1]}月"
            holder.itemView.tvDay.text = split[2]
        } else {
            holder.itemView.tvYear.visibility = View.GONE
            holder.itemView.tvMonth.visibility = View.GONE
            holder.itemView.tvDay.text = split[0]
        }

        holder.itemView.mpGirlsPic.apply {
            set(dataBean.pictureThumbList, dataBean.pictureList)
            setCallback(mCallback)
        }
    }

    fun setPictureClickCallback(callback: MessagePicturesLayout.Callback) {
        mCallback = callback
    }
}
