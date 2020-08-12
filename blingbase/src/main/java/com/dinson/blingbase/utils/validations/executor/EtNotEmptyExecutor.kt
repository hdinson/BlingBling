package com.dinson.blingbase.utils.validations.executor

import android.widget.EditText

class EtNotEmptyExecutor(private val et: EditText) : ValidationExecutor {
    override fun doValidate(): Boolean {
        return et.text.toString().isNotEmpty()
    }

    override fun getValidateView() = et
}