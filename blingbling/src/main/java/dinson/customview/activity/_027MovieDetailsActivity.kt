package dinson.customview.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._027ActressesAdapter
import dinson.customview.adapter._027MovieInfoVideoAdapter
import dinson.customview.api.AvCodeApi
import dinson.customview.entity.av.Movie
import dinson.customview.entity.av.MovieInfo
import dinson.customview.entity.av.MovieVideo
import dinson.customview.http.HttpHelper
import com.dinson.blingbase.kotlin.*
import dinson.customview.manager.GlideSimpleLoader
import dinson.customview.model._027AvModel
import dinson.customview.utils.CacheUtils
import dinson.customview.utils.GlideUtils
import dinson.customview.utils.StringUtils
import dinson.customview.utils.SystemBarModeUtils
import dinson.customview.weight.MessagePicturesLayout
import dinson.customview.weight.imagewatcher.ImageWatcherHelper
import com.dinson.blingbase.widget.recycleview.OnRvItemClickListener
import com.dinson.blingbase.widget.recycleview.RvItemClickSupport
import dinson.customview.kotlin.loge
import dinson.customview.kotlin.logi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity__027_movie_details.*


class _027MovieDetailsActivity : BaseActivity(), View.OnClickListener, MessagePicturesLayout.Callback {


    companion object {
        private const val EXTRA_MOVIE = "extra_movie"
        fun start(context: Context, movie: Movie) {
            val intent = Intent(context, _027MovieDetailsActivity::class.java)
            intent.putExtra(EXTRA_MOVIE, movie)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__027_movie_details)

        initUI()
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        SystemBarModeUtils.setPaddingTop(this, toolbar, clMovieInfo)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)//左侧添加一个默认的返回图标
        supportActionBar?.setHomeButtonEnabled(true)//设置返回键可用
        val movie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        GlideUtils.setImage(this, movie.coverUrl, ivMovieBg)
        GlideUtils.setImage(this, movie.coverUrl, ivMoviePic)
        tvMovieName.text = movie.title
        tvMovieCode.text = "${movie.code}\n${movie.date}"
        getMovieAddress(movie.code)
        getMovieInfo(movie)
    }

    private fun initMovieInfoUI(movieInfo: MovieInfo) {
        //显示大图
        GlideUtils.setImage(this, movieInfo.coverUrl, ivMovieBg)
        //添加制作商，系列等信息
        createHeard(movieInfo)
        //添加演员
        if (movieInfo.actresses.isNotEmpty()) {
            actressesEmpty.hide(true)

            rvActresses.adapter = _027ActressesAdapter(movieInfo.actresses)
            val manager = GridLayoutManager(this@_027MovieDetailsActivity, 3)
            rvActresses.layoutManager = manager
            RvItemClickSupport.addTo(rvActresses)
                .setOnItemClickListener(OnRvItemClickListener { _, _, position ->
                    val bean = movieInfo.actresses[position]
                    _027MovieListByLinkActivity.start(this@_027MovieDetailsActivity,
                        bean.name, bean.link)
                })
        }
        //添加截图
        if (movieInfo.screenshots.isNotEmpty()) {
            screenEmpty.hide(true)
            val thumb = movieInfo.screenshots.map { Uri.parse(it.thumbnailUrl) }
            val link = movieInfo.screenshots.map { Uri.parse(it.link) }
            mpScreenPic.set(thumb, link)
            mpScreenPic.setCallback(this)
        }
        //添加tag
        if (movieInfo.genres.isNotEmpty()) {
            tagEmpty.hide(true)
            movieInfo.genres.forEach { addTagItem(it) }
        }
    }

    private fun getMovieInfo(movie: Movie) {
        val cache = CacheUtils.getCache(this, "_027MovieInfo${movie.code}")
        if (StringUtils.isEmpty(cache).not()) {
            val movieInfo = Gson().fromJson<MovieInfo>(cache, MovieInfo::class.java)
            initMovieInfoUI(movieInfo)
            return
        }
        Observable.just(movie.link)
            .map { _027AvModel.decodeMovieInfo(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                initMovieInfoUI(it)
                logi { "MovieInfo >> put to cache" }
                CacheUtils.setCache(this,
                    "_027MovieInfo${movie.code}", Gson().toJson(it))
            }, {
                loge(it::toString)
            }).addToManaged()
    }

    private fun getMovieAddress(key: String) {
        HttpHelper.create(AvCodeApi::class.java)
            .queryAvCode(key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccess) {
                    createVideoItem(it)
                }
            }, {
                loge(it::toString)
            }).addToManaged()
    }

    private val mTvColor = Color.parseColor("#3CB034")
    private fun addTagItem(key: MovieInfo.Genre) {
        if (StringUtils.isEmpty(key.name)) return
        val textView = TextView(this)
        textView.text = key.name
        textView.tag = "${key.name}$*key*$${key.link}"
        textView.setTextColor(mTvColor)
        textView.setBackgroundResource(R.drawable.shape_027_category_item)
        textView.setOnClickListener(this)
        flowContent.addView(textView)
    }

    private fun createVideoItem(movieVideo: MovieVideo) {
        if (movieVideo.response == null || movieVideo.response.videos == null
            || movieVideo.response.videos.isEmpty()) return
        rvVideo.adapter = _027MovieInfoVideoAdapter(movieVideo.response.videos)
        val manager = GridLayoutManager(this@_027MovieDetailsActivity, 2)
        rvVideo.layoutManager = manager
        RvItemClickSupport.addTo(rvVideo)
            .setOnItemClickListener(OnRvItemClickListener { _, _, position ->
                val bean = movieVideo.response.videos[position]
                _004BiliBiliVideoActivity.start(this@_027MovieDetailsActivity,
                    bean.title, _027AvModel.getPlayAddress(bean.vid))
            })
    }

    @SuppressLint("SetTextI18n")
    private fun createHeard(info: MovieInfo) {
        val drawable = getDrawable(R.drawable._027_movie_info_arrow_right)
        drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        val padding = dip(8)
        info.headers.forEach {
            val tv = TextView(this)
            tv.setPadding(padding * 2, padding, padding, padding * 2)
            tv.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            tv.setLineSpacing(1.0f, 1.3f)
            tv.textSize = 18f
            val span = SpannableString(it.value)
            tv.setTextColor(Color.parseColor("#000000"))
            tv.text = "${it.name}\n"
            span.setSpan(ForegroundColorSpan(Color.parseColor("#333333")),
                0, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            span.setSpan(RelativeSizeSpan(0.7f), 0, span.length,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            tv.append(span)
            tv.setBackgroundResource(R.drawable.ripple_main_white)
            tv.setCompoundDrawables(null, null, drawable, null)
            tv.tag = "${it.value}$*key*$${it.link}"
            tv.setOnClickListener(this)
            llHeardContainer.addView(tv)
        }
    }

    override fun onClick(v: View) {
        val split = v.tag.toString().split("$*key*$")
        if (split.size == 2) {
            _027MovieListByLinkActivity.start(this, split[0], split[1])
        }
    }

    private val mPicHelper by lazy {
        ImageWatcherHelper.with(this, GlideSimpleLoader())
    }

    override fun onThumbPictureClick(i: ImageView?, imageGroupList: SparseArray<ImageView>?, urlList: MutableList<Uri>?) {
        mPicHelper.show(i, imageGroupList, urlList)
    }

    override fun onBackPressed() {
        if (!mPicHelper.handleBackPressed()) {
            super.onBackPressed()
        }
    }

}
