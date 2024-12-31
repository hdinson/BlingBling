package dinson.customview.model

import android.annotation.SuppressLint
import androidx.annotation.IntDef
import com.qiniu.common.Zone
import com.qiniu.storage.Region

/**
 * 七牛云配置文件
 */
class _005QiNiuConfig(val AccessKey: String,
                      val SecretKey: String,
                      val Domain: String,
                      val Bucket: String,
                      val isPrivate: Boolean,
                      @AREA val Area: Int) {

    companion object {
        const val HUA_DONG = 1
        const val HUA_BEI = 2
        const val HUA_NAN = 3
        const val BEI_MEI = 4
    }

    @IntDef(HUA_DONG, HUA_NAN, HUA_BEI, BEI_MEI)
    @kotlin.annotation.Retention
    annotation class AREA

    fun equals(other: _005QiNiuConfig?): Boolean {
        if (other == null) return false
        return AccessKey == other.AccessKey && SecretKey == other.SecretKey
            && Domain == other.Domain && Bucket == other.Bucket && Area == other.Area &&
            isPrivate == other.isPrivate
    }

    @SuppressLint("SwitchIntDef")
    fun getRegion() = when (this.Area) {
        HUA_DONG -> Region.huadong()
        HUA_BEI -> Region.huabei()
        HUA_NAN -> Region.huanan()
        BEI_MEI -> Region.beimei()
        else -> Region.autoRegion()
    }!!
}