package com.ddancn.keepaccount.util

import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.Utils
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.dao.RecordDao
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import java.util.*

/**
 * @author ddan.zhuang
 * @date 2019/10/17
 * 配合SumFragment使用
 */
private val COLORS = intArrayOf(R.color.colorPieChart0, R.color.colorPieChart1, R.color.colorPieChart2,
        R.color.colorPieChart3, R.color.colorPieChart4, R.color.colorPieChart5, R.color.colorPieChart6,
        R.color.colorPieChart7, R.color.colorPieChart8, R.color.colorPieChart9)

object ChartHelper {

    /**
     * 对饼图进行一些设置
     *
     * @param pieChart 饼图
     */
    fun preparePie(pieChart: PieChart) {
        // 设置无Description
        pieChart.description = null
        // 设置起始角度90（底部）
        pieChart.rotationAngle = 90f
        // 设置动画
        pieChart.animateXY(500, 500)
        // 设置占位文字
        pieChart.setNoDataText(Utils.getApp().getString(R.string.sum_no_data))

        // 设置图例方向和位置
        val legend = pieChart.legend
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
    }

    /**
     * 获取饼图的数据
     *
     * @param type  类型
     * @param label 名称
     * @param date  日期：月份/年份
     * @return 构造的数据
     */
    fun getPieData(type: Int, label: String, date: String): PieData {
        val typeSum = RecordDao.calTypeSum(type, date)
        val entries = ArrayList<PieEntry>()
        typeSum.forEach { entries.add(PieEntry(it.value.toFloat(), it.key)) }

        val set = PieDataSet(entries, label)
        set.setColors(COLORS, Utils.getApp())
        set.valueTextSize = 14f
        set.valueTextColor = Utils.getApp().resources.getColor(R.color.colorText)
        return PieData(set)
    }

    /**
     * 对折线图进行一些设置
     *
     * @param lineChart 折线图
     */
    fun prepareLine(lineChart: LineChart) {
        // 设置Description
        val description = Description()
        description.text = StringUtils.getString(R.string.sum_out_trend)
        description.textSize = 12f
        lineChart.description = description
        // 设置占位文字
        lineChart.setNoDataText(Utils.getApp().getString(R.string.sum_no_data))

        // 设置X轴，位置底部，无网格线，文字大小，最大最小值，步长，标签格式
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.axisMaximum = 31f
        xAxis.axisMinimum = 1f
        xAxis.granularity = 1f
        xAxis.setValueFormatter { value, axis -> StringUtils.getString(R.string.sum_date_dd, value.toInt()) }
        // 设置Y轴，标签格式
        val lAxis = lineChart.axisLeft
        lAxis.setValueFormatter { value, axis -> StringUtils.getString(R.string.sum_money, value) }
        lineChart.axisRight.isEnabled = false

        //设置无图例
        lineChart.legend.isEnabled = false
    }

    /**
     * 重新设置x轴的最大最小值和format
     *
     * @param lineChart 折线图
     * @param isMonth   月/年
     */
    fun resetLine(lineChart: LineChart, isMonth: Boolean) {
        val xAxis = lineChart.xAxis
        xAxis.axisMaximum = (if (isMonth) 31 else 12).toFloat()
        xAxis.setValueFormatter { value, axis -> StringUtils.getString(if (isMonth) R.string.sum_date_dd else R.string.sum_date_mm, value.toInt()) }
    }

    /**
     * 折线图的数据
     *
     * @param type 类型
     * @param date 日期：月份/年份
     */
    fun getLineData(type: Int, date: String): LineData {
        val sum = RecordDao.calDayOrMonthSum(type, date)
        val entries = ArrayList<Entry>()
        sum.forEach { entries.add(Entry(it.key.toFloat(), it.value.toFloat())) }

        val dataSet = LineDataSet(entries, "")
        dataSet.setColors(COLORS, Utils.getApp())
        dataSet.valueTextColor = Utils.getApp().resources.getColor(R.color.colorText)
        dataSet.setCircleColors(COLORS, Utils.getApp())
        dataSet.valueTextSize = 10f
        return LineData(dataSet)
    }
}
