package com.ddancn.keepaccount.util;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.constant.TypeEnum;
import com.ddancn.keepaccount.dao.RecordDao;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ddan.zhuang
 * @date 2019/10/17
 * 配合SumFragment使用
 */
public class ChartHelper {
    private ChartHelper() {
    }

    private static final int[] COLORS = {R.color.colorPieChart0, R.color.colorPieChart1, R.color.colorPieChart2,
            R.color.colorPieChart3, R.color.colorPieChart4, R.color.colorPieChart5,
            R.color.colorPieChart6, R.color.colorPieChart7, R.color.colorPieChart8, R.color.colorPieChart9};

    /**
     * 对饼图进行一些设置
     *
     * @param pieChart 饼图
     */
    public static void preparePie(PieChart pieChart) {
        // 设置无Description
        pieChart.setDescription(null);
        // 设置起始角度90（底部）
        pieChart.setRotationAngle(90);
        // 设置动画
        pieChart.animateXY(500, 500);
        // 设置占位文字
        pieChart.setNoDataText(Utils.getApp().getString(R.string.sum_no_data));

        // 设置图例方向和位置
        Legend legend = pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
    }

    /**
     * 获取饼图的数据
     *
     * @param type  类型
     * @param label 名称
     * @param showMonth 月份
     * @return 构造的数据
     */
    public static PieData getPieData(int type, String label, String showMonth) {
        Map<String, Double> typeSum = RecordDao.calTypeSum(type, showMonth);
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> perType : typeSum.entrySet()) {
            entries.add(new PieEntry(perType.getValue().floatValue(), perType.getKey()));
        }

        PieDataSet set = new PieDataSet(entries, label);
        set.setColors(COLORS, Utils.getApp());
        set.setValueTextSize(14f);
        set.setValueTextColor(Utils.getApp().getResources().getColor(R.color.colorText));
        return new PieData(set);
    }

    /**
     * 对折线图进行一些设置
     *
     * @param lineChart 折线图
     */
    public static void prepareLine(LineChart lineChart) {
        // 设置Description
        Description description = new Description();
        description.setText(StringUtils.getString(R.string.sum_out_trend));
        description.setTextSize(12);
        lineChart.setDescription(description);
        // 设置占位文字
        lineChart.setNoDataText(Utils.getApp().getString(R.string.sum_no_data));

        // 设置X轴，位置底部，无网格线，文字大小，最大最小值，步长，标签格式
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12);
        xAxis.setAxisMaximum(31);
        xAxis.setAxisMinimum(1);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter((value, axis) -> StringUtils.getString(R.string.sum_date_dd, (int) value));
        // 设置Y轴，标签格式
        YAxis lAxis = lineChart.getAxisLeft();
        lAxis.setValueFormatter((value, axis) -> StringUtils.getString(R.string.sum_money, value));
        lineChart.getAxisRight().setEnabled(false);

        //设置无图例
        lineChart.getLegend().setEnabled(false);
    }

    /**
     * 折线图的数据
     */
    public static LineData getLineData(String showMonth) {
        Map<String, Double> dailySum = RecordDao.calDailySum(TypeEnum.OUT.value(), showMonth);
        List<Entry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> perDay : dailySum.entrySet()) {
            entries.add(new Entry(Float.valueOf(perDay.getKey().split("-")[2]), perDay.getValue().floatValue()));
        }

        LineDataSet dataSet = new LineDataSet(entries,"");
        dataSet.setColors(COLORS, Utils.getApp());
        dataSet.setValueTextColor(Utils.getApp().getResources().getColor(R.color.colorText));
        dataSet.setCircleColors(COLORS, Utils.getApp());
        dataSet.setValueTextSize(10);

        return new LineData(dataSet);
    }
}
