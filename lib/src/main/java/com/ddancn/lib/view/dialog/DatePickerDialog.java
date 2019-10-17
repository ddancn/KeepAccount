package com.ddancn.lib.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.blankj.utilcode.util.LogUtils;
import com.ddancn.lib.R;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
public class DatePickerDialog extends AlertDialog implements OnClickListener, OnDateChangedListener {

    private final DatePicker datePicker;
    private final OnDateSetListener onDateSetListener;

    public interface OnDateSetListener {
        /**
         * 日期选择回调
         *
         * @param datePicker        日期选择器
         * @param year        选中年
         * @param monthOfYear 选中月
         */
        void onDateSet(DatePicker datePicker, int year, int monthOfYear);
    }

    public DatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth);
    }

    public DatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
                            int dayOfMonth) {
        super(context, theme);

        onDateSetListener = callBack;
        setButton(BUTTON_POSITIVE, getContext().getString(R.string.btn_confirm), this);
        setButton(BUTTON_NEGATIVE, getContext().getString(R.string.btn_cancel), this);
        setIcon(0);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_date_picker, null);
        setView(view);
        datePicker = view.findViewById(R.id.datePicker);
        datePicker.init(year, monthOfYear, dayOfMonth, this);

    }

    public DatePickerDialog hideDay() {
        /* 处理android5.0以上的特殊情况 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
            if (daySpinnerId != 0) {
                View daySpinner = datePicker.findViewById(daySpinnerId);
                if (daySpinner != null) {
                    daySpinner.setVisibility(View.GONE);
                }
            }
        } else {
            Field[] datePickerFields = datePicker.getClass().getDeclaredFields();
            for (Field datePickerField : datePickerFields) {
                if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                    datePickerField.setAccessible(true);
                    Object dayPicker = new Object();
                    try {
                        dayPicker = datePickerField.get(datePicker);
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        LogUtils.e(e);
                    }
                    ((View) dayPicker).setVisibility(View.GONE);
                }
            }
        }
        return this;
    }

    public DatePickerDialog hideMonth() {
        /* 处理android5.0以上的特殊情况 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int daySpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
            if (daySpinnerId != 0) {
                View daySpinner = datePicker.findViewById(daySpinnerId);
                if (daySpinner != null) {
                    daySpinner.setVisibility(View.GONE);
                }
            }
        } else {
            Field[] datePickerFields = datePicker.getClass().getDeclaredFields();
            for (Field datePickerField : datePickerFields) {
                if ("mMonthSpinner".equals(datePickerField.getName()) || ("mMonthPicker").equals(datePickerField.getName())) {
                    datePickerField.setAccessible(true);
                    Object monthPicker = new Object();
                    try {
                        monthPicker = datePickerField.get(datePicker);
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        LogUtils.e(e);
                    }
                    ((View) monthPicker).setVisibility(View.GONE);
                }
            }
        }
        return this;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == BUTTON_POSITIVE && onDateSetListener != null) {
            onDateSetListener.onDateSet(datePicker,
                    datePicker.getYear(),
                    datePicker.getMonth());
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        datePicker.init(year, month, day, this);
    }

    /**
     * 简易获取日期选择对话框，日期从当天开始
     * @param context context
     * @param listener listener
     * @return picker
     */
    public static DatePickerDialog getPickerFromToday(Context context, OnDateSetListener listener){
        Calendar calendar = Calendar.getInstance();
        return new DatePickerDialog(
                context,
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }
}
