package dinson.customview

import com.cronutils.descriptor.CronDescriptor
import com.cronutils.model.CronType
import com.cronutils.model.definition.CronDefinitionBuilder
import com.cronutils.parser.CronParser
import org.jsoup.Jsoup
import org.junit.Assert
import org.junit.Test
import org.quartz.CronExpression
import org.quartz.TriggerUtils
import org.quartz.impl.triggers.CronTriggerImpl
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*


/**
 *  测试用例
 */
class JsoupUnitTest {


    @Test
    fun testAdd() {

        println("===================Power Start================================")

        //草榴网页解析所有图片
        /*val url = "https://cl.6261x.xyz/htm_mob/2210/7/5308396.html"
        val document = Jsoup.connect(url).get()
        val select = document.select("#conttpc")[0].allElements
        //println(select)
        select.forEach {
            val img = it.select("img")
            val imgUrl = img.attr("ess-data")
            if (imgUrl.isNotEmpty()){

            }
        }*/

        //cron解析
        val expressiion = "0/30 * * * * ?"

        val cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ)
        val parser =  CronParser(cronDefinition)
        val cron = parser.parse(expressiion)
        //设置语言
        val descriptor = CronDescriptor.instance(Locale.CHINESE)

        cron.cronDefinition

        val describe = descriptor.describe(cron)
        println("describe: $describe")


        val cronTriggerImpl =   CronTriggerImpl()
        cronTriggerImpl.cronExpression = expressiion


        // 这个是重点，一行代码搞定
        val list  =   TriggerUtils.computeFireTimes(cronTriggerImpl, null, 2)
        val l = (list[1].time - list[0].time)/1000
        println("相隔 $l s")

        /*  val cronExpression = CronExpression.parse(cron)
          //下次预计的执行时间
          LocalDateTime nextFirst = cronExpression.next(LocalDateTime.now())
          //下下次预计的执行时间
          LocalDateTime nextSecond = cronExpression.next(nextFirst)
          //计算周期1
          long between1 = ChronoUnit.SECONDS.between(nextFirst, nextSecond)
          Assert.assertEquals(between1, 30)
          //计算周期2
          long between2 = Duration.between(nextFirst, nextSecond).getSeconds()
          Assert.assertEquals(between2, 30)*/

        println("===================Power End================================")
    }


}