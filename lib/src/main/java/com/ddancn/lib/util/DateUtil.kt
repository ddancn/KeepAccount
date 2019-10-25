package com.ddancn.lib.util

import com.blankj.utilcode.util.Utils
import com.ddancn.lib.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author ddan.zhuang
 * @date 2019/10/18
 */

val YEAR_MONTH_SDF = SimpleDateFormat("yyyy-MM", Locale.CHINA)
val YEAR_SDF = SimpleDateFormat("yyyy", Locale.CHINA)
val TWO_DIGITS_DF = DecimalFormat("00")

fun date2YM(date: Date): String = YEAR_MONTH_SDF.format(date)

fun getThisMonth(): String = date2YM(Date())

fun date2Y(date: Date): String = YEAR_SDF.format(date)

fun getThisYear(): String = date2Y(Date())

fun getFormatYM(year: Int, month: Int): String = Utils.getApp().getString(
        R.string.date_yyyy_mm, year.toString(), TWO_DIGITS_DF.format(month))

fun getFormatYMD(year: Int, month: Int, day: Int): String = Utils.getApp().getString(
        R.string.date_yyyy_mm_dd, year.toString(), TWO_DIGITS_DF.format(month), TWO_DIGITS_DF.format(day))