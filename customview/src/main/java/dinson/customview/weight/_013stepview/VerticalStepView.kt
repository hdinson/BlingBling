package dinson.customview.weight._013stepview

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.RelativeLayout
import dinson.customview.utils.LogUtils
import kotlinx.android.synthetic.main.activity__003_exchange.view.*

/**
 * 步骤指示器
 */
class VerticalStepView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private var mSetting: StepViewSettings = StepViewSettings(context, attrs)
    private var mCompletingPosition: Int = -1   //当前的位置

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
        if (mCompletingPosition < 0) mCompletingPosition = listData.size
        listData.forEachIndexed({ index, any ->
            if (any == null) return@forEachIndexed

            val container = RelativeLayout(context)
            container.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 500)

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
            //指示器
            val indicator = VerticalStepViewIndicator(context, pos, state, mSetting)
            indicator.id = View.generateViewId()
            //布局视图
            val view = holder.onBind(context, any)
            view.id = View.generateViewId()
            view.minimumHeight=300

            val params1 = RelativeLayout.LayoutParams(300, MATCH_PARENT)
            params1.addRule(RelativeLayout.ALIGN_TOP, indicator.id)
            params1.addRule(RelativeLayout.ALIGN_BOTTOM, indicator.id)
            container.addView(indicator, params1)
            //添加绑定后的布局
            LogUtils.e("------------$index $any-----------------")
            val params2 = RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            params2.addRule(RelativeLayout.RIGHT_OF, indicator.id)
            container.addView(view, params2)

            addView(container)

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
               val params2 = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
               params2.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
               params2.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
               params2.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
               params2.leftToRight = indicator.id
               constraintLayout.addView(view, params2)
               view.requestLayout()
               view.invalidate()
           }
           addView(constraintLayout)*/
        })
    }


    /**
     * 设置正在进行的position(默认最后一个)
     */
    fun setStepsViewIndicatorCompletingPosition(completingPosition: Int): VerticalStepView {
        mCompletingPosition = completingPosition
        return this
    }

    /**
     * 设置未完成文字的颜色
     */
    fun setStepViewUnCompletedTextColor(unCompletedTextColor: Int): VerticalStepView {
        mSetting.unCompletedTextColor = unCompletedTextColor
        return this
    }

    /**
     * 设置完成文字的颜色
     */
    fun setStepViewCompletedTextColor(complectedTextColor: Int): VerticalStepView {
        mSetting.completedTextColor = complectedTextColor
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
        mSetting.indicatorDefaultIcon = defaultIcon
        return this
    }

    /**
     * 设置StepsViewIndicator已完成图片
     */
    fun setStepsViewIndicatorCompleteIcon(completeIcon: Drawable?): VerticalStepView {
        mSetting.indicatorCompleteIcon = completeIcon
        return this
    }

    /**
     * 设置StepsViewIndicator正在进行中的图片
     */
    fun setStepsViewIndicatorAttentionIcon(attentionIcon: Drawable?): VerticalStepView {
        mSetting.indicatorAttentionIcon = attentionIcon
        return this
    }

    /**
     * is reverse draw 是否倒序画（默认true）
     */
    fun reverseDraw(isReverse: Boolean): VerticalStepView {
        mSetting.isReverseDraw = isReverse
        return this
    }

    /**
     * set linePadding  proportion 设置线间距的比例系数
     */
    fun setLinePaddingProportion(linePaddingProportion: Float): VerticalStepView {
        mSetting.linePaddingProportion = linePaddingProportion
        return this
    }
}
