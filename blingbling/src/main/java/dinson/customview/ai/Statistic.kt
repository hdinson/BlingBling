package dinson.customview.ai


class Statistic(val size: Int) {

    var table: Array<IntArray> = Array(size) { IntArray(size) }


    fun print(candidates: Array<IntArray>): String {
        //console.log(this.table.map(function (r) { return r.map(i=>parseInt(Math.sqrt(i/10000))).join(',') }))
        var max = 0
        val p = IntArray(2)
        candidates.forEach {
            val s = this.table[it[0]][it[1]]
            if (s > max) {
                max = s
                p[0] = it[0]
                p[1] = it[1]
            }
        }
        return "历史表推荐走法: ${p[0]},${p[1]}"
        //console.log('历史表推荐走法:', p);
    }
}