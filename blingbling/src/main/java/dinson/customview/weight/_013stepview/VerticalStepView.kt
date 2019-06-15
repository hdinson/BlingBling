package dinson.customview.weight._013stepview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.RelativeLayout
import dinson.customview.kotlin.dip

/**
 * 步骤指示器
 */
class VerticalStepView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private val mSetting: StepViewSettings = StepViewSettings(context, attrs)
    private var mCompletingPosition: Int = -1   //当前的位置
    private val mItemMinHeight = 170            //每个步骤最小的高度
    private val mItemPaddingTopAndBottom = 50     //每个步骤的item都有一个paddingTop和paddingBottom

    init {
        orientation = if (mSetting.isVertical) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL
    }


    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/
    /**
     * 设置显示的数据(最后调用)
     *
     * @param listData 数据集
     * @param holder holder
     */
    fun <T> setStepView(listData: List<T>, holder: StepViewHolder<T>) {
        if (listData.isEmpty()) return

        removeAllViews()
        if (mCompletingPosition < 0) mCompletingPosition = listData.size - 1

        listData.forEachIndexed { index, any ->
            if (any == null) return@forEachIndexed

            val container = RelativeLayout(context)
            container.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

            val pos = when (index) {
                0 -> Position.Start
                listData.size - 1 -> Position.End
                else -> Position.Middle
            }

            val state = when {
                index < mCompletingPosition -> State.Completed
                index == mCompletingPosition -> State.Completing
                else -> State.UnCompleted
            }
            //指示器
            val indicator = VerticalStepViewIndicator(context, pos, state, mItemPaddingTopAndBottom, mSetting)
            indicator.id = View.generateViewId()
            indicator.setPadding(mSetting.indicatorPaddingLeftAndRight, 0, mSetting.indicatorPaddingLeftAndRight, 0)
            //布局视图
            val view = holder.onBind(context, any, pos, state)
            view.id = View.generateViewId()
            view.minimumHeight = mItemMinHeight
            val params1 = RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            params1.addRule(RelativeLayout.ALIGN_TOP, view.id)
            params1.addRule(RelativeLayout.ALIGN_BOTTOM, view.id)
            container.addView(indicator, params1)
            //添加绑定后的布局
            val params2 = RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            params2.addRule(RelativeLayout.RIGHT_OF, indicator.id)
            view.setPadding(view.left, mItemPaddingTopAndBottom, mItemPaddingTopAndBottom, mItemPaddingTopAndBottom)
            container.addView(view, params2)

            if (mSetting.isReverseDraw) {
                addView(container, 0)
            } else {
                addView(container)
            }


            //-----------------------------------以下用ConstraintLayout有BUG----------------------------------//
            /*val constraintLayout = ConstraintLayout(context)
               constraintLayout.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

               val pos = when (index) {
                   0 -> VerticalStepViewIndicator.Position.Start
                   listData.size -> VerticalStepViewIndicator.Position.End
                   else -> VerticalStepViewIndicator.Position.Middle
               }

               val state = when {
                   index < mCompletingPosition -> VerticalStepViewIndicator.State.Completed
                   index == mCompletingPosition -> VerticalStepViewIndicator.State.Completing
                   else -> VerticalStepViewIndicator.State.UnCompleted
               }
               //添加指示器
               val indicator = VerticalStepViewIndicator(context, pos, state, mSetting)
               indicator.id= View.generateViewId()
               val params1 = ConstraintLayout.LayoutParams(300, 300)
               params1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
               params1.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
               params1.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
               constraintLayout.addView(indicator, params1)
               //添加绑定后的布局
               if (any != null) {
                   val view = holder.onBind(context, any)
                   LogUtils.e("------------$index $any-----------------")
                   val params2 = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                   params2.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                   params2.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                   params2.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
                   params2.leftToRight = indicator.id
                   constraintLayout.addView(view, params2)
                   view.requestLayout()
                   view.invalidate()
               }
               addView(constraintLayout)*/
        }
    }


    /**
     * 设置正在进行的position(默认最后一个)
     */
    fun setStepsViewIndicatorCompletingPosition(completingPosition: Int): VerticalStepView {
        mCompletingPosition = completingPosition
        return this
    }


    /**
     * 设置StepsViewIndicator未完成线的颜色
     */
    fun setStepsViewIndicatorUnCompletedLineColor(unCompletedLineColor: Int): VerticalStepView {
        mSetting.indicatorUnCompletedLineColor = unCompletedLineColor
        return this
    }

    /**
     * 设置StepsViewIndicator完成线的颜色
     */
    fun setStepsViewIndicatorCompletedLineColor(completedLineColor: Int): VerticalStepView {
        mSetting.indicatorCompletedLineColor = completedLineColor
        return this
    }

    /**
     * 设置StepsViewIndicator默认图片
     */
    fun setStepsViewIndicatorDefaultIcon(defaultIcon: Drawable?): VerticalStepView {
        mSetting.setIndicatorDefaultIcon(defaultIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator已完成图片
     */
    fun setStepsViewIndicatorCompleteIcon(completeIcon: Drawable?): VerticalStepView {
        mSetting.setIndicatorCompleteIcon(completeIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator正在进行中的图片
     */
    fun setStepsViewIndicatorAttentionIcon(attentionIcon: Drawable?): VerticalStepView {
        mSetting.setIndicatorAttentionIcon(attentionIcon)
        return this
    }

    /**
     * is reverse draw 是否倒序画（默认False）
     */
    fun setReverseDraw(isReverse: Boolean): VerticalStepView {
        mSetting.isReverseDraw = isReverse
        return this
    }

    /**
     * 设置虚线的虚实间隔
     */
    fun setDashLineIntervals(dp: Int): VerticalStepView {
        mSetting.dashLineIntervals = dip(dp).toFloat()
        return this
    }

    /**
     * 设置指示器图标的高
     */
    fun setIndicatorHeight(height: Float): VerticalStepView {
        mSetting.indicatorHeight = height
        return this
    }

    /**
     * 设置指示器图标的宽
     */
    fun setIndicatorWidth(width: Float): VerticalStepView {
        mSetting.indicatorWidth = width
        return this
    }

    /**
     * 设置指示器的线宽
     */
    fun setIndicatorLineWidth(width: Float): VerticalStepView {
        mSetting.indicatorLineWidth = width
        return this
    }

    /**
     * 设置指示器的左右padding
     */
    fun setIndicatorPaddingLeftAndRight(padding: Int): VerticalStepView {
        mSetting.indicatorPaddingLeftAndRight = padding
        return this
    }
}