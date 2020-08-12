package dinson.customview.entity.one

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class DailyDetailResult(
    val data: DailyDetail? = null
)

/**
 * hpcontent_id : 1736
 * hp_title : VOL.1711
 * author_id : -1
 * hp_img_url : http://image.wufazhuce.com/FlGKn4KQLvns6vJB5IwPoeE9y845
 * hp_img_original_url : http://image.wufazhuce.com/FlGKn4KQLvns6vJB5IwPoeE9y845
 * hp_author : 绘画＆Hammer Chen大木耳 作品
 * ipad_url : http://image.wufazhuce.com/FlGKn4KQLvns6vJB5IwPoeE9y845
 * hp_content : 回忆和期待一样，是一种简化和剪辑现实的工具。
 * by 阿兰·德波顿
 * hp_makettime : 2017-06-13 06:00:00
 * hide_flag : 0
 * last_update_date : 2017-06-12 00:07:01
 * web_url : http://m.wufazhuce.com/one/1736
 * wb_img_url :
 * image_authors : Hammer Chen大木耳
 * text_authors : 阿兰·德波顿
 * image_from :
 * text_from :
 * content_bgcolor : #FFFFFF
 * template_category : 0
 * maketime : 2017-06-13 06:00:00
 * share_list : {"wx":{"title":"","desc":"","link":"http://m.wufazhuce.com/one/1736?channel=singlemessage",
 * "img":"","audio":""},"wx_timeline":{"title":"","desc":"","link":"http://m.wufazhuce
 * .com/one/1736?channel=timeline","img":"","audio":""},"weibo":{"title":"ONE一个 回忆和期待一样，是一种简化和剪辑现实的工具。\r\n
 * by 阿兰·德波顿\u2014\u2014阿兰·德波顿 下载ONE一个APP:http://weibo.com/p/100404157874","desc":"","link":"http://m
 * .wufazhuce.com/one/1736?channel=weibo","img":"","audio":""},"qq":{"title":"","desc":"","link":"http://m
 * .wufazhuce.com/one/1736?channel=qq","img":"","audio":""}}
 * praisenum : 12825
 * sharenum : 3168
 * commentnum : 0
 */
@Parcelize
data class DailyDetail(
    val hpcontent_id: Int,
    val hp_img_url: String = "",
    val hp_content: String = ""
) : Parcelable