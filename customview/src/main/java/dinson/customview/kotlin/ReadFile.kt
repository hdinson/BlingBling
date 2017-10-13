package dinson.customview.kotlin

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import java.io.File

/**
 *   @author Dinson - 2017/10/13
 */
fun main(args: Array<String>) {
    val text = File("D:\\AndroidProjects\\BlingBling\\customview\\src\\main\\assets\\QuanZhou").readText()

    Observable.fromIterable(text.toCharArray().asIterable())
        .filter { !it.isWhitespace() }
        .groupBy { it }
        .map { o ->
            o.count().subscribe(Consumer {
                println("${o.key} -> $it")
            })
        }
        .subscribe()

    Thread { println("Hello") }.run()


}

