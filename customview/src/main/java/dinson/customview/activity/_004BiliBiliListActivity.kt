package dinson.customview.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._004VideoListAdapter
import dinson.customview.model._004VideoBean
import dinson.customview.weight.recycleview.LinearItemDecoration
import dinson.customview.weight.recycleview.OnItemClickListener
import kotlinx.android.synthetic.main.activity__004_bili_bili_list.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class _004BiliBiliListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__004_bili_bili_list)

        val mData = getFileFromAssets()
        rvContent.layoutManager = LinearLayoutManager(this)
        rvContent.addItemDecoration(LinearItemDecoration(this))
        rvContent.adapter = _004VideoListAdapter(this, mData)
        rvContent.addOnItemTouchListener(OnItemClickListener(this, rvContent,
            OnItemClickListener.OnClickListener { _, position ->
                _004BiliBiliVideoActivity.start(this, mData[position].title, mData[position].url)
            }))
    }

    /**
     * 获取文本
     *
     */
    private fun getFileFromAssets(): ArrayList<_004VideoBean> {
        val bean = ArrayList<_004VideoBean>()
        val reader = InputStreamReader(resources.assets.open("cntv.txt"))
        val br = BufferedReader(reader)
        var line = br.readLine()
        while (line != null) {
            val split = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            bean.add(_004VideoBean(split[0], split[1]))
            line = br.readLine()
        }
        return bean
    }

}
