package com.dinson.blingbase.kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.nfc.NfcManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.telephony.CellInfo
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * 判断nfc是否开启
 * @return true已开启  false未开启
 */
@Suppress("unused")
fun Context.isNfcEnable(): Boolean {
    val manager = getSystemService(Context.NFC_SERVICE) as? NfcManager
    return manager?.defaultAdapter?.isEnabled ?: false
}
/**
 *  设备信息相关
 */
/**
 * Return whether device is rooted.
 *
 * @return `true`: yes<br></br>`false`: no
 */
@Suppress("unused")
fun isDeviceRooted(): Boolean {
    val locations = arrayOf("/system/bin/", "/system/xbin/")
    return locations.any { File("${it}su").exists() }
}

// 获取手机CPU信息
@Suppress("unused")
fun getCpuInfo(): String {
    var reader: BufferedReader? = null
    return try {
        val fr = FileReader("/proc/cpuinfo")
        reader = BufferedReader(fr, 8192)
        val str2 = reader.readLine()
        str2.split(":")[1].trim()
    } catch (e: Exception) {
        e.printStackTrace()
        "unknown"
    } finally {
        reader?.close()
    }
}


/**
 * 电话状态：<br></br>
 * CALL_STATE_IDLE 无任何状态时
 * CALL_STATE_OFFHOOK 接起电话时
 * CALL_STATE_RINGING 电话进来时
 */
@Suppress("unused")
fun getCallState(manager: TelephonyManager): String = when (manager.callState) {
    CALL_STATE_IDLE -> "空闲"
    CALL_STATE_OFFHOOK -> "通话中"
    CALL_STATE_RINGING -> "响铃中"
    else -> "unknown"
}

/**
 * SIM的状态信息：<br></br>
 * SIM_STATE_UNKNOWN 未知状态 0<br></br>
 * SIM_STATE_ABSENT 没插卡 1<br></br>
 * SIM_STATE_PIN_REQUIRED 锁定状态，需要用户的PIN码解锁 2<br></br>
 * SIM_STATE_PUK_REQUIRED 锁定状态，需要用户的PUK码解锁 3<br></br>
 * SIM_STATE_NETWORK_LOCKED 锁定状态，需要网络的PIN码解锁 4<br></br>
 * SIM_STATE_READY 就绪状态 5
 */
@Suppress("unused")
fun getSimState(manager: TelephonyManager) = when (manager.simState) {
    SIM_STATE_ABSENT -> "未插卡"
    SIM_STATE_PIN_REQUIRED -> "锁定，需要PIN解锁"
    SIM_STATE_PUK_REQUIRED -> "锁定，需要PUK码解锁"
    SIM_STATE_NETWORK_LOCKED -> "锁定，需要网络的PIN码解锁"
    SIM_STATE_READY -> "就绪"
    else -> "unknown"
}

/**
 * 服务商名称：<br></br>
 * 例如：中国移动、联通<br></br>
 * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
 */
fun getSimOperatorName(manager: TelephonyManager): String = manager.simOperatorName

/**
 * 是否漫游:(在GSM用途下)
 */
fun isNetworkRoaming(manager: TelephonyManager): Boolean = manager.isNetworkRoaming


/**
 * 获取数据活动状态<br></br>
 * DATA_ACTIVITY_IN 数据连接状态：活动，正在接受数据<br></br>
 * DATA_ACTIVITY_OUT 数据连接状态：活动，正在发送数据<br></br>
 * DATA_ACTIVITY_INOUT 数据连接状态：活动，正在接受和发送数据<br></br>
 * DATA_ACTIVITY_NONE 数据连接状态：活动，无数据发送和接受<br></br>
 */
fun getDataActivity(manager: TelephonyManager) = when (manager.dataActivity) {
    DATA_ACTIVITY_IN -> "正在接受数据"
    DATA_ACTIVITY_OUT -> "正在发送数据"
    DATA_ACTIVITY_INOUT -> "正在接受和发送数据"
    DATA_ACTIVITY_NONE -> "无数据发送和接受"
    else -> "unknown"
}

/**
 * 获取数据连接状态<br></br>
 * DATA_CONNECTED 数据连接状态：已连接<br></br>
 * DATA_CONNECTING 数据连接状态：正在连接<br></br>
 * DATA_DISCONNECTED 数据连接状态：断开<br></br>
 * DATA_SUSPENDED 数据连接状态：暂停<br></br>
 */
