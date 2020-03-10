package dinson.blingxposed

import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import dinson.blingxposed.kotlin.logi


class XposedInit : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        Log.i("test", "------------------- hook --------------------")
        XposedBridge.log("------------------- hook2 --------------------")
        lpparam?.let { "xposed: ${it.packageName} - ${it.processName} - ${it.appInfo.className} ${it.appInfo}".logi() }

        if (lpparam?.packageName.equals("com.aaa.xposedhook")) {
            XposedBridge.log("XposedInit has hooked!")
            val clazz = lpparam!!.classLoader.loadClass("com.aaa.xposedhook.MainActivity")
            XposedHelpers.findAndHookMethod(clazz, "toastMessage", object : XC_MethodHook() {

                override fun afterHookedMethod(param: MethodHookParam?) {
                    param?.result = "You are hooked!"
                }
            })
        }
    }

}