package com.ddancn.lib.util;

import com.blankj.utilcode.util.Utils;
import com.ddancn.lib.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author ddan.zhuang
 * @date 2019/10/18
 */
public class DateUtil {

    public static final SimpleDateFormat YEAR_MONTH_SDF = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    public static final SimpleDateFormat YEAR_SDF = new SimpleDateFormat("yyyy", Locale.CHINA);
    public static final DecimalFormat TWO_DIGITS_DF = new DecimalFormat("00");

    private DateUtil(){}

    public static String date2YM(Date date){
        return YEAR_MONTH_SDF.format(date);
    }

    public static String getThisMonth(){
        return date2YM(new Date());
    }

    public static String date2Y(Date date){
        return YEAR_SDF.format(date);
    }

    public static String getThisYear(){
        return date2Y(new Date());
    }

    public static String getFormatYM(int year, int month){
        return Utils.getApp().getString(R.string.date_yyyy_mm, String.valueOf(year), TWO_DIGITS_DF.format(month));
    }

    public static String getFormatYMD(int year, int month, int day){
        return Utils.getApp().getString(R.string.date_yyyy_mm_dd, String.valueOf(year), TWO_DIGITS_DF.format(month), TWO_DIGITS_DF.format(day));
    }
}
