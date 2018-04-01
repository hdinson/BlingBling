package dinson.customview

import org.junit.Test

/**
 *   @author Dinson - 2018/3/1
 */
class KotlinUnitTest {
    @Test
    fun testAdd() {


        val items = listOf(1, 2, 3, 4)
       val a= items.filter { it % 2 == 0 }.toList() // 返回[2 , 4]
        println(a)
    }
}