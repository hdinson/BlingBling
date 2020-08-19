package com.dinson.blingbase.utils.validations

import android.view.View
import android.widget.*
import com.dinson.blingbase.kotlin.onTextChanged
import com.dinson.blingbase.utils.validations.executor.ValidationExecutor
import java.util.HashMap

class RxValidator(private val builder: Builder) {

    private fun execute(): RxValidator {
        builder.validationModels.forEach { autoValidate(it.getValidateView()) }
        builder.validateView.forEach { autoValidate(it.key) }
        doValidator()
        return this
    }

    private fun autoValidate(target: View) {
        when (target) {
            is TextView -> target.onTextChanged { _, _, _, _ ->
                doValidator()
            }
            is CheckBox -> target.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, _ ->
                doValidator()
            })
        }
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
            val result = it.value()
            if (!result) {
                return false
            }
        }
        builder.getApplyBtn().isEnabled = true
        return true
    }


    class Builder(private val applyBtn: View) {

        val validationModels = ArrayList<ValidationExecutor>()
        val validateView = HashMap<View, () -> Boolean>()

        fun add(valida: ValidationExecutor): Builder {
            validationModels.add(valida)
            return this
        }

        fun add(view: View, func: () -> Boolean): Builder {
            validateView[view] = func
            return this
        }


        fun build(): RxValidator {
            return RxValidator(this).execute()
        }

        fun getApplyBtn() = applyBtn
    }
}