package dinson.customview.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import dinson.customview.R
import dinson.customview.activity._027MovieListByLinkActivity
import dinson.customview.entity.av.MovieInfo
import dinson.customview.kotlin.loge
import dinson.customview.kotlin.logi
import dinson.customview.model._027AvModel
import dinson.customview.utils.CacheUtils
import dinson.customview.utils.StringUtils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_027_category.*
import org.jsoup.Jsoup

/**
 * 首页界面
 */
class _027CategoryFragment : ViewPagerLazyFragment(), View.OnClickListener {

    private val mTvColor = Color.parseColor("#3CB034")
    private val mTitleDrawableLeft by lazy {
        val drawable = context!!.getDrawable(R.drawable._027_category_title_left)!!
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        drawable
    }


    override fun onCreateView(original: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return original.inflate(R.layout.fragment_027_category, container, false)
    }

    override fun onFirstUserVisible() {
        val cache = CacheUtils.getCache(  context!!,"_027category")
        if (StringUtils.isEmpty(cache).not()) {
            "getTag << cache".logi()
            val type = object : TypeToken<HashMap<String, ArrayList<MovieInfo.Genre>>>() {}.type
            val turns = Gson().fromJson<HashMap<String, ArrayList<MovieInfo.Genre>>>(cache, type)
            initUI(turns)
            return
        }
        "getTag << net".logi()
        Observable.just(_027AvModel.CATEGORY)
            .map { url ->
                val document = Jsoup.connect(url).get()
                val container = document.getElementsByClass("pt-10").first()

                val keys = container.getElementsByTag("h4").map { it.text() }
                val genres = container.getElementsByClass("genre-box").map { ele ->
                    val list = ArrayList<MovieInfo.Genre>()
                    ele.getElementsByTag("a").forEach {
                        list.add(MovieInfo.Genre(it.text(), it.attr("href")))
                    }
                    list
                }
                val result = HashMap<String, ArrayList<MovieInfo.Genre>>()
                keys.forEachIndexed { index, s ->
                    val key = if (StringUtils.isEmpty(s.trim())) "场地" else s
                    result[key] = genres[index]
                }
                result
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                CacheUtils.setCache(context!!, "_027category",
                    Gson().toJson(it), 36000000)
                initUI(it)
            }, { it.toString().loge() }).addToManaged()
    }

    private fun initUI(data: HashMap<String, ArrayList<MovieInfo.Genre>>) {
        var currentCategory = ""
        data.forEach {
            if (currentCategory != it.key) {
                val text = TextView(context)
                text.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                text.text = it.key
                text.compoundDrawablePadding = 16
                text.textSize = 16f
                text.setPadding(0, 0, 8, 8)
                text.setCompoundDrawables(mTitleDrawableLeft, null, null, null)
                currentCategory = it.key
                flowContent.addView(text)
            }
            it.value.forEach { movie ->
                addItem(movie)
            }
        }
    }

    private fun addItem(movie: MovieInfo.Genre) {
        if (StringUtils.isEmpty(movie.name)) return
        val textView = TextView(context)
        textView.text = movie.name
        textView.tag = "${movie.name}$*key*$${movie.link}"
        textView.setTextColor(mTvColor)
        textView.setBackgroundResource(R.drawable.shape_027_category_item)
        textView.setOnClickListener(this)
        flowContent.addView(textView)
    }

    override fun onClick(v: View) {
        val split = v.tag.toString().split("$*key*$")
        if (split.size == 2) {
            _027MovieListByLinkActivity.start(v.context, split[0], split[1])
        }
    }
}
