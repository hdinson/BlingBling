package com.dinson.blingbase.crash

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.util.Log
import androidx.annotation.RestrictTo
import com.dinson.blingbase.kotlin.getAppName
import com.dinson.blingbase.kotlin.loge
import com.dinson.blingbase.kotlin.logv
import com.dinson.blingbase.kotlin.logw
import java.io.*
import java.lang.ref.WeakReference
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipFile
import kotlin.system.exitProcess

object CrashTool {
    private const val TAG = "CrashTool"
    private const val EXTRA_CONFIG = "com.dinson.blingbase.EXTRA_CONFIG"
    private const val EXTRA_STACK_TRACE = "com.dinson.blingbase.EXTRA_STACK_TRACE"
    private const val EXTRA_ACTIVITY_LOG = "com.dinson.blingbase.EXTRA_ACTIVITY_LOG"
    private const val INTENT_ACTION_ERROR_ACTIVITY = "com.dinson.blingbase.ERROR"
    private const val INTENT_ACTION_RESTART_ACTIVITY = "com.dinson.blingbase.RESTART"
    private const val TAM_HANDLER_PACKAGE_NAME = "com.dinson.blingbase"
    private const val DEFAULT_HANDLER_PACKAGE_NAME = "com.android.internal.os"
    private const val TIME_TO_CONSIDER_FOREGROUND_MS = 500
    private const val MAX_STACK_TRACE_SIZE = 131071 //128 KB - 1
    private const val MAX_ACTIVITIES_IN_LOG = 50
    private const val SHARED_PREFERENCES_FILE = "CrashTool"
    private const val SHARED_PREFERENCES_FIELD_TIMESTAMP = "last_crash_timestamp"
    private val activityLog: Deque<String> = ArrayDeque(MAX_ACTIVITIES_IN_LOG)

