package dinson.customview.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.qiniu.storage.BucketManager
import com.qiniu.storage.Configuration
import com.qiniu.storage.UploadManager
import com.qiniu.storage.model.DefaultPutRet
import com.qiniu.util.Auth
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter.DriverFeedAdapter
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.toast
import dinson.customview.kotlin.verbose
import dinson.customview.model._005FileInfo
import dinson.customview.model._005QiNiuConfig
import dinson.customview.utils.MD5
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
        val zone = config.getZone()
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
        menuInflater.inflate(R.menu._005_qi_niu_toolbar, menu)
        if (menu != null && mConfigList.isNotEmpty()) {
            if (mConfigList.isNotEmpty()) {
                menu.add("编辑")
                    .setIcon(R.drawable.action_icon_edit)
                    .setOnMenuItemClickListener {
                        showAddQiNiuConfigDialog(mCurrentConfig)
                        true
                    }.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

                menu.add("上传图片")
                    .setIcon(R.drawable.action_icon_upload)
                    .setOnMenuItemClickListener {
                        showPicSelectorActivity()
                        true
                    }.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

            }
            mConfigList.forEachIndexed { index, it ->
                menu.add(R.id.iconGroup, index, 0, it.Bucket)
                    .setEnabled(true)
                    .setIcon(R.drawable.action_icon_search)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
            }
        }
        return true
    }

    /**
     * 跳转图片选择界面
     */
    private fun showPicSelectorActivity() {
        PictureSelector.create(_005QiNiuYunActivity@ this)
            .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//            .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
            .maxSelectNum(1)// 最大图片选择数量 int
            .minSelectNum(1)// 最小选择数量 int
            .imageSpanCount(4)// 每行显示个数 int
            .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
            .previewImage(false)// 是否可预览图片 true or false
//            .previewVideo()// 是否可预览视频 true or false
//            .enablePreviewAudio() // 是否可播放音频 true or false
            .isCamera(true)// 是否显示拍照按钮 true or false
            .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
            .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//            .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
            .enableCrop(false)// 是否裁剪 true or false
            .compress(false)// 是否压缩 true or false
//            .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//            .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//            .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
            .isGif(false)// 是否显示gif图片 true or false
//            .compressSavePath(getPath())//压缩图片保存地址
//            .freeStyleCropEnabled()// 裁剪框是否可拖拽 true or false
//            .circleDimmedLayer()// 是否圆形裁剪 true or false
//            .showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//            .showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
//            .openClickSound()// 是否开启点击声音 true or false
//            .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
//            .previewEggs()// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//            .cropCompressQuality()// 裁剪压缩质量 默认90 int
//            .minimumCompressSize(100)// 小于100kb的图片不压缩
//            .synOrAsy(true)//同步true或异步false 压缩 默认同步
//            .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
//            .rotateEnabled() // 裁剪是否可旋转图片 true or false
//            .scaleEnabled()// 裁剪是否可放大缩小图片 true or false
//            .videoQuality()// 视频录制质量 0 or 1 int
//            .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
//            .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
//            .recordVideoSecond()//视频秒数录制 默认60s int
//            .isDragFrame(false)// 是否可拖动裁剪框(固定)
            .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    val path = selectList[0].path
                    uploadImg2QiNiu(path)
                }
            }
        }
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

    /**
     * 上传图片到服务器。path为本地路径
     */
    private fun uploadImg2QiNiu(path: String) {
        "正在上传".toast()
        mCurrentConfig?.let {
            val upToken = Auth.create(it.AccessKey, it.SecretKey).uploadToken(it.Bucket)
            val zone = it.getZone()
            val file = File(path)
            if (!file.exists() || !file.isFile) return@let
            val key = file.name.let { MD5.encode(it) + it.substring(it.lastIndexOf(".")) }

            Observable.just(UploadManager(Configuration(zone))).map {
                val response = it.put(path, key, upToken)
                val putRet = Gson().fromJson(response.bodyString(), DefaultPutRet::class.java)
                verbose("Upload success {hash: ${putRet.hash} key:${putRet.key}}")
            }.compose(RxSchedulers.io_main()).subscribe({
                mAdapter.notifyDataSetChanged()
                flCustomRefreshView.isRefreshing = true
            }, {
                it.printStackTrace()
            })
        }
    }
}