package com.ddancn.keepaccount.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.print.PrinterId;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.activity.MainActivity;
import com.ddancn.keepaccount.dialog.DatePickerDialog;
import com.ddancn.keepaccount.entity.Record;
import com.ddancn.keepaccount.entity.Type;
import com.ddancn.keepaccount.util.DimenUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.joanzapata.iconify.widget.IconTextView;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SumFragment extends Fragment {

    IconTextView iconDate;
    TextView tvIncome;
    TextView tvOutcome;
    TextView tvSum;
    PieChart chartIn;
    PieChart chartOut;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    private String showMonth = sdf.format(new Date());

    public static int[] colors = {R.color.colorPieChart0, R.color.colorPieChart1, R.color.colorPieChart2,
            R.color.colorPieChart3, R.color.colorPieChart4, R.color.colorPieChart5,
            R.color.colorPieChart6, R.color.colorPieChart7, R.color.colorPieChart8, R.color.colorPieChart9 };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sum, container, false);
        iconDate = root.findViewById(R.id.icon_date);
        tvIncome = root.findViewById(R.id.tv_income);
        tvOutcome = root.findViewById(R.id.tv_outcome);
        tvSum = root.findViewById(R.id.tv_sum);
        chartIn = root.findViewById(R.id.chart_type_in);
        chartOut = root.findViewById(R.id.chart_type_out);

        iconDate.setOnClickListener(v -> showDatePickDialog());

        getData();

        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            getData();
//        }
    }

    private void getData() {
        initSumData();
        preparePie(chartIn);
        preparePie(chartOut);
        chartIn.setData(getPieDataFromDB(MainActivity.TYPE_IN, "收入"));
        chartOut.setData(getPieDataFromDB(MainActivity.TYPE_OUT, "支出"));
    }

    private void preparePie(PieChart pieChart) {
        pieChart.setDescription(null); // 描述
        pieChart.setRotationAngle(90); // 初始旋转角度
        pieChart.animateXY(500, 500); // 设置动画
        Legend mLegend = pieChart.getLegend(); // 设置比例图
        mLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); //在右边显示
    }

    /**
     * 初始化三个数据
     */
    @SuppressLint("SetTextI18n")
    protected void initSumData() {
        List<Record> incomeList = LitePal
                .where("date like ? and type is ?",
                        showMonth + "%", String.valueOf(MainActivity.TYPE_IN))
                .find(Record.class);
        List<Record> outcomeList = LitePal
                .where("date like ? and type is ?",
                        showMonth + "%", String.valueOf(MainActivity.TYPE_OUT))
                .find(Record.class);

        double income = 0, outcome = 0, sum = 0;
        for (Record record : incomeList)
            income += record.getMoney();
        for (Record record : outcomeList)
            outcome += record.getMoney();
        sum = income - outcome;

        DecimalFormat df = new DecimalFormat("0.00");
        tvIncome.setText("￥" + df.format(income));
        tvOutcome.setText("￥-" + df.format(outcome));
        tvSum.setText("￥" + df.format(sum));
    }

    /**
     * 饼图的数据
     * @param type 类型
     * @param label 名称
     * @return 构造的数据
     */
    private PieData getPieDataFromDB(int type, String label) {
        List<PieEntry> entries = new ArrayList<>();

        List<Type> typeList = LitePal.select("name").where("type = ?", type + "").find(Type.class);
        for (Type t : typeList) {
            List<Record> recordList = LitePal.select("money")
                    .where("date like ? and typeName is ?", showMonth + "%", t.getName())
                    .find(Record.class);
            double temp = 0;
            for (Record record : recordList)
                temp += record.getMoney();
            entries.add(new PieEntry((float) temp, t.getName()));
        }
        PieDataSet set = new PieDataSet(entries, label);
        set.setColors(colors, getContext());
        set.setValueTextSize(14f);
        set.setValueTextColor(getResources().getColor(R.color.colorText));
        return new PieData(set);
    }


    /**
     * 只有年月的日期选择框
     */
    private void showDatePickDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    showMonth = year + "-" + ((monthOfYear < 9) ? "0" : "") + (monthOfYear + 1);
                    getData();
                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

}
