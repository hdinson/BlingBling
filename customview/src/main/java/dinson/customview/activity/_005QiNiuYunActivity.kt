package dinson.customview.activity

import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.qiniu.common.Zone
import com.qiniu.storage.BucketManager
import com.qiniu.storage.Configuration
import com.qiniu.storage.UploadManager
import com.qiniu.storage.model.FileInfo
import com.qiniu.util.Auth
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview._global.ConstantsUtils
import dinson.customview.adapter._005MainPicAdapter
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.debug
import dinson.customview.kotlin.error
import dinson.customview.utils.QiNiuUtils
import dinson.customview.utils.SystemBarModeUtils
import dinson.customview.weight.refreshview.CustomRefreshView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity__001_wan_android.*
import java.io.File
import kotlin.concurrent.thread


class _005QiNiuYunActivity : BaseActivity() {

    companion object {
        val AccessKey = "Pxox-Sf-S5bz21TEX6gB6d9x7H05hZLkJtTMJI3y"
        val SecretKey = "CGBwKTzZTcaGcD0NZFocIrhKG5uJNL1yqEwYXdvN"
        val domain = "http://ondlsj2sn.bkt.clouddn.com/"
    }

    private val mData = ArrayList<FileInfo>()
    private lateinit var mAdapter: _005MainPicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__005_qi_niu_yun)

        initView()
        getDataFromServer()
    }

    private fun initView() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAdapter = _005MainPicAdapter(this, mData, domain)

        flCustomRefreshView.setAdapter(mAdapter)
        flCustomRefreshView.onNoMore()
        flCustomRefreshView.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getDataFromServer()
            }

            override fun onLoadMore() {
            }
        })
        flCustomRefreshView.isRefreshing = true
        flCustomRefreshView.setEmptyView("")
        flCustomRefreshView.recyclerView.layoutManager= StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
    }

    /**
     * 获取数据
     */
    private fun getDataFromServer() {
        val auth = Auth.create(AccessKey, SecretKey)
        //实例化一个BucketManager对象
        val configuration = Configuration(Zone.zone0())
        val bucketManager = BucketManager(auth, configuration)
        //要列举文件的空间名
        val bucket = "ijkyer"

        //文件名前缀
        val prefix = ""
        //每次迭代的长度限制，最大1000，推荐值 1000
        val limit = 1000
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        val delimiter = ""
        Observable.just(bucket)
            .map { bucketManager.createFileListIterator(bucket, prefix, limit, delimiter) }
            .compose(RxSchedulers.io_main())
            .subscribe({
                mData.clear()
                while (it.hasNext()) {
                    it.next().forEach { mData.add(it) }
                }
                mAdapter.notifyDataSetChanged()
            }, {
                it.printStackTrace()
            })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu._005_qi_niu_toolbar, menu)
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
