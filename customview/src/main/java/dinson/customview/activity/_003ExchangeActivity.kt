package dinson.customview.activity

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import com.google.gson.Gson
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._003CurrencyAdapter
import dinson.customview.adapter._003LeftDrawerAdapter
import dinson.customview.api.ExchangeApi
import dinson.customview.entity.ExchangeBean
import dinson.customview.http.BaseObserver
import dinson.customview.http.HttpHelper
import dinson.customview.listener.CalculatorKey
import dinson.customview.listener.OnItemSwipeOpen
import dinson.customview.model._003CurrencyModel
import dinson.customview.model._003ModelUtil
import dinson.customview.utils.CacheUtils
import dinson.customview.utils.LogUtils
import dinson.customview.utils.SPUtils
import dinson.customview.utils.UIUtils
import dinson.customview.weight.recycleview.OnItemClickListener
import dinson.customview.weight.swipelayout.SwipeItemLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity__003_exchange.*
import org.json.JSONObject

class _003ExchangeActivity : BaseActivity(), OnItemSwipeOpen, View.OnClickListener, OnItemClickListener.OnClickListener, View.OnLongClickListener, DrawerLayout.DrawerListener {

    private lateinit var mAdapter: _003CurrencyAdapter
    private var mCurrencyData = ArrayList<_003CurrencyModel>()      //存储所有支持汇率的数据
    private var mUserCurrencyData = ArrayList<_003CurrencyModel>()  //存储用户需要计算的汇率数据
    private var mNeedChangeItem = -1    //侧滑时记录需要被替换的条目
    private var mAllDataRates: JSONObject? = null //存储接口所有的汇率的json

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__003_exchange)

        initUI()
        getExchangeData()
    }

    private fun getExchangeData() {
        val exchangeStr = CacheUtils.getExchangeRateCache()

        //显示本地数据
        if (exchangeStr != null) {
            setAdapterRate(exchangeStr)
        } else {
            // TODO 用控件实现
            UIUtils.showToast("正在获取汇率...")
        }

        //获取最新数据
        HttpHelper.create(ExchangeApi::class.java).getRate()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<ExchangeBean?>() {
                override fun onHandlerSuccess(value: ExchangeBean?) {
                    val json = Gson().toJson(value)
                    CacheUtils.setExangeRateCache(json)
                    setAdapterRate(json)
                }
            })
    }

    private fun initUI() {
        mCurrencyData = _003ModelUtil.getCurrencyList()
        val userCurrency = SPUtils.getUserCurrency()
        if (userCurrency == null) {
            mCurrencyData.take(5).toCollection(mUserCurrencyData)
        } else {
            Observable.fromIterable(mCurrencyData)
                .filter {
                    it.tag = userCurrency.indexOf(it.currencyCode)
                    it.tag != -1
                }
                .collect({ mUserCurrencyData }, { t1, t2 -> t1.add(t2) })
                .flatMap { Observable.fromIterable(it).toSortedList { t1, t2 -> t1.tag!! - t2.tag!! } }
                .subscribe(Consumer { mUserCurrencyData = it as ArrayList<_003CurrencyModel> })
        }

        mAdapter = _003CurrencyAdapter(this, mUserCurrencyData, this)

        rvContent.layoutManager = LinearLayoutManager(this)
        rvContent.adapter = mAdapter
        rvContent.addOnItemTouchListener(OnItemClickListener(this, rvContent, this))
        //recycleview notifyItemChanged刷新时闪烁问题
        (rvContent.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        rvLeft.adapter = _003LeftDrawerAdapter(this, mCurrencyData)
        rvLeft.layoutManager = LinearLayoutManager(this)
        rvLeft.addOnItemTouchListener(OnItemClickListener(this, rvContent
            , OnItemClickListener.OnClickListener { _, position ->
            val bean = mCurrencyData[position]
            bean.baseRate = mAllDataRates?.get(bean.currencyCode).toString().toDouble()
            mAdapter.onItemReplaced(mNeedChangeItem, bean)
            drawerLayout.closeDrawers()
        }))

        //侧滑布局禁止手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawerLayout.addDrawerListener(this)

        n0.setOnClickListener(this)
        n1.setOnClickListener(this)
        n2.setOnClickListener(this)
        n3.setOnClickListener(this)
        n4.setOnClickListener(this)
        n5.setOnClickListener(this)
        n6.setOnClickListener(this)
        n7.setOnClickListener(this)
        n8.setOnClickListener(this)
        n9.setOnClickListener(this)
        add.setOnClickListener(this)
        sub.setOnClickListener(this)
        mul.setOnClickListener(this)
        div.setOnClickListener(this)
        dot.setOnClickListener(this)
        delete.setOnClickListener(this)
        delete.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.n0 -> mAdapter.onInput(CalculatorKey.N0)
            R.id.n1 -> mAdapter.onInput(CalculatorKey.N1)
            R.id.n2 -> mAdapter.onInput(CalculatorKey.N2)
            R.id.n3 -> mAdapter.onInput(CalculatorKey.N3)
            R.id.n4 -> mAdapter.onInput(CalculatorKey.N4)
            R.id.n5 -> mAdapter.onInput(CalculatorKey.N5)
            R.id.n6 -> mAdapter.onInput(CalculatorKey.N6)
            R.id.n7 -> mAdapter.onInput(CalculatorKey.N7)
            R.id.n8 -> mAdapter.onInput(CalculatorKey.N8)
            R.id.n9 -> mAdapter.onInput(CalculatorKey.N9)
            R.id.add -> mAdapter.onInput(CalculatorKey.ADD)
            R.id.sub -> mAdapter.onInput(CalculatorKey.SUB)
            R.id.mul -> mAdapter.onInput(CalculatorKey.MUL)
            R.id.div -> mAdapter.onInput(CalculatorKey.DIV)
            R.id.dot -> mAdapter.onInput(CalculatorKey.DOT)
            R.id.delete -> mAdapter.onInput(CalculatorKey.DELETE)
        }
    }

    override fun onLongClick(v: View?): Boolean {
        mAdapter.onInput(CalculatorKey.CLEAR)
        return true
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

    /**
     * 条目的选中改变，更换货币基数
     */
    override fun onItemClick(view: View?, position: Int) {
        mAdapter.OnItemChange(position)
    }

    override fun onOpen(view: SwipeItemLayout, position: Int) {
        view.close()
        mNeedChangeItem = position
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onDrawerClosed(drawerView: View?) {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onDrawerOpened(drawerView: View?) {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {}
    override fun onDrawerStateChanged(newState: Int) {}
    override fun setWindowBackgroundColor(): Int = R.color._003_window_bg
}
