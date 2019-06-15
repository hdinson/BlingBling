package dinson.customview.ai


object Role {
    val com: Int = 1
    val hum: Int = 2
    val empty: Int = 0
    fun reverse(r: Int): Int {
        return if (r == 1) 2 else 1
    }
}
