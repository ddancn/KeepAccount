package com.ddancn.keepaccount.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.constant.TypeEnum;
import com.ddancn.keepaccount.dao.RecordDao;
import com.ddancn.keepaccount.util.ChartHelper;
import com.ddancn.lib.util.DateUtil;
import com.ddancn.lib.base.BaseFragment;
import com.ddancn.lib.view.dialog.DatePickerDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

/**
 * @author ddan.zhuang
 */
public class SumFragment extends BaseFragment {

    private RadioButton btnMonth;
    private RadioButton btnYear;
    private TextView tvDate;
    private ImageView iconDate;
    private TextView tvIncome;
    private TextView tvOutcome;
    private TextView tvSum;
    private PieChart pieChartIn;
    private PieChart pieChartOut;
    private LineChart lineChartOut;

    private String showMonth;
    private String showYear;
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
    protected void initParam() {
        showMonth = DateUtil.getThisMonth();
        showYear = DateUtil.getThisYear();
    }

    @Override
    protected void initView(View root) {
        btnMonth = root.findViewById(R.id.btn_month);
        btnYear = root.findViewById(R.id.btn_year);
        tvDate = root.findViewById(R.id.tv_date);
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
        btnMonth.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isMonth = true;
                toast(R.string.sum_to_month);
                getData(showMonth);
            }
        });
        btnYear.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isMonth = false;
                toast(R.string.sum_to_year);
                getData(showYear);
            }
        });
        iconDate.setOnClickListener(v -> DatePickerDialog.getPickerFromToday(getContext(), (datePicker, year, month, day) -> {
            if (isMonth) {
                showMonth = DateUtil.getFormatYM(year, month + 1);
                getData(showMonth);
            } else {
                showYear = String.valueOf(year);
                getData(showYear);
            }
        }, true, !isMonth).show());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isMonth) {
            getData(showMonth);
        } else {
            getData(showYear);
        }
    }

    private void getData(String date) {
        tvDate.setText(date);
        getSumData(date);
        pieChartIn.setData(ChartHelper.getPieData(TypeEnum.IN.value(), getString(R.string.app_in), date));
        pieChartOut.setData(ChartHelper.getPieData(TypeEnum.OUT.value(), getString(R.string.app_out), date));
        lineChartOut.setData(ChartHelper.getLineData(TypeEnum.OUT.value(), date));
        pieChartIn.invalidate();
        pieChartOut.invalidate();
        ChartHelper.resetLine(lineChartOut, isMonth);
        lineChartOut.invalidate();
    }

    /**
     * 初始化三个统计数据：收入、支出、总计
     */
    private void getSumData(String date) {
        List<Double> sumData = RecordDao.calMonthOrYearSum(date);
        tvIncome.setText(getString(R.string.sum_money_digit, sumData.get(0)));
        tvOutcome.setText(getString(R.string.sum_money_digit, sumData.get(1)));
        tvSum.setText(getString(R.string.sum_money_digit, sumData.get(2)));
    }

}
