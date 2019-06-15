package dinson.customview.ai

class Board {
    lateinit var scoreCache: Array<Array<Array<IntArray>>>
    val board= Array (2 ){IntArray(2)}

    init {
        var size = 0


        scoreCache = arrayOf(

            arrayOf(// placeholder
                Array(size) { IntArray(size) },
                Array(size) { IntArray(size) },
                Array(size) { IntArray(size) },
                Array(size) { IntArray(size) }
            ),
            arrayOf(// for role 1
                Array(size) { IntArray(size) },
                Array(size) { IntArray(size) },
                Array(size) { IntArray(size) },
                Array(size) { IntArray(size) }
            ),
            arrayOf(// for role 2
                Array(size) { IntArray(size) },
                Array(size) { IntArray(size) },
                Array(size) { IntArray(size) },
                Array(size) { IntArray(size) }
            )
        )
    }
}