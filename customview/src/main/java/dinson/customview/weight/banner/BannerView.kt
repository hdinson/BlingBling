package dinson.customview.weight.banner

import android.content.Context
import android.os.Handler
import android.support.annotation.DrawableRes
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams.MATCH_PARENT
import android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT
import dinson.customview.R
import dinson.customview.utils.UIUtils.getScreenWidth
import dinson.customview.weight.banner.holder.MZViewHolder
import dinson.customview.weight.banner.transformer.CoverModeTransformer
import dinson.customview.weight.banner.transformer.ScaleYTransformer
import org.jetbrains.anko.dip
import java.util.*

/**
 *  广告轮播图控件
 */
class BannerView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

    private val mSettings: BannerViewSettings = BannerViewSettings(context, attrs)
    private val mIndicatorContainer: LinearLayout
    private val mViewPager: BannerViewPager = BannerViewPager(context)
    private val mPagerPadding = dip(30)//左右漏出模式下的padding值
    private var mDelayedTime = 3000// Banner 切换时间间隔
    private val mIndicatorRes = intArrayOf(R.drawable.indicator_normal, R.drawable.indicator_selected)
    private var mOnPageChangeListener: ViewPager.OnPageChangeListener? = null
    private var mBannerPageClickListener: BannerPageClickListener? = null
    private var mIsAutoPlay = true// 是否自动播放
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

        val pagerParams = RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        if (mSettings.isOpenMZEffect) {
            pagerParams.marginStart = mPagerPadding
            pagerParams.marginEnd = mPagerPadding
            mViewPager.clipChildren = false

            /* CoverModeTransformer:中间页面覆盖两边，和魅族APP 的banner 效果一样。
            * ScaleYTransformer:中间页面不覆盖，页面并排，只是Y轴缩小。*/
            mViewPager.setPageTransformer(mSettings.isMiddlePageCover,
                if (mSettings.isMiddlePageCover) CoverModeTransformer(mViewPager) else ScaleYTransformer())
        }
        addView(mViewPager, pagerParams)

        mIndicatorContainer = LinearLayout(context)
        val containerParams = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        containerParams.addRule(when (mSettings.indicatorAlign) {
            0 -> RelativeLayout.ALIGN_PARENT_LEFT
            1 -> RelativeLayout.CENTER_HORIZONTAL
            2 -> RelativeLayout.ALIGN_PARENT_RIGHT
            else -> Gravity.CENTER
        })
        containerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        mIndicatorContainer.orientation = LinearLayout.HORIZONTAL
        addView(mIndicatorContainer, containerParams)
    }


    /**
     * 设置数据，这是最重要的一个方法。
     * 其他的配置应该在这个方法之前调用
     *
     * @param datas  Banner 展示的数据集合
     * @param holder ViewHolder生成器
     */
    fun <T> setPages(datas: List<T>, holder: MZViewHolder<T>) {
        if (datas.isEmpty()) return
        //如果在播放，就先让播放停止
        pause()

        //增加一个逻辑：由于魅族模式会在一个页面展示前后页面的部分，因此，数据集合的长度至少为3,否则，自动为普通Banner模式
        //不管配置的:open_mz_mode 属性的值是否为true

        if (datas.size < 3) {
            mSettings.isOpenMZEffect = false
            val layoutParams = mViewPager.layoutParams as RelativeLayout.LayoutParams
            layoutParams.setMargins(0, 0, 0, 0)
            mViewPager.layoutParams = layoutParams
            clipChildren = true
            mViewPager.clipChildren = true
        }
        // 2017.7.20 fix：将Indicator初始化放在Adapter的初始化之前，解决更新数据变化更新时crush.
        //初始化Indicator
        initIndicator(datas.size)

        val mAdapter = MZPagerAdapter(datas, holder, datas.size != 1)
        mAdapter.setUpViewViewPager(mViewPager)
        mAdapter.setPageClickListener(mBannerPageClickListener)


        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                mOnPageChangeListener?.onPageScrolled(position % mIndicators.size,
                    positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                mCurrentItem = position

                val realPos = mCurrentItem % mIndicators.size
                // 切换indicator
                if (mIndicatorContainer.visibility == View.VISIBLE) {
                    mIndicators.forEachIndexed { index, it ->
                        it.setImageResource(if (index == realPos) mIndicatorRes[1] else mIndicatorRes[0])
                    }
                }
                // 不能直接将mOnPageChangeListener 设置给ViewPager ,否则拿到的position 是原始的positon
                mOnPageChangeListener?.onPageSelected(realPos)
            }

            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager.SCROLL_STATE_DRAGGING -> mIsAutoPlay = false
                    ViewPager.SCROLL_STATE_SETTLING -> mIsAutoPlay = true
                }
                mOnPageChangeListener?.onPageScrollStateChanged(state)
            }
        })
    }

    private var mCurrentItem = 0//当前位置
    private val mLoopRunnable = object : Runnable {
        override fun run() {
            if (mIsAutoPlay) {
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

        (0..count).forEach {
            val imageView = ImageView(context)
            val left = when (mSettings.isOpenMZEffect) {
                true -> if (it == 1) dip(16) + mPagerPadding else mSettings.indicatorMargin
                false -> if (it == 1) dip(16) else mSettings.indicatorMargin
            }
            val right = when (mSettings.isOpenMZEffect) {
                true -> if (it == count) dip(16) + mPagerPadding else mSettings.indicatorMargin
                false -> if (it == count) dip(16) else mSettings.indicatorMargin
            }
            imageView.setPadding(left, 0, right, 0)
            imageView.setImageResource(if (it == mCurrentItem % count) mIndicatorRes[1] else mIndicatorRes[0])

            mIndicators.add(imageView)
            mIndicatorContainer.addView(imageView)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (!mSettings.isCanLoop) return super.dispatchTouchEvent(ev)

        when (ev?.action) {
        // 按住Banner的时候，停止自动轮播
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_OUTSIDE,
            MotionEvent.ACTION_DOWN -> {
                val paddingLeft = mViewPager.left
                val touchX = ev.rawX
                // 如果是魅族模式，去除两边的区域
                if (touchX >= paddingLeft && touchX < getScreenWidth(context) - paddingLeft) {
                    mIsAutoPlay = false
                }
            }
            MotionEvent.ACTION_UP -> mIsAutoPlay = true
        }
        return super.dispatchTouchEvent(ev)
    }


    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/
    /**
     * 开始轮播
     *
     * <p>应该确保在调用用了[BannerView.setPages]之后调用这个方法开始轮播</p>
     */
    fun start() {
        // 如果Adapter为null, 说明还没有设置数据，这个时候不应该轮播Banner
        if (mIndicators.isEmpty()) return

        if (mSettings.isCanLoop) {
            mIsAutoPlay = true
            mHandler.postDelayed(mLoopRunnable, mDelayedTime.toLong())
        }
    }

    /**
     * 停止轮播
     */
    public fun pause() {
        mIsAutoPlay = false
        mHandler.removeCallbacks(mLoopRunnable)
    }

    /**
     * 是否支持轮播
     */
    fun setCanLoop(canLoop: Boolean) {
        mSettings.isCanLoop = canLoop
    }

    /**
     * 设置BannerView 的切换时间间隔
     *
     * @param delayedTime
     */
    fun setDelayedTime(delayedTime: Int) {
        mDelayedTime = delayedTime
    }

    fun addPageChangeLisnter(onPageChangeListener: ViewPager.OnPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener
    }

    /**
     * 添加Page点击事件
     *
     * @param bannerPageClickListener [BannerPageClickListener]
     */
    fun setBannerPageClickListener(bannerPageClickListener: BannerPageClickListener) {
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
        mIndicatorRes[0] = unSelectRes
        mIndicatorRes[1] = selectRes
    }

    /**
     * 设置ViewPager切换的速度
     *
     * @param duration 切换动画时间
     */
    fun setDuration(duration: Int) {
        mViewPagerScroller?.duration = duration
    }
}