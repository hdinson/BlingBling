package dinson.customview

import org.junit.Test


/**
 *  测试用例
 */
class KotlinUnitTest {


    @Test
    fun testAdd() {

        listDelete()

    }

    private fun listDelete() {
        //创建列表并添加数据
        val list = ArrayList<String>()
        list.add("A")
        list.add("B")
        list.add("C")
        list.add("D")
        list.add("E")
        //需求是删除B和D
        

        //方法一
        /*while (iterator.hasNext()) {
            val value = iterator.next()
            if (value == "B" || value == "D") {
                iterator.remove()
                println(value + "已经移除")
            }
        }*/

        //方法二 
        /*val iterator = list.iterator()
        iterator.forEach {
            if (it == "B" || it == "D") {
                iterator.remove()
                println(it + "已经移除")
            }
        }*/


        //普通for循环遍历删除
        var i = 0
        while (i < list.size) {
            if ("D" == list[i]||"B" == list[i]) {
                list.removeAt(i)
                i--
            }
            i++
        }

        //列表中还有的对象
        println(list.toString())
    }


    private fun findTest() {
        val arr = ArrayList<Boolean>()
        (0 until 10).forEach { _ ->
            arr.add(false)
        }

        val b1 = arr.find { it }
        val b2 = arr.find { !it }
        println("one: $b1   :   $b2")


        val arr2 = ArrayList<Int>()
        (0 until 10).forEach {
            arr2.add(it)
        }

        val b3 = arr2.filterIndexed { index, _ -> index != 0 }.find { it == 0 }
        val b4 = arr2.find { it == 11 }
        print("two: $b3   :   $b4")
    }
}