    @SuppressLint("StaticFieldLeak")
    private var application: Application? = null
    private var config = CrashProfile()
    private var lastActivityCreated = WeakReference<Activity?>(null)
    private var lastActivityCreatedTimestamp = 0L
    private var isInBackground = true

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun install(context: Context?) {
        try {
            if (context == null) {
                "Install failed: context is null!".loge()
            } else {
                //INSTALL!
                val oldHandler = Thread.getDefaultUncaughtExceptionHandler()
                if (oldHandler != null && oldHandler.javaClass.name.startsWith(TAM_HANDLER_PACKAGE_NAME)) {
                    "CrashTool was already installed, doing nothing!".logv()
                } else {
                    if (oldHandler != null && !oldHandler.javaClass.name.startsWith(DEFAULT_HANDLER_PACKAGE_NAME)) {
                        "IMPORTANT WARNING! You already have an UncaughtExceptionHandler".logw()
                    }
                    application = context.applicationContext as Application
                    Thread.setDefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler { thread, throwable ->
                        if (config.isEnabled()) {
                            "App has crashed, executing TCrashTool's UncaughtExceptionHandler".loge()
                            if (hasCrashedInTheLastSeconds(application!!)) {
                                Log.e(TAG, "App already crashed recently, not starting custom error activity because we could enter a restart loop. Are you sure that your app does not crash directly on init?", throwable)
                                if (oldHandler != null) {
                                    oldHandler.uncaughtException(thread, throwable)
                                    return@UncaughtExceptionHandler
                                }
                            } else {
                                setLastCrashTimestamp(application!!, Date().time)
                                var errorActivityClass = config.getErrorActivityClass()
                                if (errorActivityClass == null) {
                                    errorActivityClass = guessErrorActivityClass(application!!)
                                }
                                if (isStackTraceLikelyConflictive(throwable, errorActivityClass)) {
                                    if (oldHandler != null) {
                                        oldHandler.uncaughtException(thread, throwable)
                                        return@UncaughtExceptionHandler
                                    }
                                } else if (config.getBackgroundMode() == CrashProfile.BACKGROUND_MODE_SHOW_CUSTOM || !isInBackground
                                    || lastActivityCreatedTimestamp >= Date().time - TIME_TO_CONSIDER_FOREGROUND_MS) {
                                    val intent = Intent(application, errorActivityClass)
                                    val sw = StringWriter()
                                    val pw = PrintWriter(sw)
                                    throwable.printStackTrace(pw)
                                    var stackTraceString = sw.toString()

                                    if (stackTraceString.length > MAX_STACK_TRACE_SIZE) {
                                        val disclaimer = " [stack trace too large]"
                                        stackTraceString = stackTraceString.substring(0, MAX_STACK_TRACE_SIZE - disclaimer.length) + disclaimer
                                    }
                                    intent.putExtra(EXTRA_STACK_TRACE, stackTraceString)
                                    if (config.isTrackActivities()) {
                                        val activityLogStringBuilder = StringBuilder()
                                        while (!activityLog.isEmpty()) {
                                            activityLogStringBuilder.append(activityLog.poll())
                                        }
                                        intent.putExtra(EXTRA_ACTIVITY_LOG, activityLogStringBuilder.toString())
                                    }
                                    if (config.isShowRestartButton() && config.getRestartActivityClass() == null) {
                                        config.setRestartActivityClass(guessRestartActivityClass(application!!))
                                    }
                                    intent.putExtra(EXTRA_CONFIG, config)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    if (config.getEventListener() != null) {
                                        config.getEventListener()!!.onLaunchErrorActivity()
                                    }
                                    application!!.startActivity(intent)
                                } else if (config.getBackgroundMode() == CrashProfile.BACKGROUND_MODE_CRASH) {
                                    if (oldHandler != null) {
                                        oldHandler.uncaughtException(thread, throwable)
                                        return@UncaughtExceptionHandler
                                    }
                                }
                            }
                            val lastActivity = lastActivityCreated.get()
                            if (lastActivity != null) {
                                lastActivity.finish()
                                lastActivityCreated.clear()
                            }
                            killCurrentProcess()
                        } else oldHandler?.uncaughtException(thread, throwable)
                    })
                    application!!.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                        var currentlyStartedActivities = 0
                        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
                            if (activity.javaClass != config.getErrorActivityClass()) {
                                lastActivityCreated = WeakReference(activity)
                                lastActivityCreatedTimestamp = Date().time
                            }
                            if (config.isTrackActivities()) {
                                activityLog.add("""${dateFormat.format(Date())}: ${activity.javaClass.simpleName} created
""")
                            }
                        }

                        override fun onActivityStarted(activity: Activity) {
                            currentlyStartedActivities++
                            isInBackground = currentlyStartedActivities == 0
                            //Do nothing
                        }

                        override fun onActivityResumed(activity: Activity) {
                            if (config.isTrackActivities()) {
                                activityLog.add("""${dateFormat.format(Date())}: ${activity.javaClass.simpleName} resumed
""")
                            }
                        }

                        override fun onActivityPaused(activity: Activity) {
                            if (config.isTrackActivities()) {
                                activityLog.add("""${dateFormat.format(Date())}: ${activity.javaClass.simpleName} paused
""")
                            }
                        }

                        override fun onActivityStopped(activity: Activity) {
                            currentlyStartedActivities--
                            isInBackground = currentlyStartedActivities == 0
                        }

                        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                        }

                        override fun onActivityDestroyed(activity: Activity) {
                            if (config.isTrackActivities()) {
                                activityLog.add("""${dateFormat.format(Date())}: ${activity.javaClass.simpleName} destroyed
""")
                            }
                        }
                    })
                }
                "CrashTool has been installed.".logv()
            }
        } catch (t: Throwable) {
            "An unknown error occurred while installing CrashTool".loge()
            t.toString().loge()
        }
    }


    @SuppressWarnings("unused")
    fun getStackTraceFromIntent(intent: Intent): String? {
        return intent.getStringExtra(EXTRA_STACK_TRACE)
    }

    /**
     * Given an Intent, returns the config extra from it.
     *
     * @param intent The Intent. Must not be null.
     * @return The config, or null if not provided.
     */
    fun getConfigFromIntent(intent: Intent): CrashProfile? {
        val config = intent.getSerializableExtra(EXTRA_CONFIG) as CrashProfile
        if (config.isLogErrorOnRestart()) {
            val stackTrace = getStackTraceFromIntent(intent)
            if (stackTrace != null) {
                Log.e(TAG, """
     The previous app process crashed. This is the stack trace of the crash:
     ${getStackTraceFromIntent(intent)}
     """.trimIndent())
            }
        }
        return config
    }

    fun getActivityLogFromIntent(intent: Intent): String? {
        return intent.getStringExtra(EXTRA_ACTIVITY_LOG)
    }

    fun getAllErrorDetailsFromIntent(context: Context, intent: Intent): String {
        val currentDate = Date()
        val dateFormat: DateFormat = SimpleDateFormat("yyyy年MM月dd日 HH点mm分ss秒", Locale.CHINA)

        val versionName = getVersionName(context)
        val appName = context.getAppName()
        val packageName = context.packageName
        val errorDetails = StringBuilder()
        errorDetails.append("Build App Name : ").append(appName).append(" \n")
        errorDetails.append("Build version : ").append(versionName).append(" \n")
        errorDetails.append("Build Package Name : ").append(packageName).append(" \n")
        errorDetails.append("Current date : ").append(dateFormat.format(currentDate)).append(" \n")
        errorDetails.append("Device : ").append(deviceModelName).append(" \n")
        errorDetails.append("OS version : Android ").append(Build.VERSION.RELEASE)
            .append(" (SDK ").append(Build.VERSION.SDK_INT).append(") \n \n")
        errorDetails.append("Stack trace :  \n")
        errorDetails.append(getStackTraceFromIntent(intent))
        val activityLog = getActivityLogFromIntent(intent)
        if (activityLog != null) {
            errorDetails.append("\nUser actions : \n")
            errorDetails.append(activityLog)
        }
        return errorDetails.toString()
    }

    fun restartApplicationWithIntent(activity: Activity, intent: Intent, config: CrashProfile) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        if (intent.component != null) {
            intent.action = Intent.ACTION_MAIN
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
        }
        if (config.getEventListener() != null) {
            config.getEventListener()!!.onRestartAppFromErrorActivity()
        }
        activity.finish()
        activity.startActivity(intent)
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        killCurrentProcess()
    }

    fun restartApplication(activity: Activity, config: CrashProfile) {
        val intent = Intent(activity, config.getRestartActivityClass())
        restartApplicationWithIntent(activity, intent, config)
    }


    fun closeApplication(activity: Activity, config: CrashProfile) {
        if (config.getEventListener() != null) {
            config.getEventListener()!!.onCloseAppFromErrorActivity()
        }
        activity.finish()
        killCurrentProcess()
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun getConfig(): CrashProfile {
        return config
    }


    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun setConfig(config: CrashProfile) {
        CrashTool.config = config
    }


    private fun isStackTraceLikelyConflictive(throwable: Throwable, activityClass: Class<out Activity?>): Boolean {
        var tempThrow = throwable
        var process: String?
        try {
            val br = BufferedReader(FileReader("/proc/self/cmdline"))
            process = br.readLine().trim { it <= ' ' }
            br.close()
        } catch (e: IOException) {
            process = null
        }
        if (process != null && process.endsWith(":error_activity")) {
            return true
        }
        do {
            val stackTrace = tempThrow.stackTrace
            for (element in stackTrace) {
                if (element.className == "android.app.ActivityThread" && element.methodName == "handleBindApplication") {
                    return true
                }
            }
        } while (tempThrow.cause.also { tempThrow = it!! } != null)
        return false
    }

    private fun getBuildDateAsString(context: Context, dateFormat: DateFormat): String? {
        var buildDate: Long
        try {
            val ai = context.packageManager.getApplicationInfo(context.packageName, 0)
            val zf = ZipFile(ai.sourceDir)
            val ze = zf.getEntry("classes.dex")
            buildDate = ze.time
            zf.close()
        } catch (e: Exception) {
            buildDate = 0
        }
        return if (buildDate > 312764400000L) {
            dateFormat.format(Date(buildDate))
        } else {
            null
        }
    }

    private fun getVersionName(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: Exception) {
            "Unknown"
        }
    }

    private val deviceModelName: String
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else {
                capitalize(manufacturer) + " " + model
            }
        }

    private fun capitalize(s: String?): String {
        if (s == null || s.isEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }


    private fun guessRestartActivityClass(context: Context): Class<out Activity>? {
        var resolvedActivityClass = getRestartActivityClassWithIntentFilter(context)
        if (resolvedActivityClass == null) {
            resolvedActivityClass = getLauncherActivity(context)
        }
        return resolvedActivityClass
    }


    private fun getRestartActivityClassWithIntentFilter(context: Context): Class<out Activity>? {
        val searchedIntent = Intent().setAction(INTENT_ACTION_RESTART_ACTIVITY).setPackage(context.packageName)
        val resolveInfos = context.packageManager.queryIntentActivities(searchedIntent,
            PackageManager.GET_RESOLVED_FILTER)
        if (resolveInfos.size > 0) {
            val resolveInfo = resolveInfos[0]
            try {
                return Class.forName(resolveInfo.activityInfo.name) as Class<out Activity>
            } catch (e: ClassNotFoundException) {
                Log.e(TAG, "Failed when resolving the restart activity class via intent filter, stack trace follows!", e)
            }
        }
        return null
    }


    private fun getLauncherActivity(context: Context): Class<out Activity>? {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        if (intent != null && intent.component != null) {
            try {
                return Class.forName(intent.component!!.className) as Class<out Activity?>
            } catch (e: ClassNotFoundException) {
                Log.e(TAG, "Failed when resolving the restart activity class via getLaunchIntentForPackage, stack trace follows!", e)
            }
        }
        return null
    }


    private fun guessErrorActivityClass(context: Context): Class<out Activity?> {
        return getErrorActivityClassWithIntentFilter(context)!!
    }

    private fun getErrorActivityClassWithIntentFilter(context: Context): Class<out Activity?>? {
        val searchedIntent = Intent().setAction(INTENT_ACTION_ERROR_ACTIVITY).setPackage(context.packageName)
        val resolveInfos = context.packageManager.queryIntentActivities(searchedIntent,
            PackageManager.GET_RESOLVED_FILTER)
        if (resolveInfos.size > 0) {
            val resolveInfo = resolveInfos[0]
            try {
                return Class.forName(resolveInfo.activityInfo.name) as Class<out Activity>
            } catch (e: ClassNotFoundException) {
                Log.e(TAG, "Failed when resolving the error activity class via intent filter, stack trace follows!", e)
            }
        }
        return null
    }

    private fun killCurrentProcess() {
        Process.killProcess(Process.myPid())
        exitProcess(10)
    }

    @SuppressLint("ApplySharedPref")
    private fun setLastCrashTimestamp(context: Context, timestamp: Long) {
        context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putLong(SHARED_PREFERENCES_FIELD_TIMESTAMP, timestamp).commit()
    }

    private fun getLastCrashTimestamp(context: Context): Long {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).getLong(SHARED_PREFERENCES_FIELD_TIMESTAMP, -1)
    }

    private fun hasCrashedInTheLastSeconds(context: Context): Boolean {
        val lastTimestamp = getLastCrashTimestamp(context)
        val currentTimestamp = Date().time
        return lastTimestamp <= currentTimestamp && currentTimestamp - lastTimestamp < config.getMinTimeBetweenCrashesMs()
    }

    interface EventListener : Serializable {
        fun onLaunchErrorActivity()
        fun onRestartAppFromErrorActivity()
        fun onCloseAppFromErrorActivity()
    }
}