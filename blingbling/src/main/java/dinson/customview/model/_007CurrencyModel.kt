package dinson.customview.model

import com.dinson.blingbase.kotlin.formatMoney


/**
 * 货币模型
 */
data class _007CurrencyModel(val currencyCn: String, val currencyCode: String, val sign: String, val imgUrl: String) {
    /**
     * 与美元的兑换比率
     */
    var baseRate = 1.0

    /**
     * 目标货币与美元的兑换比率
     */
    var targetRate = 1.0

    var equation: String = ""

    /**
     * 有两个作用
     * 1.判断是否是用户所需要的item，tag<0，表示不是，
     * 2.对用户所需的item进行排序，排序的顺序为tag值的大小
     */
    var tag: Int? = null

    /**
     * 与目标货币的兑换
     *
     * @param targetMoney 货币金额
     * @return 目标货币金额（保留两位小数）
     */
    fun getTargetMoney(targetMoney: Double): String =
        (targetMoney / targetRate * baseRate).formatMoney()
}
