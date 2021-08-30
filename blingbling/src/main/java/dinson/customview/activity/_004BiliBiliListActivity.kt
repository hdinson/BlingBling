package dinson.customview.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.View.OVER_SCROLL_NEVER
import com.dinson.blingbase.annotate.BindEventBus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trello.rxlifecycle2.android.ActivityEvent
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._004VideoListAdapter
import dinson.customview.api.BmobApi
import dinson.customview.entity.bmob.TvShowResp
import dinson.customview.event._004CheckSelectorAllEvent
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import com.dinson.blingbase.kotlin.click
import com.dinson.blingbase.kotlin.dip
import dinson.customview.kotlin.loge
import dinson.customview.kotlin.logi
import dinson.customview.utils.toast
import com.dinson.blingbase.utils.SystemBarModeUtils
import dinson.customview.utils.CacheUtils

import com.dinson.blingbase.widget.recycleview.LinearSpaceDecoration

import com.dinson.blingbase.widget.recycleview.RvItemClickSupport
import dinson.customview.widget.refreshview.CustomRefreshView
import kotlinx.android.synthetic.main.activity__004_bili_bili_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class _004BiliBiliListActivity : BaseActivity() {

    private val mDataList = ArrayList<TvShowResp.TvShow>()
    private lateinit var mAdapter: _004VideoListAdapter
    private var mIsEditMode = false //默认不是编辑模式

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__004_bili_bili_list)

        initUI()
        initData()
    }

    private fun initUI() {
        SystemBarModeUtils.setPaddingTop(this, topTitleLayout)
        actionBack.click { onBackPressed() }
        actionMode.click {
            if (mIsEditMode) {
                AlertDialog.Builder(this)
                    .setMessage("取消后将失去之前所有的操作")
                    .setNegativeButton("取消") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("确定") { dialog, _ ->
                        dialog.dismiss()
                        toggleEditMode()
                        initData()
                    }.create().show()
            } else {
                toggleEditMode()
            }
        }
        mAdapter = _004VideoListAdapter(mDataList, mIsEditMode)
        crvVideoContent.setAdapter(mAdapter)
        crvVideoContent.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getServiceData()
            }

            override fun onLoadMore() {}
        })
        RvItemClickSupport.addTo(crvVideoContent.recyclerView)
            .setOnItemClickListener { _, _, position ->
                _004BiliBiliVideoActivity.start(this, mDataList[position].tvName,
                    mDataList[position].tvUrl)
            }
        crvVideoContent.loadMoreEnable = false
        crvVideoContent.setEmptyView("")
        crvVideoContent.recyclerView.overScrollMode = OVER_SCROLL_NEVER
        crvVideoContent.recyclerView.addItemDecoration(
            LinearSpaceDecoration.Builder()
            .spaceTB(dip(1)).build())
        btnSelectorAll.click { mAdapter.selectorAll() }
        btnDelete.click {
            val count = mAdapter.deleteSelector()
            logi { "删除了 $count 个" }
        }
    }

    private val mApi by lazy { HttpHelper.create(BmobApi::class.java) }

    private fun initData() {
        val cache = CacheUtils.getCache(this, "_004VideoList")
        if (cache?.isNotEmpty() == true) {
            //加载缓存数据
            val type = object : TypeToken<ArrayList<TvShowResp.TvShow>>() {}.type
            val tempDataList =
                Gson().fromJson<ArrayList<TvShowResp.TvShow>>(cache, type)
            mDataList.clear()
            mDataList.addAll(tempDataList)
            mAdapter.notifyDataSetChanged()
        } else {
            crvVideoContent.isRefreshing = true
        }
    }

    private fun getServiceData() {
        mApi.getVideoList()
            .compose(RxSchedulers.io_main())
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe({
                if (it.code == 0) {
                    mDataList.clear()
                    mDataList.addAll(it.results)
                    CacheUtils.setCache(this, "_004VideoList",
                        Gson().toJson(mDataList))
                    mAdapter.notifyDataSetChanged()
                }
            }, {
                crvVideoContent.complete()
                it.message?.toast()
                loge(it::toString)
            }, {
                crvVideoContent.complete()
            }).addToManaged()
    }

    private fun toggleEditMode() {
        mIsEditMode = !mIsEditMode
        llBottomBtn.visibility = if (mIsEditMode) View.VISIBLE else View.GONE
        actionPush.visibility = if (mIsEditMode) View.VISIBLE else View.GONE
        actionMode.text = if (mIsEditMode) "取消" else "编辑"
        crvVideoContent.refreshEnable = !mIsEditMode
        mAdapter.toggleEditMode()
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: _004CheckSelectorAllEvent) {
        tvSelectorCount.text = "已选择  ${event.selectorCount}"
        btnSelectorAll.text = if (event.isSelectAll) "取消全选" else "全选"
    }

    override fun onBackPressed() {
        if (mIsEditMode) {
            AlertDialog.Builder(this)
                .setMessage("您确定要返回？返回将失去之前所有的操作")
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("确定") { dialog, _ ->
                    dialog.dismiss()
                    super.onBackPressed()
                }.create().show()
        } else {
            super.onBackPressed()
        }
    }
}