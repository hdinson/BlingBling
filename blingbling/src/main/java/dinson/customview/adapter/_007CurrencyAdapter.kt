package dinson.customview.adapter

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import dinson.customview.BuildConfig
import dinson.customview.R
import dinson.customview.listener.CalculatorKey
import dinson.customview.listener.OnItemSwipeOpen
import dinson.customview.listener._003OnCalculatorInput
import dinson.customview.listener._003OnRvItemChangeListener
import dinson.customview.model._007CurrencyModel
import dinson.customview.utils.ArithmeticUtils
import dinson.customview.utils.GlideUtils
import dinson.customview.utils.SPUtils
import dinson.customview.utils.StringUtils
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_007_currency.view.*
import java.util.*
import java.util.regex.Pattern

/**
 * 货币主界面适配器
 */
class _007CurrencyAdapter(context: Context,
                          dataList: List<_007CurrencyModel>,
                          private val mListener: OnItemSwipeOpen)
    : CommonAdapter<_007CurrencyModel>(dataList), _003OnCalculatorInput, _003OnRvItemChangeListener {

    private var mTargetMoney = Integer.MAX_VALUE.toDouble()
    private var mEquationStr = ""
    private var mCurrentSelect = 0

    private val mCursorAnimation = AnimationUtils.loadAnimation(context, R.anim._003_cursor_alpha)
    private val mShakeAnimation = AnimationUtils.loadAnimation(context, R.anim._003_item_shake)

    init {
        mCursorAnimation.repeatCount = Animation.INFINITE
        mCursorAnimation.repeatMode = Animation.REVERSE

        mShakeAnimation.repeatMode = Animation.REVERSE
        mShakeAnimation.repeatCount = 5
    }

    override fun getLayoutId(viewType: Int) = R.layout.item_007_currency

    override fun convert(holder: CommonViewHolder, dataBean: _007CurrencyModel, position: Int) {
        GlideUtils.setImage(holder.itemView.context, "${BuildConfig.QINIU_URL}${dataBean.imgUrl}",
            holder.itemView.ivImg)
        holder.itemView.tvCurrencyCode.text = dataBean.currencyCode
        holder.itemView.tvEquation.text = dataBean.equation
            .replace("\\+".toRegex(), "＋").replace("-".toRegex(), "－")
            .replace("\\*".toRegex(), "×").replace("/".toRegex(), "÷")
        holder.itemView.tvCurrencyCn.text = String.format(Locale.CHINA, "%s %s", dataBean.currencyCn, dataBean.sign)
        holder.itemView.contentLayout.isEnabled = position == mCurrentSelect


        //计算汇率结果
        holder.itemView.tvResult.apply {
            if (mTargetMoney == Integer.MAX_VALUE.toDouble()) {
                text = ""
                hint = dataBean.getTargetMoney(sDefaultMoney.toDouble())
            } else if (mCurrentSelect == position && StringUtils.isEmpty(dataBean.equation)) {
                hint = ""
                text = mEquationStr
            } else {
                hint = ""
                text = dataBean.getTargetMoney(mTargetMoney)
            }
        }

        //侧滑布局的监听
        holder.itemView.deleteLayout
            .addSwipeListener { view, isOpen -> if (isOpen) mListener.onOpen(view, position) }

        //处理光标
        val lCursor = holder.itemView.lFocusView
        val sCursor = holder.itemView.sFocusView
        if (position != mCurrentSelect) {
            lCursor.clearAnimation()
            sCursor.clearAnimation()
            lCursor.visibility = View.GONE
            sCursor.visibility = View.GONE
            return
        }

        if (StringUtils.isEmpty(dataBean.equation)) {
            sCursor.clearAnimation()
            sCursor.visibility = View.GONE
            lCursor.visibility = View.VISIBLE
            lCursor.startAnimation(mCursorAnimation)
        } else {
            lCursor.clearAnimation()
            lCursor.visibility = View.GONE
            sCursor.visibility = View.VISIBLE
            sCursor.startAnimation(mCursorAnimation)
        }
    }

    override fun onInput(key: CalculatorKey) {
        if (!validateInput(key)) {
            //验证不过,晃动提示
            if (StringUtils.isEmpty(mDataList[mCurrentSelect].equation)) {
                getCommonViewHolder(mCurrentSelect).itemView.tvResult.startAnimation(mShakeAnimation)
            } else {
                getCommonViewHolder(mCurrentSelect).itemView.tvEquation.startAnimation(mShakeAnimation)
            }
            return
        }

        val temp = mTargetMoney
        when (key) {

            CalculatorKey.N0, CalculatorKey.N1, CalculatorKey.N2, CalculatorKey.N3, CalculatorKey.N4,
            CalculatorKey.N5, CalculatorKey.N6, CalculatorKey.N7, CalculatorKey.N8, CalculatorKey.N9 -> {
                //普通的数字
                mEquationStr += key.value
                mTargetMoney = ArithmeticUtils.simpleCalculate(mEquationStr)
            }

            CalculatorKey.ADD, CalculatorKey.SUB, CalculatorKey.MUL, CalculatorKey.DIV ->
                //运算符
                mEquationStr = if (isEndWithNum(mEquationStr))
                    mEquationStr + key.value else mEquationStr.substring(0, mEquationStr.length - 1) + key.value

            CalculatorKey.DOT -> mEquationStr += "."    //小数点

            CalculatorKey.DELETE -> {
                //删除
                if (mEquationStr.isEmpty()) return
                var tempEquation = mEquationStr.substring(0, mEquationStr.length - 1)
                mEquationStr = tempEquation
                if (StringUtils.isEmpty(tempEquation)) tempEquation = "0"
                mTargetMoney = if (isEndWithNum(tempEquation)) {
                    ArithmeticUtils.simpleCalculate(tempEquation)
                } else {
                    val tempEquation2 = tempEquation.substring(0, tempEquation.length - 1)
                    ArithmeticUtils.simpleCalculate(tempEquation2)
                }
            }

            CalculatorKey.CLEAR -> {
                //清除
                mTargetMoney = Integer.MAX_VALUE.toDouble()
                mEquationStr = ""
                mDataList[mCurrentSelect].equation = mEquationStr
            }
        }
        mDataList[mCurrentSelect].equation = if (containsOperator(mEquationStr)) mEquationStr else ""
        if (temp != mTargetMoney) {
            notifyDataSetChanged()
        } else {
            notifyItemChanged(mCurrentSelect)
        }
    }

    private fun validateInput(key: CalculatorKey): Boolean {
        var flag = true
        when (key) {
            CalculatorKey.N0 -> if (mEquationStr == "0") flag = false
            CalculatorKey.ADD, CalculatorKey.SUB, CalculatorKey.MUL, CalculatorKey.DIV -> if (StringUtils.isEmpty(mEquationStr)) flag = false
            CalculatorKey.DOT -> if (!isEndWithNum(mEquationStr)) flag = false
            else -> {
            }
        }
        return flag
    }

    /**
     * 是否包含运算符
     */
    private fun containsOperator(str: String) = str.contains("+") || str.contains("-") || str.contains("*") || str.contains("/")


    /**
     * 是否以数字结尾
     */
    private fun isEndWithNum(str: String) = Pattern.matches("(.*\\d+$)", str)


    override fun onItemChange(position: Int) {
        if (position == mCurrentSelect) return
        //删除旧算式表达式赋给选中项
        val oldBean = mDataList[mCurrentSelect]
        val newBean = mDataList[position]
        newBean.equation = oldBean.equation
        oldBean.equation = ""

        mCurrentSelect = position
        val baseRate = newBean.baseRate
        for (currencyModel in mDataList) {
            currencyModel.targetRate = baseRate
        }
        notifyDataSetChanged()
    }


    /**
     * 条目被替换时调用
     *
     * @param position 替换的条目位置
     * @param bean     替换的实体
     */
    fun onItemReplaced(position: Int, bean: _007CurrencyModel) {
        mDataList[position] = bean
        val targetRate = mDataList[mCurrentSelect].baseRate

        val userCurrency = arrayListOf<String>()
        mDataList.forEach {
            it.targetRate = targetRate
            userCurrency.add(it.currencyCode)
        }
        SPUtils.setUserCurrency(userCurrency)
        notifyDataSetChanged()
    }

    companion object {
        private const val sDefaultMoney = 100f
    }

}
