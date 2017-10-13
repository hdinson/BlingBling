package dinson.customview.kotlin

/**
 *   @author Dinson - 2017/10/13
 */
enum class Lang(val s: String) {
    ENGLISH("英语"),
    CHINESE("中文"),
    JAPANESE("日语"),
    KOREAN("韩语");

    fun sayLanguage() {
        println("$s 2")
    }

    companion object {
        fun parse(name: String): Lang = Lang.valueOf(name.toUpperCase())
    }
}

fun main(args: Array<String>) {
    val parse = Lang.parse("english")
    println("$parse 1")
    parse.sayLanguage()
    parse.sayBye()
}

fun Lang.sayBye() {
    val bye = when (this) {
        Lang.ENGLISH -> Lang.ENGLISH
        Lang.CHINESE -> Lang.CHINESE
        Lang.JAPANESE -> Lang.JAPANESE
        Lang.KOREAN -> Lang.KOREAN
    }
    print("$bye 3")
}