fun getDataState(manager: TelephonyManager) = when (manager.dataState) {
    DATA_CONNECTED -> "已连接"
    DATA_CONNECTING -> "正在连接"
    DATA_DISCONNECTED -> "断开"
    DATA_SUSPENDED -> "暂停"
    else -> "unknown"
}

/**
 * 返回当前移动终端的位置
 * （通过google服务可以查看基站位置）
 */
@SuppressLint("MissingPermission")
fun getCellLocation(manager: TelephonyManager): MutableList<CellInfo>? = manager.allCellInfo


/**
 * SIM卡的序列号：<br></br>
 * 需要权限：READ_PHONE_STATE
 */
@SuppressLint("MissingPermission", "HardwareIds")
fun getSimSerialNumber( manager: TelephonyManager): String {
  return  manager.simSerialNumber
}


/**
 * 唯一的用户ID：<br></br>
 * 需要权限：READ_PHONE_STATE
 *  IMSI(国际移动用户识别码)：是区别移动用户的标志，储存在SIM卡中，可用于区别移动用户的有效信息。
 *
 *
 *IMSI共有15位，其结构如下：
 *MCC+MNC+MIN
 *MCC：Mobile Country Code，移动国家码，共3位，中国为460;
 *MNC:Mobile NetworkCode，移动网络码，共2位
 * MIN:是移动用户识别码，用以识别某一移动通信网中的移动用户。
 *在中国，移动的代码为电00和02，联通的代码为01，电信的代码为03
 *合起来就是（也是Android手机中APN配置文件中的代码）：
 *中国移动：46000 46002
 *中国联通：46001
 *中国电信：46003
 *举例，一个典型的IMSI号码为460030912121001
 *IMEI是International Mobile Equipment Identity （国际移动设备标识）的简称
 *IMEI由15位数字组成的”电子串号”，它与每台手机一一对应，而且该码是全世界唯一的
 *其组成为：
 *1. 前6位数(TAC)是”型号核准号码”，一般代表机型
 *2. 接着的2位数(FAC)是”最后装配号”，一般代表产地
 *3. 之后的6位数(SNR)是”串号”，一般代表生产顺序号
 *4. 最后1位数(SP)通常是”0″，为检验码，目前暂备用
 *
 */
@SuppressLint("MissingPermission", "HardwareIds")
fun getSubscriberId(manager: TelephonyManager): String = manager.subscriberId

/**
 * IMEI(国际移动身份识别码)：是由15位数字组成的”电子串号”，其组成结构为TAC（6位数字）+FAC（两位数字）+SNR（6位数字）+SP （1位数字）。
 * 它与每台手机一一对应，而且该码是全世界唯一的。每一只手机在组装完成后都将被赋予一个全球唯一的一组号码，
 * 这个号码从生产到交付使用都将被制造生产的厂商所记录。 IMEI码贴在手机背面的标志上，并且读写于手机内存中。它
 * 也是该手机在厂家的”档案”和”身份证号”。
 */
@SuppressLint("MissingPermission", "HardwareIds")
fun getIMEI(manager: TelephonyManager) = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) manager.meid else manager.deviceId)
    ?: "unknown"

/**
 * 手机号<br></br>
 *
 */
@SuppressLint("MissingPermission", "HardwareIds")
fun getLine1Number(manager: TelephonyManager): String = manager.line1Number ?: "unknown"


/**
 * 获取ISO国家码，相当于提供SIM卡的国家码。
 *
 * @return Returns the ISO country code equivalent for the SIM provider's
 * country code.
 */
fun getSimCountryIso(manager: TelephonyManager): String = manager.simCountryIso

/**
 * 获取SDCard的总大小和可用大小
 * @return [0]总大小 [1]可用大小
 */
fun getSDCardMemory(): LongArray {
    val sdCardInfo = LongArray(2)
    val state = Environment.getExternalStorageState()
    if (Environment.MEDIA_MOUNTED == state) {
        val sdcardDir = Environment.getExternalStorageDirectory()
        val sf = StatFs(sdcardDir.path)
        val bSize = sf.blockSizeLong
        val bCount = sf.blockCountLong
        val availBlocks = sf.availableBlocksLong

        sdCardInfo[0] = bSize * bCount//总大小
        sdCardInfo[1] = bSize * availBlocks//可用大小
    }
    return sdCardInfo
}