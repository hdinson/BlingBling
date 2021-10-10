package dinson.customview.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import android.widget.CursorAdapter
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.dinson.blingbase.utils.SystemBarModeUtils
import com.github.promeg.pinyinhelper.Pinyin
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._027VpAdapter
import dinson.customview.db.SearchHistory027DbUtils
import dinson.customview.db.model.SearchHistory027
import dinson.customview.db.model.SearchHistory027Dao
import dinson.customview.model._027AvModel
import kotlinx.android.synthetic.main.activity__027_movie.*


class _027MovieActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__027_movie)

        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)

        val adapter = _027VpAdapter(supportFragmentManager)
        vpContent.offscreenPageLimit = adapter.count
        vpContent.adapter = adapter
        alphaIndicator.setViewPager(vpContent)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu._027_main_toolbar, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        initSearchView(searchItem)
        return super.onCreateOptionsMenu(menu)
    }

    private var mSearchView: SearchView? = null
    private var mAutoCompleteTextView: AutoCompleteTextView? = null


    private fun initSearchView(item: MenuItem?) {
        if (item == null) return
        //通过 item 获取 Action View
        mSearchView = item.actionView as SearchView
        mSearchView?.apply {
            mAutoCompleteTextView = this.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)
            //设置触发查询的最少字符数（默认2个字符才会触发查询）
            mAutoCompleteTextView!!.threshold = 0
            val textView = this.findViewById<TextView>(R.id.search_src_text)
            textView.setTextColor(Color.WHITE)
            textView.setHintTextColor(Color.parseColor("#aaaaaa"))

            mAutoCompleteTextView!!.setOnItemClickListener { _, view, _, _ ->
                val key = view.findViewById<TextView>(R.id.tvHistoryName).text.toString()
                closeSearchView()//关闭SearchView
                if (key.isNotEmpty()) {
                    _027MovieListByLinkActivity.start(this@_027MovieActivity,
                        key, _027AvModel.getSearchUrl(key))
                }
            }
        }

        //搜索监听
        mSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                closeSearchView()//关闭SearchView

                if (query?.isNotEmpty() == true) {
                    val history = SearchHistory027()
                    history.name = query
                    history.pinyin = Pinyin.toPinyin(query, "")
                    SearchHistory027DbUtils.getInstance(this@_027MovieActivity).insertHistory(history)
                    _027MovieListByLinkActivity.start(this@_027MovieActivity,
                        query, _027AvModel.getSearchUrl(query))
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mSearchView?.apply { notifyHistoryDataSet(this, newText) }
                return false
            }
        })
    }

    private fun notifyHistoryDataSet(view: SearchView, key: String?) {
        // 不要频繁创建适配器，如果适配器已经存在，则只需要更新适配器中的cursor对象即可。
        val sql = if (key?.isEmpty() == true) {
            "select * from ${SearchHistory027Dao.TABLENAME} " +
                "order by ${SearchHistory027Dao.Properties.Id.columnName} desc limit 0,20"
        } else "select * from ${SearchHistory027Dao.TABLENAME} " +
            "where ${SearchHistory027Dao.Properties.Pinyin.columnName} " +
            "like '%$key%' order by ${SearchHistory027Dao.Properties.Id.columnName} desc limit 0,20"
        val cursor = SearchHistory027DbUtils.getInstance(this).getReadableDatabase().rawQuery(sql, null)
        if (view.suggestionsAdapter == null) {
            val adapter = SimpleCursorAdapter(this@_027MovieActivity,
                R.layout.item_027_search_history, cursor,
                arrayOf(SearchHistory027Dao.Properties.Name.columnName),
                intArrayOf(R.id.tvHistoryName), CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
            view.suggestionsAdapter = adapter
        } else {
            view.suggestionsAdapter.changeCursor(cursor)
        }
    }


    override fun onPause() {
        super.onPause()
        closeSearchView()
    }

    private fun closeSearchView() {
        if (mSearchView?.isShown == true) {
            try {
                mAutoCompleteTextView?.setText("")
                val method = mSearchView!!.javaClass.getDeclaredMethod("onCloseClicked")
                method.isAccessible = true
                method.invoke(mSearchView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}