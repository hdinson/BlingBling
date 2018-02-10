package dinson.customview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import dinson.customview.R
import dinson.customview.R.id.iv_one
import dinson.customview.entity.ClassBean
import dinson.customview.listener.OnItemMoveListener
import dinson.customview.listener.OnItemTouchMoveListener
import dinson.customview.utils.GlideUtils
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder
import java.util.*

/**
 * 主页适配器
 */
class MainContentAdapter(context: Context, dataList: List<ClassBean>, private val mMoveListener
: OnItemTouchMoveListener) : CommonAdapter<ClassBean>(context, dataList), OnItemMoveListener {

    private val mNumberImg = intArrayOf(R.drawable.n_0, R.drawable.n_1, R.drawable.n_2,
        R.drawable.n_3, R.drawable.n_4, R.drawable.n_5, R.drawable.n_6,
        R.drawable.n_7, R.drawable.n_8, R.drawable.n_9)

    override fun getLayoutId(viewType: Int) = R.layout.item_main_content

    override fun convert(holder: CommonViewHolder, classBean: ClassBean, position: Int) {
        holder.setTvText(R.id.tvTitle, classBean.title)
        holder.setTvText(R.id.tvDesc, classBean.desc)
        val ints = formatPosition(position + 1)
        val ivImg = holder.getView<ImageView>(R.id.ivImg)
        GlideUtils.setCircleImage(mContext, classBean.imgUrl, ivImg)
        ivImg.setOnTouchListener(ViewHolderTouchListener(holder))
        holder.getView<View>(iv_one).visibility = if (ints[0] == 0) View.INVISIBLE else View.VISIBLE
        if (ints[0] != 0) holder.setIvSrc(iv_one, mNumberImg[ints[0]])
        holder.setIvSrc(R.id.iv_second, mNumberImg[ints[1]])
        holder.setIvSrc(R.id.iv_third, mNumberImg[ints[2]])
    }


    private fun formatPosition(pos: Int): IntArray {
        if (pos < 10) {
            return intArrayOf(0, 0, pos)
        }
        if (pos in 10..99) {
            val second = pos / 10
            val first = pos % 10
            return intArrayOf(0, second, first)
        }
        val third = pos / 100
        val second = pos % 100 / 10
        val first = pos % 100 % 10
        return intArrayOf(third, second, first)
    }


    private inner class ViewHolderTouchListener constructor(private val mHolder: RecyclerView.ViewHolder)
        : View.OnTouchListener {

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            v.performClick()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mMoveListener.onItemTouchMove(mHolder)
                    return true
                }
            }
            return false
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(mDataList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemSwipe2Remove(position: Int) {
        mDataList.removeAt(position)
        notifyItemRemoved(position)
    }
}
