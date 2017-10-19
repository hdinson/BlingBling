package dinson.customview.model

import dinson.customview.utils.StringUtils

/**
 * @author Dinson - 2017/10/16
 */
data class _003CurrencyModel(val currencyCn: String, val currencyCode: String, val sign: String, val imgUrl: String) {
    /**
     * 与美元的兑换比率
     */
    var baseRate = 1.0

    /**
     * 目标货币与美元的兑换比率
     */
    var targetRate = 1.0

    var equation: String = ""

    var tag: Int? = null

    /**
     * 与目标货币的兑换
     *
     * @param money 货币金额
     * @return 目标货币金额（保留两位小数）
     */
    fun getTargetMoney(targetMoney: Double): String =
        StringUtils.formatMoney(targetMoney / targetRate * baseRate)
}
