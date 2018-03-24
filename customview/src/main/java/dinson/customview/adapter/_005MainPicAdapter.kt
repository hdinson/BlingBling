package dinson.customview.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.bumptech.glide.request.transition.Transition
import com.qiniu.storage.model.FileInfo
import dinson.customview.R
import dinson.customview.kotlin.screenWidth
import dinson.customview.model._005FileInfo
import dinson.customview.utils.GlideUtils
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder


/**
 *玩安卓列表适配器
 */
class _005MainPicAdapter(context: Context, dataList: List<_005FileInfo>, private val mBaseUrl: String)
    : CommonAdapter<_005FileInfo>(context, dataList) {

    //private val imageHeightMap = LinkedHashMap<Int, Int>()

    override fun getLayoutId(viewType: Int) = R.layout.item_005_main_pic

    override fun convert(holder: CommonViewHolder, dataBean: _005FileInfo, position: Int) {
        val imageView = holder.getView<ImageView>(R.id.ivImg)
        if (dataBean.mimeType.contains("image")) {

            if (!dataBean.isNull) {
                setCardViewLayoutParams(imageView, dataBean.width, dataBean.height)
            }

            Glide.with(mContext).asBitmap()
                .load(mBaseUrl + dataBean.key)
                .apply(GlideUtils.getDefaultOptions()
                    .override(imageView.width, 100))
                .into(DriverViewTarget(dataBean, imageView))
        } else {
            Glide.with(mContext).load(R.mipmap.ic_launcher)
                .apply(GlideUtils.getDefaultOptions())
                .into(holder.getView(R.id.ivImg))
        }
    }

    private inner class DriverViewTarget(val data: _005FileInfo, val ivImg: ImageView) : BitmapImageViewTarget(ivImg) {

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