package dinson.customview.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.qiniu.common.Zone
import com.qiniu.storage.BucketManager
import com.qiniu.storage.Configuration
import com.qiniu.storage.UploadManager
import com.qiniu.storage.model.FileInfo
import com.qiniu.util.Auth
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview._global.ConstantsUtils
import dinson.customview.adapter.DriverFeedAdapter
import dinson.customview.adapter._005MainPicAdapter
import dinson.customview.kotlin.debug
import dinson.customview.kotlin.error
import dinson.customview.model.SizeModel
import dinson.customview.model._005FileInfo
import dinson.customview.utils.QiNiuUtils
import dinson.customview.utils.SystemBarModeUtils
import dinson.customview.utils.UIUtils
import dinson.customview.weight.refreshview.CustomRefreshView
import kotlinx.android.synthetic.main.activity__005_qi_niu_yun.*
import java.io.File
import kotlin.concurrent.thread


class _005QiNiuYunActivity : BaseActivity() {

    companion object {
        val AccessKey = "Pxox-Sf-S5bz21TEX6gB6d9x7H05hZLkJtTMJI3y"
        val SecretKey = "CGBwKTzZTcaGcD0NZFocIrhKG5uJNL1yqEwYXdvN"
        val domain = "http://p2c0m2mi6.bkt.clouddn.com/"
    }

    private val mData = ArrayList<_005FileInfo>()
    private val mSizeData = ArrayList<SizeModel>()
    //    private lateinit var mAdapter: _005MainPicAdapter
    private lateinit var mAdapter: DriverFeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__005_qi_niu_yun)

        initView()
    }

    private fun initView() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        mAdapter = _005MainPicAdapter(this, mData, domain)
        mAdapter = DriverFeedAdapter(this, mData,mSizeData)

        flCustomRefreshView.setAdapter(mAdapter)
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        //manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        flCustomRefreshView.recyclerView.apply {
            layoutManager = manager
            overScrollMode = View.OVER_SCROLL_NEVER
            // addItemDecoration(StaggeredItemDecoration(this@_005QiNiuYunActivity))
            /*addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    manager.invalidateSpanAssignments()
                }
            })*/
        }
        flCustomRefreshView.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getDataFromServer()
            }

            override fun onLoadMore() {
                getDataFromServer()
            }
        })
        flCustomRefreshView.isRefreshing = true
        flCustomRefreshView.setEmptyView("")
    }

    /**
     * 获取数据
     */
    private fun getDataFromServer() {
        val auth = Auth.create(AccessKey, SecretKey)
        //实例化一个BucketManager对象
        val configuration = Configuration(Zone.zone2())
        val bucketManager = BucketManager(auth, configuration)
        //要列举文件的空间名
        val bucket = "dinson-blog"

        //文件名前缀
        val prefix = ""
        //每次迭代的长度限制，最大1000，推荐值 1000
        val limit = 1000
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        val delimiter = ""
        error("1.onNext: -" + Thread.currentThread().name)
        /*Observable.just(bucket)
            .flatMap {
                error("2.onNext: -" + Thread.currentThread().name)
                val iterator = bucketManager.createFileListIterator(bucket, prefix, limit, delimiter)
                //mData.clear()
                while (iterator.hasNext()) {
                    iterator.next().forEach {
                        debug(it.toString())
                        mData.add(it)
                    }
                }
                Observable.just("eee")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.toast()
                error("3.onNext: -" + Thread.currentThread().name)
                mAdapter.notifyDataSetChanged()
                flCustomRefreshView.complete()
            }, {
                it.printStackTrace()
                flCustomRefreshView.complete()
            })*/
        error("1.onNext: -" + Thread.currentThread().name)
        thread {
            error("2.onNext: -" + Thread.currentThread().name)
            val iterator = bucketManager.createFileListIterator(bucket, prefix, limit, delimiter)
            while (iterator.hasNext()) {
                val next = iterator.next()
                next.forEach {
                    debug(it.toString())
                    mData.add(_005FileInfo(it))

                    val sizeModel = SizeModel()
                    sizeModel.url = it.key
                    mSizeData.add(sizeModel)

                }
            }
            UIUtils.runOnUIThread {
                error("3.onNext: -" + Thread.currentThread().name)
                mAdapter.notifyDataSetChanged()
                flCustomRefreshView.complete()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu._005_qi_niu_toolbar, menu)

        menu?.apply {
            add(Menu.NONE, R.id.tvLike, 0, R.string.des_bg)
                .setEnabled(true)
                .setIcon(R.drawable._001_action_search)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        viewClick@ when (item?.itemId) {
            R.id.action_add_repository -> {

            }
            else -> {
            }
        }
        return true
    }

    private fun uploadImg2QiNiu() {
        /* val config = Configuration.Builder()
             .zone(Zone.autoZone())
             .build()*/
        val configuration = Configuration(Zone.autoZone())
        val uploadManager = UploadManager(configuration)
        // 设置图片名字
        val key = "icons_wireless.png"
//        val picPath = getOutputMediaFile().toString()
        val picPath = ConstantsUtils.SDCARD_ROOT + "wireless.png"
        debug("picPath: $picPath fileIsExists: ${File(picPath).exists()}")

        val bucket = "ijkplayer"

        val token = QiNiuUtils.create(AccessKey, SecretKey).uploadToken(bucket)//生成token

        thread {
            val put = uploadManager.put(picPath, key, token)
            if (put.isOK) {
                debug("token=${QiNiuUtils.create(AccessKey, SecretKey).uploadToken("photo")}")
                debug("complete: http://p2c0m2mi6.bkt.clouddn.com/$key")
            } else {
                error("info is not ok")
            }
        }
    }
}