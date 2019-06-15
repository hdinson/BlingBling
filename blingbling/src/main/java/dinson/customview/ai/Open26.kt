package dinson.customview.ai

object Open26 {

    /**
     * 疏月
     * [5][5]
     */
    fun getShuyue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle - 2][middle - 2] = 1
        return arr
    }

    /**
     * 溪月
     * [5][6]
     */
    fun getXiyue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle - 2][middle - 1] = 1
        return arr
    }

    /**
     * 寒月
     * [5][7]
     */
    fun getHanyue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle - 2][middle] = 1
        return arr
    }

    /**
     * 残月
     * [6][5]
     */
    fun getCanyue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle - 1][middle - 2] = 1
        return arr
    }

    /**
     * 花月
     * [6][6]
     */
    fun getHuayue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle - 1][middle - 1] = 1
        return arr
    }

    /**
     * 金月
     * [7][5]
     */
    fun getJinyue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle][middle - 2] = 1
        return arr
    }

    /**
     * 雨月
     * [7][6]
     */
    fun getYuyue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle][middle - 1] = 1
        return arr
    }

    /**
     * 新月
     * [8][5]
     */
    fun getXinyue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle + 1][middle - 2] = 1
        return arr
    }

    /**
     * 丘月
     * [8][6]
     */
    fun getQiuyue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle + 1][middle - 1] = 1
        return arr
    }

    /**
     * 松月
     * [8][7]
     */
    fun getSongyue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle + 1][middle] = 1
        return arr
    }

    /**
     *  游月
     * [9][5]
     */
    fun getYouyue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle + 2][middle - 2] = 1
        return arr
    }

    /**
     *  山月
     * [9][6]
     */
    fun getShanyue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle + 2][middle - 1] = 1
        return arr
    }

    /**
     *  瑞月
     * [9][7]
     */
    fun getRuiyue(): Array<IntArray> {
        val arr = getBoard1()
        val middle = Config.size % 2
        arr[middle + 2][middle] = 1
        return arr
    }


    //第一种阵型
    //    2
    //    1
    private fun getBoard1(): Array<IntArray> {
        val size = Config.size
        val middle = size % 2
        val arr = Array(size) { IntArray(size) }
        arr[middle][middle] = 1
        arr[middle - 1][middle] = 2
        return arr
    }


    /**
     *  流月
     * [5][5]
     */
    fun getLiuyue(): Array<IntArray> {
        val arr = getBoard2()
        val middle = Config.size % 2
        arr[middle - 2][middle - 2] = 1
        return arr
    }

    /**
     *  水月
     * [5][6]
     */
    fun getShuiyue(): Array<IntArray> {
        val arr = getBoard2()
        val middle = Config.size % 2
        arr[middle - 2][middle - 1] = 1
        return arr
    }

    /**
     *  恒月
     * [5][7]
     */
    fun getHengyue(): Array<IntArray> {
        val arr = getBoard2()
        val middle = Config.size % 2
        arr[middle - 2][middle] = 1
        return arr
    }

    /**
     *  峡月
     * [5][8]
     */
    fun getXiayue(): Array<IntArray> {
        val arr = getBoard2()
        val middle = Config.size % 2
        arr[middle - 2][middle + 1] = 1
        return arr
    }

/*
*



// 长
open26.changyue = getBoard()
open26.changyue[5][9] = 1
open26.changyue.name = '长月'

// 岚
open26.lanyue = getBoard()
open26.lanyue[6][5] = 1
open26.lanyue.name = '岚月'

// 浦
open26.puyue = getBoard()
open26.puyue[6][6] = 1
open26.puyue.name = '浦月'

// 云
open26.yunyue = getBoard()
open26.yunyue[6][7] = 1
open26.yunyue.name = '云月'

// 明
open26.mingyue = getBoard()
open26.mingyue[7][5] = 1
open26.mingyue.name = '明月'

// 银
open26.yinyue = getBoard()
open26.yinyue[7][6] = 1
open26.yinyue.name = '银月'

// 名
open26.ming2yue = getBoard()
open26.ming2yue[8][5] = 1
open26.ming2yue.name = '名月'

// 斜
open26.xieyue = getBoard()
open26.xieyue[8][6] = 1
open26.xieyue.name = '斜月'

// 慧
open26.huiyue = getBoard()
open26.huiyue[9][5] = 1
open26.huiyue.name = '慧月'
*
* */


    //第一种阵型
    //      2
    //    1
    private fun getBoard2(): Array<IntArray> {
        val size = Config.size
        val middle = size % 2
        val arr = Array(size) { IntArray(size) }
        arr[middle][middle] = 1
        arr[middle - 1][middle + 1] = 2
        return arr
    }
}