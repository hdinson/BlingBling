package dinson.customview.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.view.menu.MenuBuilder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview._global.ConstantsUtils
import dinson.customview.adapter._001WanAndroidMainListAdapter
import dinson.customview.api.WanAndroidApi
import dinson.customview.entity.wanandroid.WanAndArticle
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.error
import dinson.customview.kotlin.then
import dinson.customview.kotlin.toast
import dinson.customview.listener._001OnLikeViewClickListener
import dinson.customview.utils.SystemBarModeUtils
import dinson.customview.weight.dialog.OnLoginSuccessListener
import dinson.customview.weight.dialog._001DialogLogin
import dinson.customview.weight.refreshview.CustomRefreshView
import kotlinx.android.synthetic.main.activity__001_wan_android.*

open class _001WanAndroidActivity : BaseActivity(), _001OnLikeViewClickListener {


    companion object {
        private const val REQUEST_CODE_LIKE = 0x1000

        /**
         * 判断当前是否登录
         */
        fun isLogin() = HttpHelper.getCookie(ConstantsUtils.WANANDROID_DOMAIN)
            .filter { it.name() == "loginUserPassword" || it.name() == "loginUserName" }.count() == 2
    }

    private lateinit var mWanAndroidApi: WanAndroidApi
    private lateinit var mAdapter: _001WanAndroidMainListAdapter
    private val mData = ArrayList<WanAndArticle>()
    private var mPageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__001_wan_android)

        mWanAndroidApi = HttpHelper.create(WanAndroidApi::class.java)
        initUI()
    }

    private fun initUI() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initToolbarTitle()//设置title

        mAdapter = _001WanAndroidMainListAdapter(this, mData, this)

        flCustomRefreshView.setAdapter(mAdapter)
        flCustomRefreshView.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getServiceData(true)
            }

            override fun onLoadMore() {
                getServiceData(false)
            }
        })
        flCustomRefreshView.isRefreshing = true
        flCustomRefreshView.setEmptyView("")
    }

    /**
     * 设置toolbarTitle
     */
    private fun initToolbarTitle() {
        toolbar.title = "玩安卓 - ${HttpHelper.getCookie(ConstantsUtils.WANANDROID_DOMAIN)
            .firstOrNull { it.name() == "loginUserName" }?.value() ?: "未登录"}"
    }

    /**
     * 获取文章数据
     * @param isRefresh 是否刷新
     */
    private fun getServiceData(isRefresh: Boolean) {
        if (isRefresh) mPageIndex = 0
        mWanAndroidApi.getMainArticleList(mPageIndex).compose(RxSchedulers.io_main())
            .subscribe({
                flCustomRefreshView.complete()
                if (isRefresh) mData.clear()
                val article = it.data.datas
                if (article.isEmpty()) flCustomRefreshView.onNoMore()

                mData.addAll(article)
                mAdapter.notifyDataSetChanged()
                mPageIndex++
            }, {
                flCustomRefreshView.complete()
            })
    }

    /**
     * 收藏的点击事件
     */
    override fun onClickLikeView(likeView: CheckBox, dataBean: WanAndArticle, position: Int) {
        if (!isLogin()) {
            likeView.isChecked = !likeView.isChecked
            showLoginDialog()
            return
        }
        val observable = likeView.isChecked then mWanAndroidApi.addCollect(dataBean.id)
            ?: mWanAndroidApi.delCollectFromMainList(dataBean.id)
        observable.compose(RxSchedulers.io_main())
            .subscribe({
                //接口请求成功
                if (it.errorCode == 0) {
                    dataBean.isCollect = likeView.isChecked
                    mData[position] = dataBean
                }
            }, {
                error(it.toString())
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu._001_main_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        viewClick@ when (item?.itemId) {
            R.id.action_like -> {
                val login = isLogin()
                if (!login) {
                    showLoginDialog()
                    return@viewClick
                }
                _001WanAndroidLikeActivity.start(this, REQUEST_CODE_LIKE)
            }
            R.id.action_search -> {
                error("search")
            }
            R.id.action_logout -> {
                HttpHelper.clearCookie(ConstantsUtils.WANANDROID_DOMAIN)
                flCustomRefreshView.isRefreshing = true
                initToolbarTitle()
            }
            else -> {
            }
        }
        return true
    }


    /**
     * 显示登录对话框
     */
    private val mLoginSuccessListener by lazy {
        OnLoginSuccessListener({ isSuccess, errorMsg ->
            if (isSuccess) {
                initToolbarTitle()
                flCustomRefreshView.isRefreshing = true
            } else {
                errorMsg.toast()
            }
        })
    }

    /**
     * 显示登录对话框
     */
    private fun showLoginDialog() {
        _001DialogLogin(this).apply {
            setOnLoginSuccessListener(mLoginSuccessListener)
            show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_LIKE) {
            flCustomRefreshView.isRefreshing = true
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 显示item中的图片；
     */
    override fun onPrepareOptionsPanel(view: View?, menu: Menu?): Boolean {
        if (menu?.javaClass == MenuBuilder::class.java) {
            try {
                val m = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", java.lang.Boolean.TYPE)
                m.isAccessible = true
                m.invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return true
    }
}
