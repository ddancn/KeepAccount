package com.ddancn.lib.view.dialog;

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.widget.DatePicker
import com.blankj.utilcode.util.LogUtils
import com.ddancn.lib.R
import kotlinx.android.synthetic.main.dialog_date_picker.*
import java.util.*

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
class DatePickerDialog(context: Context,
                       private val onDateSetListener: OnDateSetListener?,
                       private val year: Int,
                       private val monthOfYear: Int,
                       private val dayOfMonth: Int,
                       private val hideDay: Boolean,
                       private val hideMonth: Boolean)
    : BaseDialog(context, R.layout.dialog_date_picker) {

    override fun initView() {
        date_picker.init(year, monthOfYear, dayOfMonth, null)
        btn_confirm.setOnClickListener {
            onDateSetListener?.onDateSet(date_picker,
                    date_picker.year,
                    date_picker.month,
                    date_picker.dayOfMonth)
        }
        if (hideDay) {
            hideDay()
        }
        if (hideMonth) {
            hideMonth()
        }
    }

    interface OnDateSetListener {
        /**
         * 日期选择回调
         *
         * @param datePicker 日期选择器
         * @param year       选中年
         * @param monthOfYear      选中月
         * @param dayOfMonth        选中日
         */
        fun onDateSet(datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int)
    }

    fun hideDay() {
        /* 处理android5.0以上的特殊情况 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android")
            if (daySpinnerId != 0) {
                val daySpinner = date_picker.findViewById<View>(daySpinnerId)
                daySpinner.visibility = View.GONE
            }
        } else {
            val datePickerFields = date_picker.javaClass.declaredFields
            for (field in datePickerFields) {
                if ("mDaySpinner" == field.name || ("mDayPicker") == field.name) {
                    field.isAccessible = true
                    var dayPicker: Any? = null
                    try {
                        dayPicker = field.get(date_picker)
                    } catch (e: Exception) {
                        LogUtils.e(e)
                    }
                    (dayPicker as? View)?.visibility = View.GONE
                }
            }
        }
    }

    fun hideMonth() {
        /* 处理android5.0以上的特殊情况 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val daySpinnerId = Resources.getSystem().getIdentifier("month", "id", "android")
            if (daySpinnerId != 0) {
                val daySpinner = date_picker.findViewById<View>(daySpinnerId)
                daySpinner.visibility = View.GONE
            }
        } else {
            val datePickerFields = date_picker.javaClass.declaredFields
            for (field in datePickerFields) {
                if ("mMonthSpinner" == field.name || ("mMonthPicker") == field.name) {
                    field.isAccessible = true
                    var monthPicker: Any? = null
                    try {
                        monthPicker = field.get(date_picker)
                    } catch (e: Exception) {
                        LogUtils.e(e)
                    }
                    (monthPicker as? View)?.visibility = View.GONE
                }
            }
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
        fun getPickerFromToday(context: Context, listener: OnDateSetListener, hideDay: Boolean, hideMonth: Boolean): DatePickerDialog {
            val calendar = Calendar.getInstance()
            return DatePickerDialog(context, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hideDay, hideMonth)
        }

        fun getYearPickerFromToday(context: Context, listener: OnDateSetListener): DatePickerDialog {
            return getPickerFromToday(context, listener, true, true)
        }

        fun getYMPickerFromToday(context: Context, listener: OnDateSetListener): DatePickerDialog {
            return getPickerFromToday(context, listener, true, false)
        }

        fun getYMDPickerFromToday(context: Context, listener: OnDateSetListener): DatePickerDialog {
            return getPickerFromToday(context, listener, false, false)
        }
    }
}


