package dinson.customview.activity


import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.Gson
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._019LeftAdapter
import dinson.customview.adapter._019RightAdapter
import dinson.customview.entity.MonsterHunter
import dinson.customview.entity.MonsterHunter.DataBean.MonsterBean
import dinson.customview.utils.FileUtils
import dinson.customview.utils.GlideUtils
import dinson.customview.utils.StringUtils
import dinson.customview.weight.recycleview.LinearItemDecoration
import kotlinx.android.synthetic.main.activity__019_ganged_recycle_view.*
import java.util.*

class _019GangedRecycleViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__019_ganged_recycle_view)

        val url = "http://ondlsj2sn.bkt.clouddn.com/FpUVRI5ACAX7YkTOXLuD7mP_3BPg.webp"
        GlideUtils.setImage(this, url, ivImg)

        initUI()
    }

    private fun initUI() {
        val textFromAssets = FileUtils.getTextFromAssets(this, "mh.json")
        val datas = Gson().fromJson(textFromAssets, MonsterHunter::class.java)

        val leftAdapter = _019LeftAdapter(this, datas.data)

        //添加标题数据
        val rightDatas = ArrayList<MonsterBean>()
        var currentType = ""
        for (i in 0 until datas.data.size) {
            if (!StringUtils.equals(currentType, datas.data[i].family)) {
                currentType = datas.data[i].family
                rightDatas.add(MonsterBean(currentType, true))
            }
            rightDatas.addAll(datas.data[i].monster)
        }
        val rightAdapter = _019RightAdapter(this, rightDatas)


        rvLeft.layoutManager = LinearLayoutManager(this)
        val gridManager = GridLayoutManager(this, 3)
        rvRight.layoutManager = gridManager
        gridManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                if (rightDatas[position].isTitle) 3 else 1
        }

        rvLeft.adapter = leftAdapter
        rvRight.adapter = rightAdapter
        rvLeft.addItemDecoration(LinearItemDecoration(this))
    }

    override fun setWindowBackgroundColor() = android.R.color.white

}
