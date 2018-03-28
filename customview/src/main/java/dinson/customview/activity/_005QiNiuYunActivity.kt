package dinson.customview.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.qiniu.common.Zone
import com.qiniu.storage.BucketManager
import com.qiniu.storage.Configuration
import com.qiniu.util.Auth
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter.DriverFeedAdapter
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.toast
import dinson.customview.kotlin.verbose
import dinson.customview.model._005FileInfo
import dinson.customview.model._005QiNiuConfig
import dinson.customview.utils.SPUtils
import dinson.customview.utils.SystemBarModeUtils
import dinson.customview.weight.dialog._005DialogQiNiuConfig
import dinson.customview.weight.recycleview.OnItemClickListener
import dinson.customview.weight.refreshview.CustomRefreshView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity__005_qi_niu_yun.*
import java.io.File


class _005QiNiuYunActivity : BaseActivity() {

    private val mListData = ArrayList<_005FileInfo>()
    private val mConfigList = ArrayList<_005QiNiuConfig>()
    private var mCurrentConfig: _005QiNiuConfig? = null

    private lateinit var mAdapter: DriverFeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__005_qi_niu_yun)

        mAdapter = DriverFeedAdapter(this, mListData)
        initView()
        initConfig()
    }

    /**
     * 初始化七牛云设置
     */
    private fun initConfig() {
        val (arrayList, i) = SPUtils.getQiNiuConfig(this)
        dealQiNiuConfig(arrayList, i)
    }


    private fun initView() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        flCustomRefreshView.setAdapter(mAdapter)
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        flCustomRefreshView.recyclerView.apply {
            layoutManager = manager
            overScrollMode = View.OVER_SCROLL_NEVER
            addOnItemTouchListener(OnItemClickListener(context, this,
                OnItemClickListener.OnClickListener { _, position ->
                    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val bean = mListData[position]
                    var domain = bean.domain
                    if (!domain.startsWith("http")) domain = "http://" + domain + File.separator
                    val data = ClipData.newPlainText("Label", "$domain${bean.key}")
                    cm.primaryClip = data
                    "已复制".toast()
                }))
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
                iterator.next().filter { it.mimeType.contains("image") }
                    .forEach { mListData.add(_005FileInfo(it, config.Domain)) }
            }
            mListData.sortBy { it.putTime }
            mListData.reverse()
            verbose("Load ${mListData.size} pic")
        }.compose(RxSchedulers.io_main()).subscribe({
            mAdapter.notifyDataSetChanged()
            flCustomRefreshView.complete()
        }, {
            it.printStackTrace()
            mAdapter.notifyDataSetChanged()
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
            //点击切换七牛云的配置条目
            if (mConfigList[item.itemId].equals(mCurrentConfig)) return true
            mCurrentConfig = mConfigList[item.itemId]
            resetTitleBarText()
            SPUtils.setQiuNiuDefaultDomain(this, mCurrentConfig!!.Domain)
            flCustomRefreshView.isRefreshing = true
        } else {
            when (item.itemId) {
            //点击添加七牛云配置
                R.id.action_add_repository -> showAddQiNiuConfigDialog()
                R.id.action_edit_repository -> showAddQiNiuConfigDialog(mCurrentConfig)
            }
        }
        return true
    }

    /**
     * 显示添加七牛配置对话框
     */
    private fun showAddQiNiuConfigDialog(config: _005QiNiuConfig? = null) {
        val dialog = _005DialogQiNiuConfig(this, config)
        dialog.setOnDismissListener {
            val (list, domain) = SPUtils.getQiNiuConfig(this)
            dealQiNiuConfig(list, domain)
        }
        dialog.show()
    }

    /**
     * 处理变更数据（包括title，和menu）
     */
    private fun dealQiNiuConfig(configList: ArrayList<_005QiNiuConfig>?, domain: String?) {
        if (configList == null || domain == null) {
            mCurrentConfig = null
            mConfigList.clear()
            invalidateOptionsMenu()
            mListData.clear()
            resetTitleBarText()
            mAdapter.notifyDataSetChanged()
            return
        }

        mConfigList.clear()
        mConfigList.addAll(configList)
        invalidateOptionsMenu()

        val newConfig = configList.find { it.Domain == domain }
        if (!newConfig!!.equals(mCurrentConfig)) {
            mCurrentConfig = newConfig
            flCustomRefreshView.isRefreshing = true
        }
        resetTitleBarText()
    }

    /**
     * 根据当前的配置文件动态修改标题栏文字
     * 在当前配置变动的时候都应该调一下，更改标题
     */
    private fun resetTitleBarText() {
        toolbar.title = if (mCurrentConfig != null) mCurrentConfig!!.Bucket else "七牛云"
    }

    private fun uploadImg2QiNiu() {
        /*val config = Configuration.Builder()
             .zone(Zone.autoZone())
             .build()
        val configuration = Configuration(Zone.autoZone())
        val uploadManager = UploadManager(configuration)
        // 设置图片名字
        val key = "icons_wireless.png"
        //val picPath = getOutputMediaFile().toString()
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