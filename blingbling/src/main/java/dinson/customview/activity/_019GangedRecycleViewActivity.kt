package dinson.customview.activity


import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dinson.blingbase.kotlin.dip
import com.dinson.blingbase.utils.FileUtils
import com.dinson.blingbase.utils.StringUtils
import com.google.gson.Gson
import dinson.customview.BuildConfig
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._019LeftAdapter
import dinson.customview.adapter._019RightAdapter
import dinson.customview.entity.MonsterHunter
import dinson.customview.entity.MonsterHunter.DataBean.MonsterBean
import dinson.customview.utils.GlideUtils

import com.dinson.blingbase.widget.recycleview.LinearItemDecoration
import com.dinson.blingbase.widget.recycleview.LinearSpaceDecoration
import kotlinx.android.synthetic.main.activity__019_ganged_recycle_view.*
import java.util.*

class _019GangedRecycleViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__019_ganged_recycle_view)

        val url = "${BuildConfig.QINIU_URL}FpUVRI5ACAX7YkTOXLuD7mP_3BPg.webp"
        GlideUtils.setImage(this, url, ivImg)

        initUI()
    }

    private fun initUI() {
        val textFromAssets = FileUtils.getTextFromAssets(this, "mh.json")
        val datas = Gson().fromJson(textFromAssets, MonsterHunter::class.java)

        val leftAdapter = _019LeftAdapter(datas.data)

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
        val rightAdapter = _019RightAdapter(rightDatas)


        rvLeft.layoutManager = LinearLayoutManager(this)
        val gridManager = GridLayoutManager(this, 3)
        rvRight.layoutManager = gridManager
        gridManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                if (rightDatas[position].isTitle) 3 else 1
        }

        rvLeft.adapter = leftAdapter
        rvRight.adapter = rightAdapter
        rvLeft.addItemDecoration(
            LinearSpaceDecoration.Builder()
            .spaceTB(dip(1)).build())
    }

    override fun setWindowBackgroundColor() = android.R.color.white

}
