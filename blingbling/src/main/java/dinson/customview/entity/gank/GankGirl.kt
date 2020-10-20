package dinson.customview.entity.gank

class GankGirl(
    val data: List<GankGirlImg> = listOf(),
    val status: Int = 0
)

class GankGirlImg(
    val url: String = "",
    var height: Int = 0,
    var width: Int = 0
)