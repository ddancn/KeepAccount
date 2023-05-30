package com.ddancn.lib.view.dialog;

import android.content.Context
import android.content.res.Resources
import android.view.View
import com.ddancn.lib.databinding.DialogDatePickerBinding
import java.util.*

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
class DatePickerDialog(context: Context,
                       private val onDateSetListener: android.app.DatePickerDialog.OnDateSetListener?,
                       private val year: Int,
                       private val monthOfYear: Int,
                       private val dayOfMonth: Int,
                       private val hideDay: Boolean,
                       private val hideMonth: Boolean)
    : BaseDialog<DialogDatePickerBinding>(context) {

    override fun initView() {
        vb.datePicker.init(year, monthOfYear, dayOfMonth, null)
        vb.btnConfirm.setOnClickListener {
            onDateSetListener?.onDateSet(vb.datePicker,
                    vb.datePicker.year,
                    vb.datePicker.month,
                    vb.datePicker.dayOfMonth)
            cancel()
        }
        vb.btnCancel.setOnClickListener { cancel() }
        if (hideDay) {
            hideDay()
        }
        if (hideMonth) {
            hideMonth()
        }
    }

    private fun hideDay() {
        val daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android")
        if (daySpinnerId != 0) {
            val daySpinner = vb.datePicker.findViewById<View>(daySpinnerId)
            daySpinner.visibility = View.GONE
        }
    }

    private fun hideMonth() {
        val daySpinnerId = Resources.getSystem().getIdentifier("month", "id", "android")
        if (daySpinnerId != 0) {
            val daySpinner = vb.datePicker.findViewById<View>(daySpinnerId)
            daySpinner.visibility = View.GONE
        }
    }

    companion object {
        /**
         * 简易获取日期选择对话框，日期从当天开始
         *
         * @param context   context
         * @param listener  listener
         * @param hideDay   是否隐藏日
         * @param hideMonth 是否隐藏月
         * @return picker
         */
        fun getPickerFromToday(context: Context, listener: android.app.DatePickerDialog.OnDateSetListener, hideDay: Boolean, hideMonth: Boolean): DatePickerDialog {
            val calendar = Calendar.getInstance()
            return DatePickerDialog(context, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hideDay, hideMonth)
        }

        fun getYearPickerFromToday(context: Context, listener: android.app.DatePickerDialog.OnDateSetListener): DatePickerDialog {
            return getPickerFromToday(context, listener, hideDay = true, hideMonth = true)
        }

        fun getYMPickerFromToday(context: Context, listener: android.app.DatePickerDialog.OnDateSetListener): DatePickerDialog {
            return getPickerFromToday(context, listener, hideDay = true, hideMonth = false)
        }

        fun getYMDPickerFromToday(context: Context, listener: android.app.DatePickerDialog.OnDateSetListener): DatePickerDialog {
            return getPickerFromToday(context, listener, hideDay = false, hideMonth = false)
        }
    }
}


