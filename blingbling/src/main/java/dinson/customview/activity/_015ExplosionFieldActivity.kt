package dinson.customview.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._015AppIconAdapter
import dinson.customview.model._015AppIcon
import dinson.customview.weight._015explosionview.ExplosionField
import dinson.customview.weight.recycleview.OnRvItemClickListener
import dinson.customview.weight.recycleview.RvItemClickSupport
import kotlinx.android.synthetic.main.activity__015_explosion_field.*


class _015ExplosionFieldActivity : BaseActivity() {

    private lateinit var mExplosionField: ExplosionField

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__015_explosion_field)
        mExplosionField = ExplosionField.attach2Window(this)

        rvContent.layoutManager = GridLayoutManager(this, 4)
        val mDataList = getAppIconData()
        val mAdapter = _015AppIconAdapter(mDataList)
        rvContent.adapter = mAdapter
        RvItemClickSupport.addTo(rvContent)
            .setOnItemClickListener(OnRvItemClickListener { _, view, position ->
                if (position<0)return@OnRvItemClickListener
                mExplosionField.explode(view)
                mDataList.removeAt(position)
                mAdapter.notifyItemRemoved(position)

                if (mDataList.isEmpty()) {
                    mDataList.addAll(getAppIconData())
                    mAdapter.notifyItemRangeInserted(0, mDataList.size)
                }
            })
        tvDesc.setOnClickListener {
            mExplosionField.explode(it)
            it.setOnClickListener(null)
        }
    }

    private fun getAppIconData() = arrayListOf(
        _015AppIcon(R.drawable._015_img1, "Telephone"),
        _015AppIcon(R.drawable._015_img2, "Contact"),
        _015AppIcon(R.drawable._015_img3, "Browser"),
        _015AppIcon(R.drawable._015_img4, "Information"),
        _015AppIcon(R.drawable._015_img5, "Camera"),
        _015AppIcon(R.drawable._015_img6, "Theme Style"),
        _015AppIcon(R.drawable._015_img7, "File"),
        _015AppIcon(R.drawable._015_img8, "Accelerate"),
        _015AppIcon(R.drawable._015_img9, "Time"),
        _015AppIcon(R.drawable._015_img10, "Map"),
        _015AppIcon(R.drawable._015_img11, "Mail"),
        _015AppIcon(R.drawable._015_img12, "Video"),
        _015AppIcon(R.drawable._015_img13, "Weather"),
        _015AppIcon(R.drawable._015_img14, "Radio"),
        _015AppIcon(R.drawable._015_img15, "Music"),
        _015AppIcon(R.drawable._015_img16, "Set up"),
        _015AppIcon(R.drawable._015_img17, "Calendar"),
        _015AppIcon(R.drawable._015_img18, "Calculator"),
        _015AppIcon(R.drawable._015_img19, "AppStore")
    )

    /*private fun addListener(root: View) {
        if (root is ViewGroup) {
            for (i in 0 until root.childCount) {
                addListener(root.getChildAt(i))
            }
        } else {
            root.isClickable = true
            root.setOnClickListener {
                mExplosionField.explode(it)
                it.setOnClickListener(null)
            }
        }
    }*/
}
