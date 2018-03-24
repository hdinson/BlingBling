package dinson.customview.model

import android.support.annotation.IntDef

/**
 * 七牛云配置文件
 */
class _005QiNiuConfig(val AccessKey: String,
                      val SecretKey: String,
                      val Domain: String,
                      val Bucket: String,
                      @AREA val Area: Int) {

    companion object {
        const val HUA_NAN = 1
        const val HUA_DONG = 2
        const val HUA_BEI = 3
        const val BEI_MEI = 4
    }

    @IntDef(HUA_DONG, HUA_NAN, HUA_BEI, BEI_MEI)
    @kotlin.annotation.Retention
    annotation class AREA
}