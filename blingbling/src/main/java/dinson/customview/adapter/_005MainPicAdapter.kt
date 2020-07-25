package dinson.customview.adapter

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import dinson.customview.R
import dinson.customview.model.QiNiuFileInfo
import dinson.customview.utils.GlideUtils
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_005_main_pic.view.*


/**
 *玩安卓列表适配器
 */
class _005MainPicAdapter(dataList: MutableList<QiNiuFileInfo>, private val mBaseUrl: String)
    : CommonAdapter<QiNiuFileInfo>(dataList) {

    //private val imageHeightMap = LinkedHashMap<Int, Int>()

    override fun getLayoutId(viewType: Int) = R.layout.item_005_main_pic

    override fun convert(holder: CommonViewHolder, dataBean: QiNiuFileInfo, position: Int) {


        if (dataBean.mimeType.contains("image")) {

            if (!dataBean.isNull) {
                setCardViewLayoutParams(holder.itemView.ivImg, dataBean.width, dataBean.height)
            }

            Glide.with(holder.itemView.context).asBitmap()
                .load(mBaseUrl + dataBean.key)
                .apply(GlideUtils.getDefaultOptions()
                    .override(holder.itemView.ivImg.width, 100))
                .into(DriverViewTarget(dataBean, holder.itemView.ivImg))
        } else {
            Glide.with(holder.itemView).load(R.mipmap.ic_launcher)
                .apply(GlideUtils.getDefaultOptions())
                .into(holder.itemView.ivImg)
        }
    }

    private inner class DriverViewTarget(val data: QiNiuFileInfo, val ivImg: ImageView) : BitmapImageViewTarget(ivImg) {

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

            if (data.isNull) {
                val viewWidth = ivImg.width
                val scale = resource.width / (viewWidth * 1.0f)
                val viewHeight = (resource.height * scale).toInt()
                setCardViewLayoutParams(ivImg, viewWidth, viewHeight)
                data.setSize(viewWidth, viewHeight)
            }


            super.onResourceReady(resource, transition)
        }


    }

    private fun setCardViewLayoutParams(view: View, viewWidth: Int, viewHeight: Int) {
        val params = view.layoutParams
        params.height = viewHeight
        params.width = viewWidth
        view.layoutParams = params
    }
}