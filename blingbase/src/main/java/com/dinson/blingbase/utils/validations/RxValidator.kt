package com.dinson.blingbase.utils.validations

import android.view.View
import android.widget.*
import com.dinson.blingbase.kotlin.onTextChanged
import com.dinson.blingbase.utils.validations.executor.ValidationExecutor

class RxValidator(private val builder: Builder) {

    private fun execute(): RxValidator {
        builder.validationModels.forEach {
            when (val validateView = it.getValidateView()) {
                is TextView -> validateView.onTextChanged { _, _, _, _ ->
                    doValidator()
                }
                is CheckBox -> validateView.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, _ ->
                    doValidator()
                })
            }
        }
        return this
    }


    fun doValidator(): Boolean {
        builder.getApplyBtn().isEnabled = false
        builder.validationModels.forEach {
            val result = it.doValidate()
            if (!result) {
                return false
            }
        }
        builder.validateView.forEach {
            val result = it()
            if (!result) {
                return false
            }
        }
        builder.getApplyBtn().isEnabled = true
        return true
    }


    class Builder(private val applyBtn: View) {

        val validationModels = ArrayList<ValidationExecutor>()
        val validateView = ArrayList<() -> Boolean>()

        fun add(valida: ValidationExecutor): Builder {
            validationModels.add(valida)
            return this
        }

        fun add(func: () -> Boolean): Builder {
            validateView.add(func)
            return this
        }


        fun build(): RxValidator {
            return RxValidator(this).execute()
        }

        fun getApplyBtn() = applyBtn
    }
}