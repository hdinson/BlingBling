package dinson.customview.adapter

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import dinson.customview.BuildConfig
import dinson.customview.R
import dinson.customview.entity.ClassBean
import dinson.customview.listener.OnItemMoveListener
import dinson.customview.listener.OnItemTouchMoveListener
import dinson.customview.utils.GlideUtils
import kotlinx.android.synthetic.main.item_main_content.view.*
import java.util.*

/**
 * 主页适配器
 */
class MainContentAdapter(val mDataList: MutableList<ClassBean>, private val mMoveListener
: OnItemTouchMoveListener) : CommonAdapter<ClassBean>(mDataList), OnItemMoveListener {

    private val mNumberImg = intArrayOf(R.drawable.n_0, R.drawable.n_1, R.drawable.n_2,
        R.drawable.n_3, R.drawable.n_4, R.drawable.n_5, R.drawable.n_6,
        R.drawable.n_7, R.drawable.n_8, R.drawable.n_9)

    override fun getLayoutId(viewType: Int) = R.layout.item_main_content

    override fun convert(holder: CommonViewHolder, classBean: ClassBean, position: Int) {
        holder.itemView.tvTitle.text = classBean.title
        holder.itemView.tvDesc.text = classBean.desc

        val ints = formatPosition(position + 1)
        GlideUtils.setCircleImage(holder.itemView.context, "${BuildConfig.QINIU_URL}${classBean.img}",
            holder.itemView.ivImg)
        holder.itemView.ivImg.setOnTouchListener(ViewHolderTouchListener(holder))

        if (ints[0] == 0) {
            holder.itemView.ivOne.visibility = View.INVISIBLE
        } else {
            holder.itemView.ivOne.visibility = View.VISIBLE
            holder.itemView.ivOne.setImageResource(mNumberImg[ints[0]])
        }
        holder.itemView.ivSecond.setImageResource(mNumberImg[ints[1]])
        holder.itemView.ivThird.setImageResource(mNumberImg[ints[2]])
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
