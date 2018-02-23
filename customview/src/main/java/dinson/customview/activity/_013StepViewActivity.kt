package dinson.customview.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.holder._013StepViewHolder
import kotlinx.android.synthetic.main.activity__013_step_view.*


class _013StepViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarTintColor(ContextCompat.getColor(this, R.color._013_title_bg))
        setContentView(R.layout.activity__013_step_view)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val list0 = arrayListOf(
            "您已提交定单，等待系统确认",
            "您的商品需要从外地调拨，我们会尽快处理，请耐心等待",
            "您的订单已经进入亚洲第一仓储中心1号库准备出库",
            "您的订单预计6月23日送达您的手中，618期间促销火爆，可能影响送货时间，请您谅解，我们会第一时间送到您的手中",
            "您的订单已打印完毕",
            "您的订单已拣货完成",
            "扫描员已经扫描",
            "打包成功",
            "您的订单在京东【华东外单分拣中心】发货完成，准备送往京东【北京通州分拣中心】",
            "您的订单在京东【北京通州分拣中心】分拣完成",
            "您的订单在京东【北京通州分拣中心】发货完成，准备送往京东【北京中关村大厦站】",
            "您的订单在京东【北京中关村大厦站】验货完成，正在分配配送员",
            "配送员【包牙齿】已出发，联系电话【130-0000-0000】，感谢您的耐心等待，参加评价还能赢取好多礼物哦配送员【包牙齿】已出发，联系电话",
            "感谢你在京东购物，欢迎你下次光临！")
        step_view0.setStepsViewIndicatorCompletingPosition(list0.size - 6)//设置完成的步数
            .setDashLineIntervals(8)
            .setReverseDraw(true)
            .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, android.R.color.white))//设置StepsViewIndicator完成线的颜色
            .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.white))//设置StepsViewIndicator未完成线的颜色
            .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable._013_complted))//设置StepsViewIndicator CompleteIcon
            .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable._013_default_icon))//设置StepsViewIndicator DefaultIcon
            .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable._013_attention))//设置StepsViewIndicator AttentionIcon
            .setStepView(list0, _013StepViewHolder())//总步骤
    }
}
