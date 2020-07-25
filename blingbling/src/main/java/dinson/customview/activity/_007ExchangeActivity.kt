package dinson.customview.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.dinson.blingbase.widget.recycleview.RvItemClickSupport
import com.google.gson.Gson
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._007CurrencyAdapter
import dinson.customview.adapter._007LeftDrawerAdapter
import dinson.customview.api.ExchangeApi
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.listener.CalculatorKey
import dinson.customview.listener.OnItemSwipeOpen
import dinson.customview.model._007CurrencyModel
import dinson.customview.utils.AppCacheUtil
import dinson.customview.utils.SPUtils
import dinson.customview.weight._003toast.LoadToast
import dinson.customview.weight.swipelayout.SwipeItemLayout
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity__007_exchange.*
import org.json.JSONObject

class _007ExchangeActivity : BaseActivity(), OnItemSwipeOpen, DrawerLayout.DrawerListener {

    private lateinit var mAdapter: _007CurrencyAdapter
    private lateinit var mDrawerAdapter: _007LeftDrawerAdapter
    private lateinit var mLoading: LoadToast    //加载toast
    private var mCurrencyData = ArrayList<_007CurrencyModel>()      //存储所有支持汇率的数据
    private var mUserCurrencyData = ArrayList<_007CurrencyModel>()  //存储用户需要计算的汇率数据
    private var mNeedChangeItem = -1    //侧滑时记录需要被替换的条目
    private var mAllDataRates: JSONObject? = null //存储接口所有的汇率的json

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__007_exchange)

        initUI()
        getExchangeData()
    }

    private fun getExchangeData() {
        val exchangeStr = AppCacheUtil.getExchangeRateCache(this)

        //显示本地数据
        if (exchangeStr != null) {
            setAdapterRate(exchangeStr)
        } else {
            //mLoading.setText("获取汇率")
        }
        mLoading.setText("获取汇率").show()
        //获取最新数据
        HttpHelper.create(ExchangeApi::class.java).getRate()
            .compose(RxSchedulers.io_main())
            .subscribe({ value ->
                val json = Gson().toJson(value)
                AppCacheUtil.setExchangeRateCache(this@_007ExchangeActivity, json)
                setAdapterRate(json)
                mLoading.success()
            }, {
                mLoading.error()
            }).addToManaged()
    }

    private fun initUI() {
        mLoading = LoadToast(this)
            .setProgressColor(Color.RED)
            .setTranslationY(100)

        mCurrencyData = getCurrencyList()

        val userCurrency = SPUtils.getUserCurrency()

        if (userCurrency == null) {
            mCurrencyData.asSequence().take(5).toCollection(mUserCurrencyData)
        } else {
            Observable.fromIterable(mCurrencyData)
                .filter {
                    it.tag = userCurrency.indexOf(it.currencyCode)
                    it.tag != -1
                }
                .collect({ mUserCurrencyData }, { t1, t2 -> t1.add(t2) })
                .flatMap { Observable.fromIterable(it).toSortedList { t1, t2 -> t1.tag!! - t2.tag!! } }
                .subscribe(Consumer { mUserCurrencyData = it as ArrayList<_007CurrencyModel> })
                .addToManaged()
        }
        //在总货币里面删除用户已选择的货币
        mUserCurrencyData.forEach { mCurrencyData.remove(it) }

        mAdapter = _007CurrencyAdapter(this, mUserCurrencyData, this)
        rvContent.layoutManager = LinearLayoutManager(this)
        rvContent.adapter = mAdapter
        rvContent.setOnRvItemClickListener(object : RvItemClickSupport.OnRvItemClickListener {
            override fun onItemClicked(recyclerView: RecyclerView, view: View, position: Int) {
                mAdapter.onItemChange(position)//条目的选中改变，更换货币基数
            }
        })

        //recycleView notifyItemChanged刷新时闪烁问题
        (rvContent.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        mDrawerAdapter = _007LeftDrawerAdapter(mCurrencyData)
        rvLeft.adapter = mDrawerAdapter
        rvLeft.layoutManager = LinearLayoutManager(this)
        RvItemClickSupport.addTo(rvLeft).setOnItemClickListener { _, _, position ->
            val bean = mCurrencyData[position]
            mCurrencyData[position] = mAdapter.mDataList[mNeedChangeItem]
            mDrawerAdapter.notifyItemChanged(position)
            bean.baseRate = mAllDataRates?.get(bean.currencyCode).toString().toDouble()
            mAdapter.onItemReplaced(mNeedChangeItem, bean)
            drawerLayout.closeDrawers()
        }

        //侧滑布局禁止手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawerLayout.addDrawerListener(this)

        n0.setOnClickListener { mAdapter.onInput(CalculatorKey.N0) }
        n1.setOnClickListener { mAdapter.onInput(CalculatorKey.N1) }
        n2.setOnClickListener { mAdapter.onInput(CalculatorKey.N2) }
        n3.setOnClickListener { mAdapter.onInput(CalculatorKey.N3) }
        n4.setOnClickListener { mAdapter.onInput(CalculatorKey.N4) }
        n5.setOnClickListener { mAdapter.onInput(CalculatorKey.N5) }
        n6.setOnClickListener { mAdapter.onInput(CalculatorKey.N6) }
        n7.setOnClickListener { mAdapter.onInput(CalculatorKey.N7) }
        n8.setOnClickListener { mAdapter.onInput(CalculatorKey.N8) }
        n9.setOnClickListener { mAdapter.onInput(CalculatorKey.N9) }
        add.setOnClickListener { mAdapter.onInput(CalculatorKey.ADD) }
        sub.setOnClickListener { mAdapter.onInput(CalculatorKey.SUB) }
        mul.setOnClickListener { mAdapter.onInput(CalculatorKey.MUL) }
        div.setOnClickListener { mAdapter.onInput(CalculatorKey.DIV) }
        dot.setOnClickListener { mAdapter.onInput(CalculatorKey.DOT) }
        delete.setOnClickListener { mAdapter.onInput(CalculatorKey.DELETE) }
        delete.setOnLongClickListener { mAdapter.onInput(CalculatorKey.CLEAR);true }
    }

    private fun setAdapterRate(exchangeStr: String) {
        mAllDataRates = JSONObject(exchangeStr).getJSONObject("rates")
        var targetRate = 0.0
        mAdapter.mDataList.forEachIndexed { index, it ->
            if (index == 0) targetRate = mAllDataRates!!.get(it.currencyCode).toString().toDouble()
            it.targetRate = targetRate
            it.baseRate = mAllDataRates!!.get(it.currencyCode).toString().toDouble()
        }
        mAdapter.notifyDataSetChanged()
    }


    override fun onOpen(view: SwipeItemLayout, position: Int) {
        view.close()
        mNeedChangeItem = position
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onDrawerClosed(drawerView: View) {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onDrawerOpened(drawerView: View) {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
    override fun onDrawerStateChanged(newState: Int) {}
    override fun setWindowBackgroundColor(): Int = R.color._003_window_bg

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
            return
        }
        super.onBackPressed()
    }

    private fun getCurrencyList(): ArrayList<_007CurrencyModel> {
        val result = ArrayList<_007CurrencyModel>()
        resources.assets.open("currency.txt").bufferedReader()
            .useLines { lines ->
                lines.forEach { s ->
                    val split = s.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    result.add(_007CurrencyModel(split[0], split[1], split[2], split[3]))
                }
            }
        return result
    }
}
