package dinson.customview.holder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dinson.blingbase.widget.banner.holder.BannerViewHolder
import dinson.customview.R
import dinson.customview.entity.countdown.IDailyNews
import dinson.customview.utils.GlideUtils


class _025DailyTodayViewHolder : BannerViewHolder<IDailyNews> {
    private var mImageView: ImageView? = null
    private var mDescView: TextView? = null

    override fun createView(context: Context): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_025_daily_banner, null)
        mImageView = view.findViewById(R.id.ivDaily)
        mDescView = view.findViewById(R.id.tvDailyDesc)
        return view
    }

    override fun onBind(context: Context, position: Int, data: IDailyNews) {
        GlideUtils.setImage(context, data.cover_thumb, mImageView)
        mDescView?.text = data.content
    }
}