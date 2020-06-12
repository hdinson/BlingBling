package com.dinson.blingbase.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.dinson.blingbase.R

/**
 * 通用标题栏
 */
@Suppress("unused")
class CommonTitleBar : FrameLayout {
    /**
     * 获取中间标题控件
     */
    private lateinit var titleTextTv: TextView

    /**
     * 获取右边标题控件
     */
    private lateinit var titleRightTv: TextView

    /**
     * 获取右边按钮
     */
    private lateinit var titleRightBtn: ImageButton

    /**
     * 获取左边按钮
     */
    private lateinit var titleLeftBtn: ImageButton

    private lateinit var mSettings: CommonTitleBarSettings

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
        context: Context, attrs: AttributeSet, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    /**
     * 设置左边标题
     */
    fun setTitleLeft(title: String?) {
        titleTextTv.text = title
    }

    /**
     * 设置标题背景颜色
     *
     * @param color 颜色值（#ff0000）
     */
    fun setTitleBgColor(color: String?) {
        setBackgroundColor(Color.parseColor(color))
    }

    /**
     * 设置左边按钮显示隐藏
     *
     * @param visible true：显示   false：隐藏
     */
    fun setLeftBtnVisible(visible: Boolean) {
        titleLeftBtn.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * 初始化
     */
    private fun init(
        context: Context, attrs: AttributeSet
    ) {
        mSettings = CommonTitleBarSettings(context, attrs)
        this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val view = LayoutInflater.from(context)
            .inflate(R.layout.common_title_bar, this, true)

        //标题
        titleTextTv = view.findViewById(R.id.tvTitleLeft)
        titleTextTv.text = mSettings.titleText   //设置标题文字
        titleTextTv.setTextColor(mSettings.titleTextColor) //设置标题颜色

        //左边按钮
        titleLeftBtn = view.findViewById(R.id.ibTitleLeft)
        titleLeftBtn.setImageResource(mSettings.leftBtnDrawable)
        titleLeftBtn.visibility = if (mSettings.isLeftBtnVisible) View.VISIBLE else View.GONE

        //右边按钮
        titleRightBtn = view.findViewById(R.id.ibTitleRight)
        titleRightBtn.setImageResource(mSettings.rightBtnDrawable)
        titleRightBtn.visibility = if (mSettings.isRightBtnVisible) View.VISIBLE else View.GONE

        //右边标题
        titleRightTv = view.findViewById(R.id.tvTitleRight)
        titleRightTv.text = mSettings.rightText
        titleRightTv.setTextColor(mSettings.rightTextColor)
        titleRightTv.visibility = if (mSettings.isRightTvVisible) View.VISIBLE else View.GONE

        //左右按钮默认关闭
        if (mSettings.backGroundResource > 0)
            setBackgroundResource(mSettings.backGroundResource)
        transparentPadding(context, this, mSettings.ancestorFitsSystemWindow)
    }


    /////////////////////////////以下解决标题栏问题//////////////////////////////////////////

    /**
     * 动态计算顶部padding
     */
    private fun transparentPadding(
        context: Context, view: View, ancestorFitsSystemWindow: Boolean
    ) {
        if (!ancestorFitsSystemWindow) {
            val resources = context.resources
            val barSize = getStatusBarSize(resources)
            if (barSize > 0) {
                val layoutParams = view.layoutParams
                if (layoutParams != null) {
                    layoutParams.height += barSize
                    view.setPadding(view.paddingLeft, view.paddingTop + barSize, view.paddingRight, view.paddingBottom)
                }
            }
        }
    }

    /**
     * 获取statusBar大小
     */
    private fun getStatusBarSize(res: Resources): Int {
        var result = 0
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
        return result
    }


    /**
     * 标题栏设置
     */
    class CommonTitleBarSettings internal constructor(context: Context, attrs: AttributeSet?) {
        var titleText: String? = ""
        var rightText: String? = ""
        val isLeftBtnVisible: Boolean
        val isRightBtnVisible: Boolean
        val isRightTvVisible: Boolean
        val ancestorFitsSystemWindow: Boolean
        val titleTextColor: Int
        val rightTextColor: Int
        val leftBtnDrawable: Int
        val rightBtnDrawable: Int
        val backGroundResource: Int

        init {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar)
            isLeftBtnVisible = attributes.getBoolean(R.styleable.CommonTitleBar_leftBtnVisible, false)
            isRightBtnVisible = attributes.getBoolean(R.styleable.CommonTitleBar_rightBtnVisible, false)
            isRightTvVisible = attributes.getBoolean(R.styleable.CommonTitleBar_rightTextVisible, false)
            leftBtnDrawable = attributes.getResourceId(R.styleable.CommonTitleBar_leftBtnDrawable, R.drawable.ic_baseline_arrow_back_24)
            rightBtnDrawable = attributes.getResourceId(R.styleable.CommonTitleBar_rightBtnDrawable, R.drawable.ic_baseline_arrow_back_24)
            titleText = attributes.getString(R.styleable.CommonTitleBar_titleText)
            titleTextColor = attributes.getColor(R.styleable.CommonTitleBar_titleTextColor, Color.BLACK)
            rightText = attributes.getString(R.styleable.CommonTitleBar_rightText)
            rightTextColor = attributes.getColor(R.styleable.CommonTitleBar_rightTextColor, Color.BLACK)
            ancestorFitsSystemWindow = attributes.getBoolean(R.styleable.CommonTitleBar_ancestorFitsSystemWindow, false)

            backGroundResource =
                attributes.getResourceId(R.styleable.CommonTitleBar_backgroundResource, -1)

            attributes.recycle()
        }
    }
}