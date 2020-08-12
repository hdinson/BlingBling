package com.dinson.blingbase.utils.validations.executor

import android.view.View

interface ValidationExecutor {
    fun doValidate(): Boolean
    fun getValidateView(): View
}