package com.dinson.blingbase.widget.banner

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams.MATCH_PARENT
import android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT
import androidx.annotation.DrawableRes
import androidx.viewpager.widget.ViewPager
import com.dinson.blingbase.R
import com.dinson.blingbase.kotlin.dip
import com.dinson.blingbase.kotlin.screenWidth
import com.dinson.blingbase.widget.banner.holder.BannerViewHolder
import com.dinson.blingbase.widget.banner.transformer.CoverModeTransformer
import com.dinson.blingbase.widget.banner.transformer.ScaleYTransformer


/**
 *  广告轮播图控件
 */
@Suppress("unused")
class BannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val mSettings: BannerViewSettings = BannerViewSettings(context, attrs)
    private val mIndicatorContainer: LinearLayout
    private val mViewPager = BannerViewPager(context)
    private var mAdapter: BannerPagerAdapter<*>? = null
    private val mPagerPadding = dip(30)//左右漏出模式下的padding值
    private var mDelayedTime = 3000// Banner 切换时间间隔

    private var mOnPageChangeListener: ViewPager.OnPageChangeListener? = null
    private var mBannerPageClickListener: BannerPageClickListener? = null
    private val mHandler = Handler()
    private val mIndicators = ArrayList<ImageView>()
    private val mViewPagerScroller: ViewPagerScroller? by lazy {
        try {
            val field = ViewPager::class.java.getDeclaredField("mScroller")
            field.isAccessible = true
            val pagerScroller = ViewPagerScroller(context)
            field.set(mViewPager, pagerScroller)
            pagerScroller
        } catch (e: Exception) {
            null
        }
    }


    init {
        clipChildren = false

        val pagerParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        if (mSettings.mIsOpenMZEffect) {
            pagerParams.marginStart = mPagerPadding
            pagerParams.marginEnd = mPagerPadding
            mViewPager.clipChildren = false

            /* CoverModeTransformer:中间页面覆盖两边，和魅族APP 的banner 效果一样。
            * ScaleYTransformer:中间页面不覆盖，页面并排，只是Y轴缩小。*/
            mViewPager.setPageTransformer(
                mSettings.mIsMiddlePageCover,
                if (mSettings.mIsMiddlePageCover) CoverModeTransformer(mViewPager) else ScaleYTransformer()
            )
        }
        addView(mViewPager, pagerParams)

        mIndicatorContainer = LinearLayout(context)
        mIndicatorContainer.setPadding(dip(16), 0, dip(16), mSettings.mIndicatorMarginBottom)
        val containerParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        mIndicatorContainer.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        containerParams.addRule(
            when (mSettings.mIndicatorAlign) {
                0 -> ALIGN_PARENT_LEFT
                1 -> CENTER_HORIZONTAL
                2 -> ALIGN_PARENT_RIGHT
                else -> Gravity.CENTER
            }
        )
        if (mSettings.mIsOpenMZEffect) {
            containerParams.marginStart = mPagerPadding
            containerParams.marginEnd = mPagerPadding
        }
        containerParams.addRule(ALIGN_PARENT_BOTTOM)
        mIndicatorContainer.orientation = LinearLayout.HORIZONTAL
        addView(mIndicatorContainer, containerParams)
        //隐藏指示器
        if (mSettings.mHiddenIndicator) mIndicatorContainer.visibility = View.GONE
    }

    /**
     * 初始化数据
     */
    private fun <T> initData(datas: ArrayList<T>, holder: BannerViewHolder<T>) {
        when (datas.size) {
            0 -> return
            1 -> {
                mSettings.mIsCanLoop = false
                mSettings.mIsAutoLoop = false
                mSettings.mIsOpenMZEffect = false
                val params = mViewPager.layoutParams as LayoutParams
                params.marginEnd = 0
                params.marginStart = 0
                mViewPager.layoutParams = params
                setIndicatorVisibility(View.GONE)
            }
        }
        //如果在播放，就先让播放停止
        pause()

        // 2017.7.20 fix：将Indicator初始化放在Adapter的初始化之前，解决更新数据变化更新时crush.
        //初始化Indicator
        initIndicator(datas.size)
        setDuration(1000)


        while (mSettings.mIsCanLoop && datas.size < 5) {
            datas.addAll(datas)
        }

        mAdapter = BannerPagerAdapter(datas, holder, mSettings.mIsCanLoop)
        mAdapter!!.setUpViewViewPager(mViewPager)
        mAdapter!!.setPageClickListener(mBannerPageClickListener)

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                mOnPageChangeListener?.onPageScrolled(
                    position % mIndicators.size,
                    positionOffset, positionOffsetPixels
                )
            }

            override fun onPageSelected(position: Int) {
                mCurrentItem = position

                val realPos = mCurrentItem % mIndicators.size
                // 切换indicator
                if (mIndicatorContainer.visibility == View.VISIBLE) {
                    mIndicators.forEachIndexed { index, it ->
                        it.setImageResource(if (index == realPos) mSettings.mIndicatorSelect else mSettings.mIndicatorNormal)
                    }
                }
                // 不能直接将mOnPageChangeListener 设置给ViewPager ,否则拿到的position 是原始的positon
                mOnPageChangeListener?.onPageSelected(realPos)
            }

            override fun onPageScrollStateChanged(state: Int) {
                mOnPageChangeListener?.onPageScrollStateChanged(state)
            }
        })
    }

    private var mCurrentItem = 0//当前位置
    private val mLoopRunnable = object : Runnable {
        override fun run() {
            if (mSettings.mIsAutoLoop) {
                mCurrentItem = mViewPager.currentItem
                mCurrentItem++
                if (mCurrentItem == mIndicators.size - 1) {
                    mCurrentItem = 0
                    mViewPager.setCurrentItem(mCurrentItem, false)
                    mHandler.postDelayed(this, mDelayedTime.toLong())
                } else {
                    mViewPager.currentItem = mCurrentItem
                    mHandler.postDelayed(this, mDelayedTime.toLong())
                }
            } else {
                mHandler.postDelayed(this, mDelayedTime.toLong())
            }
        }
    }

    /**
     * 初始化指示器Indicator
     *
     * 这里要引用一个类 {@link AspectRatioImageView} <br/>
     */
    private fun initIndicator(count: Int) {
        mIndicatorContainer.removeAllViews()
        mIndicators.clear()

        (0 until count).forEach {
            val imageView = ImageView(context)
            imageView.setImageResource(if (it == mCurrentItem % count) mSettings.mIndicatorSelect else mSettings.mIndicatorNormal)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(mSettings.mIndicatorMargin, 0, mSettings.mIndicatorMargin, 0)
            imageView.layoutParams = params

            mIndicators.add(imageView)
            mIndicatorContainer.addView(imageView)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (!mSettings.mIsAutoLoop) return super.dispatchTouchEvent(ev)

        when (ev?.action) {
            //按住Banner的时候，停止自动轮播
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_OUTSIDE,
            MotionEvent.ACTION_DOWN -> {
                val paddingLeft = mViewPager.left
                val touchX = ev.rawX
                // 如果是魅族模式，去除两边的区域
                if (touchX >= paddingLeft && touchX < context.screenWidth() - paddingLeft) {
                    pause()
                }
            }
            MotionEvent.ACTION_UP -> start()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val hMode = MeasureSpec.getMode(heightMeasureSpec)

        val wSize = MeasureSpec.getSize(widthMeasureSpec)
        val hSize =
            if (hMode == MeasureSpec.AT_MOST || hMode == MeasureSpec.UNSPECIFIED) {//相当于我们设置为wrap_content
                (wSize / mSettings.mAspectRatio).toInt()
            } else {//相当于我们设置为match_parent或者为一个具体的值
                MeasureSpec.getSize(heightMeasureSpec)
            }
        mViewPager.layoutParams.apply {
            this.height = hSize
            this.width = wSize
        }
        setMeasuredDimension(wSize, hSize)

        /* val width = View.MeasureSpec.getSize(widthMeasureSpec)
         val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
         val height = (width / mSettings.aspectRatio()).toInt()
         val heightMeasure = View.MeasureSpec.makeMeasureSpec(height, heightMode)
         super.onMeasure(widthMeasureSpec, heightMeasure)*/
    }


    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/

    /**
     * 设置数据，这是最重要的一个方法。
     * 其他的配置应该在这个方法之前调用
     *
     * @param datas  Banner 展示的数据集合
     * @param holder ViewHolder生成器
     */
    fun <T> setPages(datas: ArrayList<T>, holder: BannerViewHolder<T>) {
        initData(datas, holder)
    }

    /**
     * 开始轮播
     *
     * <p>应该确保在调用用了[BannerView.setPages]之后调用这个方法开始轮播</p>
     */
    fun start() {
        // 如果Adapter为null, 说明还没有设置数据，这个时候不应该轮播Banner
        if (mIndicators.isEmpty()) return

        if (mSettings.mIsAutoLoop) {
            mHandler.postDelayed(mLoopRunnable, mDelayedTime.toLong())
        }
    }

    /**
     * 停止轮播
     */
    fun pause() {
        mHandler.removeCallbacks(mLoopRunnable)
    }

    /**
     * 是否支持轮播
     */
    fun setCanLoop(canLoop: Boolean) {
        mSettings.mIsCanLoop = canLoop
    }

    /**
     * 是否支持轮播
     */
    fun setAutoLoop(autoLoop: Boolean) {
        mSettings.mIsAutoLoop = autoLoop
    }


    /**
     * 设置BannerView 的切换时间间隔
     *
     * @param delayedTime
     */
    fun setDelayedTime(delayedTime: Int) {
        mDelayedTime = delayedTime
    }

    fun addPageChangeLisnter(onPageChangeListener: ViewPager.OnPageChangeListener?) {
        mOnPageChangeListener = onPageChangeListener
    }

    /**
     * 添加Page点击事件
     *
     * @param bannerPageClickListener [BannerPageClickListener]
     */
    fun setBannerPageClickListener(bannerPageClickListener: BannerPageClickListener?) {
        mBannerPageClickListener = bannerPageClickListener
    }

    /**
     * 是否显示Indicator
     *
     * @param visible true 显示Indicator，否则不显示
     */
    fun setIndicatorVisible(visible: Boolean) {
        mIndicatorContainer.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * 返回ViewPager
     *
     * @return [ViewPager]
     */
    fun getViewPager(): ViewPager = mViewPager

    /**
     * 设置indicator 图片资源
     *
     * @param unSelectRes 未选中状态资源图片
     * @param selectRes   选中状态资源图片
     */
    fun setIndicatorRes(@DrawableRes unSelectRes: Int, @DrawableRes selectRes: Int) {
        mSettings.mIndicatorNormal = unSelectRes
        mSettings.mIndicatorSelect = selectRes
    }

    /**
     * 设置ViewPager切换的速度
     *
     * @param duration 切换动画时间
     */
    fun setDuration(duration: Int) {
        mViewPagerScroller?.duration = duration
    }

    fun setIndicatorVisibility(visibility: Int) {
        mIndicatorContainer.visibility = visibility
    }

    /**
     * 添加page改变监听
     */
    fun addOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
        mViewPager.addOnPageChangeListener(listener)
    }


    /**
     * 标题栏设置
     */
    class BannerViewSettings internal constructor(
        context: Context, attrs: AttributeSet?
    ) {
        var mIndicatorMargin: Int
        var mIndicatorMarginBottom: Int
        var mIsOpenMZEffect = false
        var mIsMiddlePageCover: Boolean
        var mIsCanLoop: Boolean
        var mIsAutoLoop: Boolean
        var mHiddenIndicator: Boolean
        var mAspectRatio: Float
        var mIndicatorAlign: Int
        var mIndicatorNormal: Int
        var mIndicatorSelect: Int


        init {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.BannerView)
            mIndicatorNormal = attributes.getResourceId(
                R.styleable.BannerView_bvIndicatorNormal,
                R.drawable.shape_banner_indicator_point_normal
            )
            mIndicatorSelect = attributes.getResourceId(
                R.styleable.BannerView_bvIndicatorSelect,
                R.drawable.shape_banner_indicator_point_selector
            )

            mIndicatorMargin =
                attributes.getDimensionPixelOffset(R.styleable.BannerView_bvIndicatorMargin, dip(6))
            mIndicatorMarginBottom = attributes.getDimensionPixelOffset(
                R.styleable.BannerView_bvIndicatorMarginBottom,
                dip(6)
            )
            mIsOpenMZEffect = attributes.getBoolean(R.styleable.BannerView_bvIsOpenMZEffect, true)
            mIsMiddlePageCover =
                attributes.getBoolean(R.styleable.BannerView_bvIsMiddlePageCover, true)
            mIsCanLoop = attributes.getBoolean(R.styleable.BannerView_bvIsCanLoop, true)
            mIsAutoLoop = attributes.getBoolean(R.styleable.BannerView_bvIsAutoLoop, true)
            mHiddenIndicator =
                attributes.getBoolean(R.styleable.BannerView_bvHiddenIndicator, false)
            mAspectRatio = attributes.getFloat(R.styleable.BannerView_bvAspectRatio, 2f)
            mIndicatorAlign = attributes.getInteger(R.styleable.BannerView_bvIndicatorAlign, 1)
            attributes.recycle()
        }
    }
}