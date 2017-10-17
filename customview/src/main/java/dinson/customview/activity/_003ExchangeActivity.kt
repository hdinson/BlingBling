package dinson.customview.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._003CurrencyAdapter
import dinson.customview.listener.OnItemSwipeOpen
import dinson.customview.model._003ModelUtil
import dinson.customview.utils.LogUtils
import dinson.customview.weight.swipelayout.SwipeItemLayout
import kotlinx.android.synthetic.main.activity__003_exchange.*

class _003ExchangeActivity : BaseActivity(), OnItemSwipeOpen, View.OnClickListener {

    override fun onOpen(view: SwipeItemLayout, position: Int) {
        view.close()
        LogUtils.e("open position: " + position)
    }

    private val mAdapter = _003CurrencyAdapter(this, _003ModelUtil.getCurrencyList(5), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__003_exchange)

        initUI()
    }

    private fun initUI() {
        rvContent.layoutManager = LinearLayoutManager(this)
        rvContent.adapter = mAdapter

        n1.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.n0 -> add()
            R.id.n1 -> add()
            R.id.n2 -> add()
            R.id.n3 -> add()
            R.id.n4 -> add()
            R.id.n5 -> add()
            R.id.n6 -> add()
            R.id.n7 -> add()
            R.id.n8 -> add()
            R.id.n9 -> add()
            R.id.add -> add()
            R.id.sub -> add()
            R.id.mul -> add()
            R.id.div -> add()
            R.id.dot -> add()
            R.id.delete -> add()
        }
    }

    fun add() {


    }

    override fun setWindowBackgroundColor(): Int = R.color._003_window_bg
}
