package dinson.customview.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._003CurrencyAdapter
import dinson.customview.api.ExchangeApi
import dinson.customview.entity.ExchangeBean
import dinson.customview.http.BaseObserver
import dinson.customview.http.HttpHelper
import dinson.customview.listener.CalculatorKey
import dinson.customview.listener.OnItemSwipeOpen
import dinson.customview.model._003ModelUtil
import dinson.customview.utils.CacheUtils
import dinson.customview.utils.LogUtils
import dinson.customview.utils.UIUtils
import dinson.customview.weight.recycleview.OnItemClickListener
import dinson.customview.weight.swipelayout.SwipeItemLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity__003_exchange.*
import org.json.JSONObject


class _003ExchangeActivity : BaseActivity(), OnItemSwipeOpen, View.OnClickListener, OnItemClickListener.OnClickListener, View.OnLongClickListener {


    private val mAdapter = _003CurrencyAdapter(this, _003ModelUtil.getCurrencyList(5), this)
    private var mExchangeData: ExchangeBean? = null

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
            mExchangeData = Gson().fromJson(exchangeStr, ExchangeBean::class.java)
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
                    mExchangeData = value
                    val json = Gson().toJson(value)
                    CacheUtils.setExangeRateCache(json)
                    setAdapterRate(json)
                }
            })
    }

    private fun initUI() {
        rvContent.layoutManager = LinearLayoutManager(this)
        rvContent.adapter = mAdapter
        rvContent.addOnItemTouchListener(OnItemClickListener(this, rvContent, this))

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
        val rates = JSONObject(exchangeStr).getJSONObject("rates")
        mAdapter.mDataList.forEach {
            it.baseRate = rates.get(it.currencyCode).toString().toFloat()
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
        LogUtils.e("open position: " + position)
    }

    override fun setWindowBackgroundColor(): Int = R.color._003_window_bg
}
