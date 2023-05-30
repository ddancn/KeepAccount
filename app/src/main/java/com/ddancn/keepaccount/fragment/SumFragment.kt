package com.ddancn.keepaccount.fragment

import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.constant.TypeEnum
import com.ddancn.keepaccount.dao.RecordDao
import com.ddancn.keepaccount.databinding.FragmentSumBinding
import com.ddancn.keepaccount.util.ChartHelper
import com.ddancn.lib.base.BaseFragment
import com.ddancn.lib.util.getFormatYM
import com.ddancn.lib.util.getThisMonth
import com.ddancn.lib.util.getThisYear
import com.ddancn.lib.view.dialog.DatePickerDialog

/**
 * @author ddan.zhuang
 */
class SumFragment : BaseFragment<FragmentSumBinding>() {

    private var showMonth = getThisMonth()
    private var showYear = getThisYear()
    // 月视图或年视图
    private var isMonth = true

    override fun initParam() {
        showMonth = getThisMonth()
        showYear = getThisYear()
    }

    override fun initView() {
        headerView.setTitle(R.string.sum_title)
        ChartHelper.preparePie(vb.pieChartIn)
        ChartHelper.preparePie(vb.pieChartOut)
        ChartHelper.prepareBar(vb.barChartOut)
    }

    override fun bindListener() {
        vb.rbMonth.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isMonth = true
                toast(R.string.sum_to_month)
                getData(showMonth)
            }
        }
        vb.rbYear.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isMonth = false
                toast(R.string.sum_to_year)
                getData(showYear)
            }
        }
        vb.iconDate.setOnClickListener {
            DatePickerDialog.getPickerFromToday(vb.iconDate.context,
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
        vb.tvDate.text = date
        getSumData(date)

        vb.pieChartIn.data = ChartHelper.getPieData(TypeEnum.IN.value(), getString(R.string.app_in), date)
        vb.pieChartOut.data = ChartHelper.getPieData(TypeEnum.OUT.value(), getString(R.string.app_out), date)
        vb.barChartOut.clear()
        ChartHelper.resetBar(vb.barChartOut, isMonth)
        vb.barChartOut.data = ChartHelper.getBarData(TypeEnum.OUT.value(), date)

        vb.pieChartIn.invalidate()
        vb.pieChartOut.invalidate()
        vb.barChartOut.invalidate()
    }

    /**
     * 初始化三个统计数据：收入、支出、总计
     */
    private fun getSumData(date: String) {
        val sumData = RecordDao.calMonthOrYearSum(date)
        vb.tvIncome.text = getString(R.string.sum_money_digit, sumData[0])
        vb.tvOutcome.text = getString(R.string.sum_money_digit, sumData[1])
        vb.tvSum.text = getString(R.string.sum_money_digit, sumData[2])
    }

}
