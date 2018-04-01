package dinson.customview.weight.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import dinson.customview.R
import dinson.customview.kotlin.click
import dinson.customview.kotlin.show
import dinson.customview.kotlin.toast
import dinson.customview.model._005QiNiuConfig
import dinson.customview.utils.SPUtils
import kotlinx.android.synthetic.main.dialog_005_qi_niu_config.*

/**
 *  七牛云参数设置对话框
 */
class _005DialogQiNiuConfig(context: Context, private val mConfig: _005QiNiuConfig? = null, theme: Int = R.style.BaseDialogTheme)
    : Dialog(context, theme) {


    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/


    /******************************************************************************************************/
    /**                             内部实现                                                              **/
    /******************************************************************************************************/

    init {
        //设置是否可取消
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        setContentView(R.layout.dialog_005_qi_niu_config)


        mConfig?.let {
            btnDel.show()
            etAK.setText(it.AccessKey)
            etSK.setText(it.SecretKey)
            etBucket.setText(it.Bucket)
            etDomain.setText(it.Domain)
            rgArea.check(getId(it.Area))
        }
        initClick()
    }

    /**
     * 初始化各按钮的点击事件
     */
    private fun initClick() {
        btnSave.click {
            if (etAK.text.isEmpty() || etSK.text.isEmpty() ||
                etBucket.text.isEmpty() || etDomain.text.isEmpty()) {
                "请完善参数后提交".toast()
                return@click
            }
            val config = _005QiNiuConfig(etAK.text.toString(), etSK.text.toString(), etDomain.text.toString(),
                etBucket.text.toString(), getArea(rgArea.checkedRadioButtonId))

            if (mConfig == null) {
                SPUtils.addQiNiuConfig(context, config)
            } else {
                SPUtils.updateQiNiuDefault(context, config)
            }
            dismiss()
        }

        btnDel.click {
            mConfig?.let {
                SPUtils.removeQiNiuConfig(context, it)
                dismiss()
            }
        }
    }

    private fun getArea(checkedRadioButtonId: Int): Int {
        return when (checkedRadioButtonId) {
            R.id.rb1 -> _005QiNiuConfig.HUA_DONG
            R.id.rb2 -> _005QiNiuConfig.HUA_BEI
            R.id.rb3 -> _005QiNiuConfig.HUA_NAN
            R.id.rb4 -> _005QiNiuConfig.BEI_MEI
            else -> _005QiNiuConfig.HUA_DONG
        }
    }

    private fun getId(area: Int): Int {
        return when (area) {
            _005QiNiuConfig.HUA_DONG -> R.id.rb1
            _005QiNiuConfig.HUA_BEI -> R.id.rb2
            _005QiNiuConfig.HUA_NAN -> R.id.rb3
            _005QiNiuConfig.BEI_MEI -> R.id.rb4
            else -> R.id.rb1
        }
    }
}