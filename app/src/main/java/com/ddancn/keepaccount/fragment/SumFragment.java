package com.ddancn.keepaccount.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.constant.TypeEnum;
import com.ddancn.keepaccount.dao.RecordDao;
import com.ddancn.keepaccount.util.ChartHelper;
import com.ddancn.lib.base.BaseFragment;
import com.ddancn.lib.view.dialog.DatePickerDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author ddan.zhuang
 */
public class SumFragment extends BaseFragment {

    private Button btnMonth;
    private Button btnYear;
    private TextView tvMonth;
    private ImageView iconDate;
    private TextView tvIncome;
    private TextView tvOutcome;
    private TextView tvSum;
    private PieChart pieChartIn;
    private PieChart pieChartOut;
    private LineChart lineChartOut;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    private String showMonth = sdf.format(new Date());
    private boolean isMonth = true;

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
        btnMonth = root.findViewById(R.id.btn_month);
        btnYear = root.findViewById(R.id.btn_year);
        tvMonth = root.findViewById(R.id.tv_month);
        iconDate = root.findViewById(R.id.icon_date);
        tvIncome = root.findViewById(R.id.tv_income);
        tvOutcome = root.findViewById(R.id.tv_outcome);
        tvSum = root.findViewById(R.id.tv_sum);
        pieChartIn = root.findViewById(R.id.pie_chart_in);
        pieChartOut = root.findViewById(R.id.pie_chart_out);
        lineChartOut = root.findViewById(R.id.line_chart_out);

        ChartHelper.preparePie(pieChartIn);
        ChartHelper.preparePie(pieChartOut);
        ChartHelper.prepareLine(lineChartOut);
    }

    @Override
    protected void bindListener() {
        btnMonth.setOnClickListener(v -> {
            isMonth = true;
            toast("切换到月视图");
        });
        btnYear.setOnClickListener(v -> {
            isMonth = false;
            toast("切换到年视图");
        });
        iconDate.setOnClickListener(v -> DatePickerDialog.getPickerFromToday(getContext(), (datePicker, year, monthOfYear) -> {
            showMonth = getString(R.string.date_yyyy_mm, year, monthOfYear + 1);
            getData();
        }).hideDay().show());
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        tvMonth.setText(showMonth);
        initSumData();
        pieChartIn.setData(ChartHelper.getPieData(TypeEnum.IN.value(), getString(R.string.app_in), showMonth));
        pieChartOut.setData(ChartHelper.getPieData(TypeEnum.OUT.value(), getString(R.string.app_out), showMonth));
        lineChartOut.setData(ChartHelper.getLineData(showMonth));
        pieChartIn.invalidate();
        pieChartOut.invalidate();
        lineChartOut.invalidate();
    }

    /**
     * 初始化饼图的三个数据：收入、支出、总计
     */
    private void initSumData() {
        List<Double> sumData = RecordDao.calMonthSum(showMonth);
        tvIncome.setText(getString(R.string.sum_money_digit, sumData.get(0)));
        tvOutcome.setText(getString(R.string.sum_money_digit, sumData.get(1)));
        tvSum.setText(getString(R.string.sum_money_digit, sumData.get(2)));
    }

}
