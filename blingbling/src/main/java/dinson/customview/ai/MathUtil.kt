package dinson.customview.ai

object MathUtil {

    var threshold = 1.15


    fun equal(a: Int, b: Int): Boolean {
        val tempB = if (b == 0) 0.01 else b.toDouble()
        return if (tempB >= 0) ((a >= tempB / threshold) && (a <= tempB * threshold))
        else ((a >= tempB * threshold) && (a <= tempB / threshold))
    }

    fun greatThan(a: Int, b: Int): Boolean {
        return if (b >= 0) (a >= (b + 0.1) * threshold)
        else (a >= (b + 0.1) / threshold) // 注意处理b为0的情况，通过加一个0.1 做简单的处理
    }

    fun greatOrEqualThan(a: Int, b: Int): Boolean {
        return equal(a, b) || greatThan(a, b)
    }

    fun littleThan(a: Int, b: Int): Boolean {
        return if (b >= 0) (a <= (b - 0.1) / threshold)
        else (a <= (b - 0.1) * threshold)
    }


    fun littleOrEqualThan(a: Int, b: Int): Boolean {
        return equal(a, b) || littleThan(a, b)
    }

    fun containPoint(arrays: Array<IntArray>, p: IntArray): Boolean {
        arrays.forEach {
            if (it[0] == p[0] && it[1] == p[1]) return true
        }
        return false

    }

    fun pointEqual(a: IntArray, b: IntArray): Boolean {
        return a[0] == b[0] && a[1] == b[1]
    }

    fun round(score: ChessType): Int {
        val neg = if (score.score < 0) -1 else 1
        val abs = Math.abs(score.score)
        if (abs <= ChessType.ONE.score / 2) return 0
        if (abs <= ChessType.TWO.score / 2 && abs > ChessType.ONE.score / 2) return neg * ChessType.ONE.score
        if (abs <= ChessType.THREE.score / 2 && abs > ChessType.TWO.score / 2) return neg * ChessType.TWO.score
        if (abs <= ChessType.THREE.score * 1.5 && abs > ChessType.THREE.score / 2) return neg * ChessType.THREE.score
        if (abs <= ChessType.FOUR.score / 2 && abs > ChessType.THREE.score * 1.5) return neg * ChessType.THREE.score * 2
        if (abs <= ChessType.FIVE.score / 2 && abs > ChessType.FOUR.score / 2) return neg * ChessType.FOUR.score
        return neg * ChessType.FIVE.score
    }
}