package dinson.customview.model

import com.dinson.blingbase.kotlin.loge
import com.dinson.blingbase.utils.MD5
import dinson.customview.entity.av.Movie
import dinson.customview.entity.av.MovieInfo
import org.jsoup.Jsoup


object _027AvModel {

//    const val BASE_URL = "https://avmoo.host"
    private const val BASE_URL = "https://avsox.host"


    const val HOME = "$BASE_URL/cn//page/"
    const val PUBLIC = "$BASE_URL/cn/released/page/"
    const val HOT = "$BASE_URL/cn/popular/page/"

    const val CATEGORY = "$BASE_URL/cn/genre/"
        const val SEARCH_KEY = "$BASE_URL/cn/search/"


    fun getSearchUrl(query: String) = "$BASE_URL/cn/search/$query"

    fun getPlayAddress(vid: String, time: String? = null): String {
        val ts = time ?: (System.currentTimeMillis() / 1000).toString()
        return "http://api.rekonquer.com/psvs/mp4.php?vid=$vid&ts=$ts&sign=${MD5.b(vid, ts)}"
    }


    /**
     * 注意运行在子线程
     */
    fun decodeMovie(url: String): ArrayList<Movie> {
        val list = ArrayList<Movie>()
        try {
            val document = Jsoup.connect(url).get()
            val boxes = document.select("a[class*=movie-box]")

            boxes.forEach { e ->
                val img = e.select("div.photo-frame > img")
                val span = e.select("div.photo-info > span")
                val hot = span.tagName("i").size > 0
                val date = span.select("date")
                list.add(Movie(img.attr("title"), date[0].text(),
                    date[1].text(), img.attr("src"), e.attr("href"), hot))
            }
        } catch (e: Exception) {
            e.message.toString().loge()
        }
        return list
    }

    /**
     * 注意运行在子线程
     */
    fun decodeMovieInfo(url: String): MovieInfo {
        val moveDetailInfo = MovieInfo()
        try {
            val document = Jsoup.connect(url).get()
            moveDetailInfo.coverUrl =
                document.select("[class=bigImage]").attr("href")

            document.select("[class*=sample-box]").forEach {
                it.getElementsByTag("img")
                val thumbnailUrl = it.getElementsByTag("img")[0].attr("src")
                val link = it.attr("href")
                moveDetailInfo.screenshots.add(MovieInfo.Screenshot(thumbnailUrl, link))
            }
            document.select("[class*=avatar-box]").forEach {
                moveDetailInfo.actresses.add(MovieInfo.Actress(
                    it.text(),
                    it.getElementsByTag("img")[0].attr("src"),
                    it.attr("href")))
            }
            val info = document.select("div.info")
            if (info != null) {
                val headerNames = ArrayList<String>()
                val headerAttr = ArrayList<ArrayList<String>>()
                info.select("p[class*=header]").forEach {
                    headerNames.add(it.text().toString().replace(":", ""))
                }

                info.select("p > a").forEach {
                    val strs = ArrayList<String>()
                    strs.add(it.text())
                    strs.add(it.attr("href"))
                    headerAttr.add(strs)
                }
                val size = if (headerNames.size < headerAttr.size)
                    headerNames.size else headerAttr.size

                for (i in 0 until size) {
                    moveDetailInfo.headers.add(MovieInfo.Header(headerNames[i],
                        headerAttr[i][0].trim(), headerAttr[i][1].trim()))
                }

                info.select("* > [class=genre] > a").forEach {
                    moveDetailInfo.genres.add(MovieInfo.Genre(it.text(), it.attr("href")));
                }
            }
        } catch (e: Exception) {
            e.message.toString().loge()
        }
        return moveDetailInfo
    }
}