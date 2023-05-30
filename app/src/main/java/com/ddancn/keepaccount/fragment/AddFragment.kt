package com.ddancn.keepaccount.fragment

import android.content.Context
import android.widget.ArrayAdapter
import androidx.annotation.NonNull
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.activity.SettingActivity
import com.ddancn.keepaccount.activity.UpdateActivity
import com.ddancn.keepaccount.constant.TypeEnum
import com.ddancn.keepaccount.dao.CategoryDao
import com.ddancn.keepaccount.dao.RecordDao
import com.ddancn.keepaccount.databinding.FragmentAddBinding
import com.ddancn.keepaccount.entity.Record
import com.ddancn.keepaccount.util.getFormatYMD
import com.ddancn.lib.base.BaseFragment
import java.util.*

/**
 * @author ddan.zhuang
 * 添加或修改记录页面
 */
class AddFragment : BaseFragment<FragmentAddBinding>() {

    private var type = TypeEnum.OUT.value()
    private var isUpdate = false
    private lateinit var recordToUpdate: Record

    override fun hasHeader(): Boolean = !isUpdate

    override fun onAttach(@NonNull context: Context) {
        super.onAttach(context)
        isUpdate = context is UpdateActivity
    }

    override fun onStart() {
        super.onStart()
        recordToUpdate = (activity as? UpdateActivity)?.recordToUpdate ?: Record()
    }

    override fun initView() {
        headerView.setTitle(R.string.app_name)
        headerView.setRightImage(R.drawable.ic_setting)
    }

    override fun bindListener() {
        headerView.setRightClickListener { start(SettingActivity::class.java) }
        // 获取radio group收支选项，联动改变类型spinner的内容
        vb.rgType.setOnCheckedChangeListener { group, checkedId ->
            type = if (checkedId == R.id.rb_type_out) TypeEnum.OUT.value() else TypeEnum.IN.value()
            setSpinnerItems()
        }
        // 按钮点击事件
        vb.btnAdd.setOnClickListener {
            if (!checkParamLegal()) {
                return@setOnClickListener
            }
            if (saveOrUpdateRecord()) {
                toast(if (isUpdate) R.string.update_succeed else R.string.add_succeed)
                if (isUpdate) {
                    activity?.finish()
                } else {
                    reset()
                }
            } else {
                toast(if (isUpdate) R.string.update_fail else R.string.add_fail)
            }
        }
    }

    /**
     * 判断输入是否合法
     *
     * @return 是否合法
     */
    private fun checkParamLegal(): Boolean {
        if (vb.etMoney.text.isNullOrBlank()) {
            toast(R.string.add_need_money)
            return false
        }
        if (vb.spinnerCategory.selectedItem == null
            || vb.spinnerCategory.selectedItem.toString().isEmpty()
        ) {
            toast(R.string.add_need_category)
            return false
        }
        return true
    }

    /**
     * 如果是修改页面，先填上原来的数据
     */
    private fun fillOldData() {
        vb.btnAdd.text = getString(R.string.update_btn)
        // 选择日期
        recordToUpdate.date.split("-").let {
            val year = it[0].toInt()
            val month = it[1].toInt()
            val dayOfMonth = it[2].toInt()
            vb.datePicker.updateDate(year, month - 1, dayOfMonth)
        }
        vb.etMoney.setText(recordToUpdate.money.toString())
        vb.etDetail.setText(recordToUpdate.detail)

        // 填充收支类型判断
        type = recordToUpdate.type
        val categories = CategoryDao.getCategoriesByType(type)
        vb.rgType.check(if (type == TypeEnum.IN.value()) R.id.rb_type_in else R.id.rb_type_out)
        setSpinnerItems()

        // 填充具体类型名称
        var selected = 0
        categories.filterIndexed { index, category ->
            val result = category.name == recordToUpdate.categoryName
            if (result) {
                selected = index
            }
            result
        }
        vb.spinnerCategory.setSelection(selected, true)
    }

    override fun onResume() {
        super.onResume()
        if (isUpdate) {
            fillOldData()
        } else {
            setSpinnerItems()
        }
    }

    /**
     * 添加或修改记录
     */
    private fun saveOrUpdateRecord(): Boolean {
        val y = vb.datePicker.year
        val m = vb.datePicker.month + 1
        val d = vb.datePicker.dayOfMonth
        val date = getFormatYMD(y, m, d)
        val record = Record(
            date = date,
            money = vb.etMoney.text.toString().toDouble(),
            detail = vb.etDetail.text.toString(),
            type = type,
            categoryName = vb.spinnerCategory.selectedItem.toString()
        )

        return if (isUpdate) {
            record.id = recordToUpdate.id
            RecordDao.updateRecord(record)
        } else {
            RecordDao.addRecord(record)
        }
    }

    /**
     * 根据收支的选择填充spinner的内容
     */
    private fun setSpinnerItems() {
        val categories = CategoryDao.getCategoriesByType(type)
        if (categories.isEmpty()) {
            toast(R.string.add_no_category)
        }
        val spinnerAdapter =
            ArrayAdapter(vb.btnAdd.context, R.layout.item_spinner, categories.map { it.name })
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        vb.spinnerCategory.adapter = spinnerAdapter
    }

    /**
     * 重置清空
     */
    private fun reset() {
        val calendar = Calendar.getInstance()
        vb.datePicker.updateDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE)
        )
        vb.etMoney.setText("")
        vb.etDetail.setText("")
        type = TypeEnum.OUT.value()
        vb.rgType.check(R.id.rb_type_out)
        setSpinnerItems()
    }

}
