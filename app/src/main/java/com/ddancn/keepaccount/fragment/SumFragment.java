package com.ddancn.keepaccount.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.constant.TypeEnum;
import com.ddancn.keepaccount.dao.RecordDao;
import com.ddancn.keepaccount.dao.TypeDao;
import com.ddancn.keepaccount.entity.Record;
import com.ddancn.keepaccount.entity.Type;
import com.ddancn.lib.base.BaseFragment;
import com.ddancn.lib.view.dialog.DatePickerDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author ddan.zhuang
 */
public class SumFragment extends BaseFragment {

    private TextView tvIncome;
    private TextView tvOutcome;
    private TextView tvSum;
    private PieChart chartIn;
    private PieChart chartOut;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    private String showMonth = sdf.format(new Date());

    private static final int[] COLORS = {R.color.colorPieChart0, R.color.colorPieChart1, R.color.colorPieChart2,
            R.color.colorPieChart3, R.color.colorPieChart4, R.color.colorPieChart5,
            R.color.colorPieChart6, R.color.colorPieChart7, R.color.colorPieChart8, R.color.colorPieChart9};

    @Override
    protected int bindLayout() {
        return R.layout.fragment_sum;
    }

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.sum_title);
    }

    @Override
    protected void initView(View root) {
        setRightImage(R.drawable.ic_calendar);
        tvIncome = root.findViewById(R.id.tv_income);
        tvOutcome = root.findViewById(R.id.tv_outcome);
        tvSum = root.findViewById(R.id.tv_sum);
        chartIn = root.findViewById(R.id.chart_type_in);
        chartOut = root.findViewById(R.id.chart_type_out);
    }

    @Override
    protected void bindListener() {
        setRightClickListener(v ->
                DatePickerDialog.getPickerFromToday(getContext(), (datePicker, year, monthOfYear) -> {
                    showMonth = getString(R.string.date_yyyy_mm, year, monthOfYear + 1);
                    getData();
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        initSumData();
        preparePie(chartIn);
        preparePie(chartOut);
        chartIn.setData(getPieDataFromDB(TypeEnum.IN.value(), getString(R.string.app_in)));
        chartOut.setData(getPieDataFromDB(TypeEnum.OUT.value(), getString(R.string.app_out)));
    }

    /**
     * 初始化三个数据
     */
    @SuppressLint("SetTextI18n")
    private void initSumData() {
        List<Record> incomeList = RecordDao.getRecordByTypeAndMonth(TypeEnum.IN.value(), showMonth);
        List<Record> outcomeList = RecordDao.getRecordByTypeAndMonth(TypeEnum.OUT.value(), showMonth);

        double income = 0;
        double outcome = 0;
        for (Record record : incomeList) {
            income += record.getMoney();
        }
        for (Record record : outcomeList) {
            outcome -= record.getMoney();
        }
        tvIncome.setText(getString(R.string.sum_money, income));
        tvOutcome.setText(getString(R.string.sum_money, outcome));
        tvSum.setText(getString(R.string.sum_money, income + outcome));
    }

    /**
     * 对饼进行一些设置
     *
     * @param pieChart 饼图
     */
    private void preparePie(PieChart pieChart) {
        pieChart.setDescription(null);
        pieChart.setRotationAngle(90);
        pieChart.animateXY(500, 500);

        Legend mLegend = pieChart.getLegend();
        mLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
    }

    /**
     * 饼图的数据
     *
     * @param type  类型
     * @param label 名称
     * @return 构造的数据
     */
    private PieData getPieDataFromDB(int type, String label) {
        List<PieEntry> entries = new ArrayList<>();

        List<Type> typeList = TypeDao.getTypesByType(type);
        for (Type t : typeList) {
            List<Record> recordList = RecordDao.getRecordByTypeAndMonth(t.getType(), t.getName(), showMonth);
            double temp = 0;
            for (Record record : recordList) {
                temp += record.getMoney();
            }
            entries.add(new PieEntry((float) temp, t.getName()));
        }
        PieDataSet set = new PieDataSet(entries, label);
        set.setColors(COLORS, getContext());
        set.setValueTextSize(14f);
        set.setValueTextColor(getResources().getColor(R.color.colorText));
        return new PieData(set);
    }

}
