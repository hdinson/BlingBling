package dinson.customview.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import dinson.customview.BuildConfig
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview._global.ConstantsUtils
import dinson.customview.adapter._009ContentAdapter
import dinson.customview.download.DownloadManager
import dinson.customview.download.listener.HttpDownOnNextListener
import dinson.customview.download.model.DownloadState
import dinson.customview.download.utils.DbDownUtil
import dinson.customview.http.RxSchedulers
import dinson.customview.model._009ModelUtil
import dinson.customview.model._009PanoramaImageModel
import dinson.customview.utils.LogUtils
import dinson.customview.weight.recycleview.LinearItemDecoration
import dinson.customview.weight.recycleview.OnRvItemClickListener
import dinson.customview.weight.recycleview.RvItemClickSupport
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity__009_google_vr.*
import java.io.File

class _009GoogleVRActivity : BaseActivity() {

    private var mListDatas = _009ModelUtil.getPanoramaImageList()
    private var mAdapter = _009ContentAdapter(mListDatas)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__009_google_vr)

        initView()
    }

    private fun initView() {
        //顶部图片
        val imgUrl = "${BuildConfig.QINIU_URL}Fqp3F7wLs2rjSrMlA-9bSIIi27Of.webp"
        val rOptions = RequestOptions().error(R.drawable.def_img)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
        val tOptions = DrawableTransitionOptions().crossFade(500)
        Glide.with(this).load(imgUrl).transition(tOptions).apply(rOptions).into(ivImg)

        vrPanoramaView.apply {
            setTouchTrackingEnabled(true)
            setFullscreenButtonEnabled(true)
            setInfoButtonEnabled(false)
            setStereoModeButtonEnabled(false)
        }

        rvContent.apply {
            addItemDecoration(LinearItemDecoration(this@_009GoogleVRActivity))
            layoutManager = LinearLayoutManager(this@_009GoogleVRActivity)
            itemAnimator = null
            adapter = mAdapter
            RvItemClickSupport.addTo(this)
                .setOnItemClickListener(OnRvItemClickListener { _, _, position ->
                    onItemClick(position)
                })
        }
    }

    private fun loadPanoramaImage(model: _009PanoramaImageModel) {
        val file = File(model.localPath)
        LogUtils
            .e(String.format("File exists? %s and the path is %s", file.exists(), file.absoluteFile))

        Observable.just(model.localPath)
            .map {
                Glide.with(this).asBitmap().load(it)
                    .submit(SIZE_ORIGINAL, SIZE_ORIGINAL).get()
            }
            .compose(RxSchedulers.io_main())
            .subscribe { this.loadPanoramaImage(it) }
            .addToManaged()
    }


    private fun onItemClick(position: Int) {
        val selector = mListDatas[position]
        val transform = selector.transform()
        val downloadInfo = DbDownUtil.getInstance().queryDownBy(transform.url)
        if (downloadInfo == null) {
            transform.state = DownloadState.START
            transform.savePath = selector.localPath
            transform.listener = object : HttpDownOnNextListener<_009PanoramaImageModel>() {
                override fun onComplete() {
                    mAdapter.notifyItemChanged(position)
                }

                override fun updateProgress(readLength: Long, countLength: Long) {
                    selector.currentPos = readLength.toFloat()
                    selector.size = countLength.toFloat()
                    mAdapter.notifyItemChanged(position)
                }
            }
            DownloadManager.getInstance().startDown(transform)
        } else {
            loadPanoramaImage(selector)
        }
    }

    private fun loadPanoramaImage(bitmap: Bitmap) {
        val options = VrPanoramaView.Options()
        options.inputType = VrPanoramaView.Options.TYPE_MONO
        val childCount = vrPanoramaView.childCount
        if (childCount > 1) for (i in 1 until childCount) vrPanoramaView!!.removeViewAt(i)
        vrPanoramaView.loadImageFromBitmap(bitmap, options)
    }

    override fun onResume() {
        super.onResume()
        vrPanoramaView.resumeRendering()
    }

    override fun onPause() {
        super.onPause()
        vrPanoramaView.pauseRendering()
    }

    override fun onDestroy() {
        vrPanoramaView.shutdown()
        super.onDestroy()
    }

    override fun setWindowBackgroundColor(): Int? {
        return null
    }
}
