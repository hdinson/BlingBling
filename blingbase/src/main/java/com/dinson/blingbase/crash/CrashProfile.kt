package com.dinson.blingbase.crash

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import java.io.Serializable
import java.lang.reflect.Modifier

class CrashProfile : Serializable {
    private var backgroundMode = BACKGROUND_MODE_SHOW_CUSTOM
    private var enabled = true
    private var showErrorDetails = true
    private var showRestartButton = true
    private var logErrorOnRestart = true
    private var trackActivities = false
    private var minTimeBetweenCrashesMs = 3000
    internal var errorDrawable: Int? = null
    private var errorActivityClass: Class<out Activity>? = null
    internal var restartActivityClass: Class<out Activity>? = null
    private var mEventListener: CrashTool.EventListener? = null

    @BackgroundMode
    fun getBackgroundMode(): Int {
        return backgroundMode
    }

    fun setBackgroundMode(@BackgroundMode backgroundMode: Int) {
        this.backgroundMode = backgroundMode
    }

    fun isEnabled(): Boolean {
        return enabled
    }

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    fun isShowErrorDetails(): Boolean {
        return showErrorDetails
    }

    fun setShowErrorDetails(showErrorDetails: Boolean) {
        this.showErrorDetails = showErrorDetails
    }

    fun isShowRestartButton(): Boolean {
        return showRestartButton
    }

    fun setShowRestartButton(showRestartButton: Boolean) {
        this.showRestartButton = showRestartButton
    }

    fun isLogErrorOnRestart(): Boolean {
        return logErrorOnRestart
    }

    fun setLogErrorOnRestart(logErrorOnRestart: Boolean) {
        this.logErrorOnRestart = logErrorOnRestart
    }

    fun isTrackActivities(): Boolean {
        return trackActivities
    }

    fun setTrackActivities(trackActivities: Boolean) {
        this.trackActivities = trackActivities
    }

    fun getMinTimeBetweenCrashesMs(): Int {
        return minTimeBetweenCrashesMs
    }

    fun setMinTimeBetweenCrashesMs(minTimeBetweenCrashesMs: Int) {
        this.minTimeBetweenCrashesMs = minTimeBetweenCrashesMs
    }

    @DrawableRes
    fun getErrorDrawable(): Int? {
        return errorDrawable
    }

    fun setErrorDrawable(@DrawableRes errorDrawable: Int?) {
        this.errorDrawable = errorDrawable
    }

    fun getErrorActivityClass(): Class<out Activity>? {
        return errorActivityClass
    }

    fun setErrorActivityClass(errorActivityClass: Class<out Activity>?) {
        this.errorActivityClass = errorActivityClass
    }

    fun getRestartActivityClass(): Class<out Activity>? {
        return restartActivityClass
    }

    fun setRestartActivityClass(restartActivityClass: Class<out Activity>?) {
        this.restartActivityClass = restartActivityClass
    }

    fun getEventListener(): CrashTool.EventListener? {
        return mEventListener
    }

    fun setEventListener(eventListener: CrashTool.EventListener?) {
        this.mEventListener = eventListener
    }

    @IntDef(BACKGROUND_MODE_CRASH, BACKGROUND_MODE_SHOW_CUSTOM, BACKGROUND_MODE_SILENT)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    private annotation class BackgroundMode { //I hate empty blocks
    }

    class Builder {
        private var mProfile: CrashProfile? = null


        fun backgroundMode(@BackgroundMode backgroundMode: Int): Builder {
            mProfile!!.backgroundMode = backgroundMode
            return this
        }


        fun enabled(enabled: Boolean): Builder {
            mProfile!!.enabled = enabled
            return this
        }


        fun showErrorDetails(showErrorDetails: Boolean): Builder {
            mProfile!!.showErrorDetails = showErrorDetails
            return this
        }


        fun showRestartButton(showRestartButton: Boolean): Builder {
            mProfile!!.showRestartButton = showRestartButton
            return this
        }


        fun logErrorOnRestart(logErrorOnRestart: Boolean): Builder {
            mProfile!!.logErrorOnRestart = logErrorOnRestart
            return this
        }


        fun trackActivities(trackActivities: Boolean): Builder {
            mProfile!!.trackActivities = trackActivities
            return this
        }


        fun minTimeBetweenCrashesMs(minTimeBetweenCrashesMs: Int): Builder {
            mProfile!!.minTimeBetweenCrashesMs = minTimeBetweenCrashesMs
            return this
        }


        fun errorDrawable(@DrawableRes errorDrawable: Int?): Builder {
            mProfile!!.errorDrawable = errorDrawable
            return this
        }


        fun errorActivity(errorActivityClass: Class<out Activity>?): Builder {
            mProfile!!.errorActivityClass = errorActivityClass
            return this
        }


        fun restartActivity(restartActivityClass: Class<out Activity>?): Builder {
            mProfile!!.restartActivityClass = restartActivityClass
            return this
        }


        fun eventListener(eventListener: CrashTool.EventListener?): Builder {
            require(!(eventListener != null && eventListener.javaClass.enclosingClass != null && !Modifier.isStatic(eventListener.javaClass.modifiers))) { "The event listener cannot be an inner or anonymous class, because it will need to be serialized. Change it to a class of its own, or make it a static inner class." }
            mProfile!!.mEventListener = eventListener
            return this
        }

        fun get(): CrashProfile {
            return mProfile!!
        }

        fun apply() {
            CrashTool.setConfig(mProfile!!)
        }

        companion object {
            fun create(): Builder {
                val builder = Builder()
                val currentConfig = CrashTool.getConfig()
                val config = CrashProfile()
                config.backgroundMode = currentConfig.backgroundMode
                config.enabled = currentConfig.enabled
                config.showErrorDetails = currentConfig.showErrorDetails
                config.showRestartButton = currentConfig.showRestartButton
                config.logErrorOnRestart = currentConfig.logErrorOnRestart
                config.trackActivities = currentConfig.trackActivities
                config.minTimeBetweenCrashesMs = currentConfig.minTimeBetweenCrashesMs
                config.errorDrawable = currentConfig.errorDrawable
                config.errorActivityClass = currentConfig.errorActivityClass
                config.restartActivityClass = currentConfig.restartActivityClass
                config.mEventListener = currentConfig.mEventListener
                builder.mProfile = config
                return builder
            }
        }
    }

    companion object {
        const val BACKGROUND_MODE_SILENT = 0
        const val BACKGROUND_MODE_SHOW_CUSTOM = 1
        const val BACKGROUND_MODE_CRASH = 2
    }
}