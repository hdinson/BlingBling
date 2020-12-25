package com.dinson.blingbase.widget.catpcha

interface CaptchaListener {
    /**
     * Called when captcha access.
     *
     * @param time cost of access time
     * @return text to show,show default when return null
     */
    fun onAccess(time: Long): String

    /**
     * Called when captcha failed.
     *
     * @param failCount fail count
     * @return text to show,show default when return null
     */
    fun onFailed(failCount: Int): String

    /**
     * Called when captcha failed
     *
     * @return text to show,show default when return null
     */
    fun onMaxFailed(): String
}