package com.ddancn.keepaccount.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.NonNull
import com.blankj.utilcode.util.ToastUtils
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.activity.SettingActivity
import com.ddancn.keepaccount.activity.UpdateActivity
import com.ddancn.keepaccount.constant.TypeEnum
import com.ddancn.keepaccount.dao.RecordDao
import com.ddancn.keepaccount.dao.TypeDao
import com.ddancn.keepaccount.entity.Record
import com.ddancn.lib.base.BaseFragment
import com.ddancn.lib.util.getFormatYMD
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.*

/**
 * @author ddan.zhuang
 * 添加或修改记录
 */
class AddFragment : BaseFragment() {

    private var type = TypeEnum.OUT.value()
    private var isUpdate = false
    private lateinit var recordToUpdate: Record

    override fun bindLayout(): Int {
        return R.layout.fragment_add
    }

    override fun setHeaderTitle(): String {
        return getString(R.string.app_name)
    }

    override fun hasHeader(): Boolean {
        return !isUpdate
    }

    override fun onAttach(@NonNull context: Context) {
        super.onAttach(context)
        if (context is UpdateActivity) {
            isUpdate = true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recordToUpdate = (activity as? UpdateActivity)?.recordToUpdate ?: Record()
    }

    override fun initView() {
        setRightImage(R.drawable.ic_setting)
    }

    override fun bindListener() {
        setRightClickListener(View.OnClickListener { start(SettingActivity::class.java) })
        // 获取radio group收支选项，联动改变类型spinner的内容
        rg_type.setOnCheckedChangeListener { group, checkedId ->
            type = if (checkedId == R.id.rb_type_out) TypeEnum.OUT.value() else TypeEnum.IN.value()
            setSpinnerContent()
        }
        // 按钮点击事件
        btn_add.setOnClickListener {
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
     * 判断是否合法
     *
     * @return 是否合法
     */
    private fun checkParamLegal(): Boolean {
        if (et_money.text.isNullOrBlank()) {
            toast(R.string.add_need_money)
            return false
        }
        if (spinner_type.selectedItem.toString().isEmpty()) {
            ToastUtils.showShort(R.string.add_need_money)
            return false
        }
        return true
    }

    /**
     * 修改页面，先填上原来的数据
     */
    private fun initData() {
        btn_add.text = getString(R.string.update_btn)
        val date = recordToUpdate.date?.split("-")?.let {
            val year = it[0].toInt()
            val month = it[1].toInt()
            val dayOfMonth = it[2].toInt()
            date_picker.updateDate(year, month - 1, dayOfMonth)
        }
        et_money.setText(recordToUpdate.money.toString())
        et_detail.setText(recordToUpdate.detail)
        // 收支类型判断
        type = recordToUpdate.type
        val types = TypeDao.getTypesByType(type)
        rg_type.check(if (type == TypeEnum.IN.value()) R.id.rb_type_in else R.id.rb_type_out)
        setSpinnerContent()
        // 具体类型名称
        var selected = 0
        for ((index, type) in types.withIndex()) {
            if (type.name == recordToUpdate.typeName) {
                selected = index
                break
            }
        }
        spinner_type.setSelection(selected, true)
    }

    override fun onResume() {
        super.onResume()
        setSpinnerContent()
        if (isUpdate) {
            initData()
        }
    }

    private fun saveOrUpdateRecord(): Boolean {
        val y = date_picker.year
        val m = date_picker.month + 1
        val d = date_picker.dayOfMonth
        val date = getFormatYMD(y, m, d)
        return RecordDao.addOrUpdateRecord(isUpdate,
                recordToUpdate.id,
                date,
                et_money.text.toString().toDouble(),
                et_detail.text.toString(),
                type,
                spinner_type.selectedItem.toString())
    }

    /**
     * 根据收支的选择填充spinner的内容
     */
    private fun setSpinnerContent() {
        val types = TypeDao.getTypesByType(type)
        if (types.isEmpty()) {
            ToastUtils.showShort(R.string.add_no_type)
            return
        }
        val typeNames = ArrayList<String>()
        types.forEach { typeNames.add(it.name ?: "") }
        val spinnerAdapter = ArrayAdapter<String>(btn_add.context, R.layout.item_spinner, typeNames)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinner_type.adapter = spinnerAdapter
    }

    /**
     * 重置清空
     */
    private fun reset() {
        val calendar = Calendar.getInstance()
        date_picker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        et_money.setText("")
        et_detail.setText("")
        type = TypeEnum.OUT.value()
        rg_type.check(R.id.rb_type_out)
        setSpinnerContent()
    }

}
