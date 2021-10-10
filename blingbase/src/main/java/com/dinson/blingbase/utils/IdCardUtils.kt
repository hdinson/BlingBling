package com.dinson.blingbase.utils

/**
 * 身份证校验及反向推导
 */
object IdCardUtils {

    private val RATIO_ARR =
        intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)// 17 位加权因子
    private val CHECK_CODE_LIST =
        charArrayOf('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2')// 校验码列表

    private const val NUM_0 = '0'.code

    /**
     * 二代身份证18位号码校验
     * @param idNo 二代身份证号码
     */
    @Suppress("unused")
    fun verifyId(idNo: String): Boolean {
        if (idNo.isEmpty()) {
            return false
        }
        val tempId = idNo.trim { it <= ' ' }
        if (tempId.length != 18) {
            return false
        }
        // 获取身份证号字符数组
        val idCharArr = tempId.toCharArray()
        // 获取最后一位（身份证校验码）
        val verifyCode = idCharArr[17]
        // 身份证号第1-17加权和
        var idSum = 0
        // 余数
        tempId.take(tempId.length - 1).forEachIndexed { index, c ->
            val value = (c - NUM_0).code
            idSum += value * RATIO_ARR[index]
        }
        // 取得余数
        val residue = idSum % 11
        return Character.toUpperCase(verifyCode) == CHECK_CODE_LIST[residue]
    }

    /**
     * 二代身份证号码各个位置上的数字的可能性字符集合
     */
    private val number = arrayOf(
        arrayOf('1', '2', '3', '4', '5', '6'),
        arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
        arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
        arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
        arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
        arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
        arrayOf('1', '2'),
        arrayOf('0', '9'),
        arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
        arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
        arrayOf('0', '1'),
        arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
        arrayOf('0', '1', '2', '3'),
        arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
        arrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9'),
        arrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9'),
        arrayOf('1', '2'),
        arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'X'),
    )

    /**
     * 二代身份证号码反向推导
     * @param idNo 带有隐藏位数的身份证号码，隐藏字符用*号表示
     */
    @Suppress("unused")
    fun reverseIdNo(idNo: String): ArrayList<String> {
        val result = ArrayList<String>()
        val tempId = idNo.trim { it <= ' ' }
        if (idNo.length != 18) {
            return result
        }
        val idCharArr = tempId.toCharArray()
        val star = arrayListOf<Int>()
        idCharArr.forEachIndexed { index, c ->
            if (c == '*') star.add(index)
        }
        if (star.size == 0) {
            result.add(idNo)
            return result
        }
        val map = star.map { number[it].toList() }
        return calculateCombination(idCharArr, star, map)
    }

    /**
     * 算法二，非递归计算所有组合
     * @param inputList 所有数组的列表
     */
    private fun calculateCombination(
        idCharArr: CharArray,
        star: ArrayList<Int>,
        inputList: List<List<Char>>
    ): ArrayList<String> {
        val combination = ArrayList<Int>()
        val result = ArrayList<String>()
        val n = inputList.size
        for (i in 0 until n) {
            combination.add(0)
        }
        var i = 0
        var isContinue: Boolean
        val rg = getDateTimeRegex()
        do {
            //打印一次循环生成的组合
            for (j in 0 until n) {
                val num = inputList[j][combination[j]]
                idCharArr[star[j]] = num
            }
            val date = CharArray(8)
            idCharArr.copyInto(date, 0, 6, 14)
            if (rg.matches(date.concatToString())) {
                val id = idCharArr.concatToString()
                val checked = verifyId(id)
                if (checked) {
                    result.add(id)
                }
            }
            i++
            combination[n - 1] = i
            for (j in n - 1 downTo 0) {
                if (combination[j] >= inputList[j].size) {
                    combination[j] = 0
                    i = 0
                    if (j - 1 >= 0) {
                        combination[j - 1] = combination[j - 1] + 1
                    }
                }
            }
            isContinue = false
            for (integer in combination) {
                if (integer != 0) {
                    isContinue = true
                }
            }
        } while (isContinue)
        return result
    }

    private fun getDateTimeRegex(separator: String = ""): Regex {
        val d =
            "((((19|20)\\d{2})$separator(0?(1|[3-9])|1[012])$separator(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})$separator(0?[13578]|1[02])${separator}31)|(((19|20)\\d{2})${separator}0?2${separator}(0?[1-9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))${separator}0?2${separator}29))\$"
        return Regex(d)
    }
}