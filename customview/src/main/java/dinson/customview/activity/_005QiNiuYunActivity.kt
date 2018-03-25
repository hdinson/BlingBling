package dinson.customview.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.qiniu.common.FixedZone
import com.qiniu.common.Zone
import com.qiniu.storage.BucketManager
import com.qiniu.storage.Configuration
import com.qiniu.storage.UploadManager
import com.qiniu.util.Auth
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview._global.ConstantsUtils
import dinson.customview.adapter.DriverFeedAdapter
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.debug
import dinson.customview.kotlin.error
import dinson.customview.model._005FileInfo
import dinson.customview.model._005QiNiuConfig
import dinson.customview.utils.QiNiuUtils
import dinson.customview.utils.SPUtils
import dinson.customview.utils.SystemBarModeUtils
import dinson.customview.weight.dialog._005DialogQiNiuConfig
import dinson.customview.weight.refreshview.CustomRefreshView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity__005_qi_niu_yun.*
import java.io.File
import javax.crypto.SecretKey
import kotlin.concurrent.thread


class _005QiNiuYunActivity : BaseActivity() {

    private val mListData = ArrayList<_005FileInfo>()
    private val mConfigList = ArrayList<_005QiNiuConfig>()
    private var mCurrentConfig: _005QiNiuConfig? = null

    private lateinit var mAdapter: DriverFeedAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__005_qi_niu_yun)

        initConfig()
        initView()
    }

    /**
     * 初始化七牛云设置
     */
    private fun initConfig() {
        val (arrayList, i) = SPUtils.getQiNiuConfig(this)
        if (arrayList != null && i != null) {
            arrayList.forEach {
                if (it.Domain == i) mCurrentConfig = it
                mConfigList.add(it)
            }
        }
    }

    private fun initView() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAdapter = DriverFeedAdapter(this, mListData)
        flCustomRefreshView.setAdapter(mAdapter)
        flCustomRefreshView.setFooterView(null)
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        flCustomRefreshView.recyclerView.apply {
            layoutManager = manager
            overScrollMode = View.OVER_SCROLL_NEVER
        }
        flCustomRefreshView.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                if (mCurrentConfig != null) {
                    getDataFromServer(mCurrentConfig!!)
                } else {
                    flCustomRefreshView.complete()
                }
            }

            override fun onLoadMore() {
            }
        })
        flCustomRefreshView.isRefreshing = true
        flCustomRefreshView.setEmptyView("")
    }

    /**
     * 获取数据
     */
    @SuppressLint("SwitchIntDef")
    private fun getDataFromServer(config: _005QiNiuConfig) {
        val auth = Auth.create(config.AccessKey, config.SecretKey)
        val zone = when (config.Area) {
            _005QiNiuConfig.HUA_DONG -> Zone.zone0()
            _005QiNiuConfig.HUA_BEI -> Zone.zone1()
            _005QiNiuConfig.HUA_NAN -> Zone.zone2()
            else -> Zone.autoZone()
        }
        Observable.just(BucketManager(auth, Configuration(zone))).map {
            val iterator = it.createFileListIterator(config.Bucket, "", 1000, "")
            mListData.clear()
            while (iterator.hasNext()) {
                iterator.next().forEach { mListData.add(_005FileInfo(it, config.Domain)) }
            }
            mListData.sortBy { it.putTime }
            mListData.reverse()
        }.compose(RxSchedulers.io_main()).subscribe({
            mAdapter.notifyDataSetChanged()
            flCustomRefreshView.complete()
        }, {
            it.printStackTrace()
            flCustomRefreshView.complete()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu != null && mConfigList.isNotEmpty()) {
            mConfigList.forEachIndexed { index, it ->
                menu.add(R.id.iconGroup, index, 0, it.Bucket)
                    .setEnabled(true)
                    .setIcon(R.drawable.action_icon_search)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
            }
        }
        menuInflater.inflate(R.menu._005_qi_niu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.groupId == R.id.iconGroup) {
            //点击七牛云的配置条目
            _005DialogQiNiuConfig(this, mConfigList[item.itemId]).show()
        } else {
            when (item.itemId) {
            //点击添加七牛云配置
                R.id.action_add_repository -> showAddQiNiuConfigDialog()
            }
        }
        return true
    }

    /**
     * 显示添加七牛配置对话框
     */
    private fun showAddQiNiuConfigDialog() {
        val dialog = _005DialogQiNiuConfig(this)
        dialog.setOnDismissListener {
            if (mCurrentConfig != null) return@setOnDismissListener
            val (list, domain) = SPUtils.getQiNiuConfig(this)
            if (list == null || domain == null) return@setOnDismissListener
            list.forEach {
                if (it.Domain == domain) mCurrentConfig = it
                mConfigList.add(it)
            }
            invalidateOptionsMenu()
            flCustomRefreshView.isRefreshing = true
        }
        dialog.show()
    }

    private fun uploadImg2QiNiu() {
        /* val config = Configuration.Builder()
             .zone(Zone.autoZone())
             .build()
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
        }*/
    }
}