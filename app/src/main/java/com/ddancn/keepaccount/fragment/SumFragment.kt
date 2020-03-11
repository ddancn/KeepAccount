package com.ddancn.keepaccount.fragment

import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.constant.TypeEnum
import com.ddancn.keepaccount.dao.RecordDao
import com.ddancn.keepaccount.util.ChartHelper
import com.ddancn.lib.base.BaseFragment
import com.ddancn.lib.util.getFormatYM
import com.ddancn.lib.util.getThisMonth
import com.ddancn.lib.util.getThisYear
import com.ddancn.lib.view.dialog.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_sum.*

/**
 * @author ddan.zhuang
 */
class SumFragment : BaseFragment() {

    private var showMonth = getThisMonth()
    private var showYear = getThisYear()
    // 月视图或年视图
    private var isMonth = true

    override fun bindLayout(): Int = R.layout.fragment_sum

    override fun setHeaderTitle(): String = getString(R.string.sum_title)

    override fun initParam() {
        showMonth = getThisMonth()
        showYear = getThisYear()
    }

    override fun initView() {
        ChartHelper.preparePie(pie_chart_in)
        ChartHelper.preparePie(pie_chart_out)
        ChartHelper.prepareBar(bar_chart_out)
    }

    override fun bindListener() {
        rb_month.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isMonth = true
                toast(R.string.sum_to_month)
                getData(showMonth)
            }
        }
        rb_year.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isMonth = false
                toast(R.string.sum_to_year)
                getData(showYear)
            }
        }
        icon_date.setOnClickListener {
            DatePickerDialog.getPickerFromToday(icon_date.context,
                    android.app.DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                        if (isMonth) {
                            showMonth = getFormatYM(year, month + 1)
                            getData(showMonth)
                        } else {
                            showYear = year.toString()
                            getData(showYear)
                        }
                    }, hideDay = true, hideMonth = !isMonth).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isMonth) {
            getData(showMonth)
        } else {
            getData(showYear)
        }
    }

    private fun getData(date: String) {
        tv_date.text = date
        getSumData(date)

        pie_chart_in.data = ChartHelper.getPieData(TypeEnum.IN.value(), getString(R.string.app_in), date)
        pie_chart_out.data = ChartHelper.getPieData(TypeEnum.OUT.value(), getString(R.string.app_out), date)
        bar_chart_out.clear()
        ChartHelper.resetBar(bar_chart_out, isMonth)
        bar_chart_out.data = ChartHelper.getBarData(TypeEnum.OUT.value(), date)

        pie_chart_in.invalidate()
        pie_chart_out.invalidate()
        bar_chart_out.invalidate()
    }

    /**
     * 初始化三个统计数据：收入、支出、总计
     */
    private fun getSumData(date: String) {
        val sumData = RecordDao.calMonthOrYearSum(date)
        tv_income.text = getString(R.string.sum_money_digit, sumData[0])
        tv_outcome.text = getString(R.string.sum_money_digit, sumData[1])
        tv_sum.text = getString(R.string.sum_money_digit, sumData[2])
    }

}
