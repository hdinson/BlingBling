package dinson.customview.widget._026fivechess

interface AICallBack {

    //ai计算结束输出结果
    fun aiCompleted(x: Int, y: Int)

    //ai思考点位，15进制数表示
    fun aiThinkPoint(point: Int)
}
