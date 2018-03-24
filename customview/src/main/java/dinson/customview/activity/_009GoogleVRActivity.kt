package dinson.customview.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.vr.sdk.widgets.pano.VrPanoramaView

import java.io.File

import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._009ContentAdapter
import dinson.customview.download.DownloadManager
import dinson.customview.download.listener.HttpDownOnNextListener
import dinson.customview.download.model.DownloadState
import dinson.customview.download.utils.DbDownUtil
import dinson.customview.model._009ModelUtil
import dinson.customview.model._009PanoramaImageModel
import dinson.customview.utils.LogUtils
import dinson.customview.utils.UIUtils
import dinson.customview.weight.recycleview.LinearItemDecoration
import dinson.customview.weight.recycleview.OnItemClickListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import kotlinx.android.synthetic.main.activity__009_google_vr.*
import kotlinx.android.synthetic.main.activity__009_google_vr.view.*

class _009GoogleVRActivity : BaseActivity(), OnItemClickListener.OnClickListener {

    private var mListDatas = _009ModelUtil.getPanoramaImageList()
    private var mAdapter = _009ContentAdapter(this, mListDatas)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__009_google_vr)

        initView()
    }

    private fun initView() {
        //顶部图片
        val imgUrl = "http://ondlsj2sn.bkt.clouddn.com/Fqp3F7wLs2rjSrMlA-9bSIIi27Of.webp"
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
            addOnItemTouchListener(OnItemClickListener(this@_009GoogleVRActivity, rvContent,
                this@_009GoogleVRActivity))
            adapter = mAdapter
        }
    }

    private fun loadPanoramaImage(model: _009PanoramaImageModel) {
        val file = File(model.localPath)
        LogUtils
            .e(String.format("File exists? %s and the path is %s", file.exists(), file.absoluteFile))

        Observable.just(model.localPath)
            .map {
                Glide.with(UIUtils.getContext()).asBitmap().load(it)
                    .submit(SIZE_ORIGINAL, SIZE_ORIGINAL).get()
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ this.loadPanoramaImage(it) })
    }


    override fun onItemClick(view: View, position: Int) {
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

    private fun loadPanoramaImage(bitmap: Bitmap ) {
        val options = VrPanoramaView.Options()
        options.inputType = VrPanoramaView.Options.TYPE_MONO
        val childCount = vrPanoramaView .childCount
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

    override fun setWindowBackgroundColor() = null
}
