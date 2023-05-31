package com.ddancn.keepaccount.fragment

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.NonNull
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.activity.SettingActivity
import com.ddancn.keepaccount.activity.UpdateActivity
import com.ddancn.keepaccount.adapter.AddCategoryAdapter
import com.ddancn.keepaccount.constant.TypeEnum
import com.ddancn.keepaccount.dao.CategoryDao
import com.ddancn.keepaccount.dao.RecordDao
import com.ddancn.keepaccount.databinding.FragmentAddBinding
import com.ddancn.keepaccount.entity.Category
import com.ddancn.keepaccount.entity.Record
import com.ddancn.keepaccount.util.getFormatYMD
import com.ddancn.lib.base.BaseFragment
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.util.*

/**
 * @author ddan.zhuang
 * 添加或修改记录页面
 */
class AddFragment : BaseFragment<FragmentAddBinding>() {

    private var type = TypeEnum.OUT.value()
    private var isUpdate = false
    private lateinit var recordToUpdate: Record

    private val categoryAdapter = AddCategoryAdapter(R.layout.item_add_category)
    private var selectedCate: Category? = null

    override fun hasHeader(): Boolean = !isUpdate

    override fun onAttach(@NonNull context: Context) {
        super.onAttach(context)
        isUpdate = context is UpdateActivity
    }

    override fun onStart() {
        super.onStart()
        recordToUpdate = (activity as? UpdateActivity)?.recordToUpdate ?: Record()
//        view?.postDelayed({
//            KeyboardUtils.showSoftInput(vb.etMoney)
//        }, 500)
    }

    override fun initView() {
        headerView.setTitle(R.string.app_name)
        headerView.setRightImage(R.drawable.ic_setting)

        val manager = FlexboxLayoutManager(context)
        // 主轴方向排列
        manager.justifyContent = JustifyContent.FLEX_START
        // 次轴方向排列
        manager.alignItems = AlignItems.CENTER
        vb.rvCategory.layoutManager = manager
        vb.rvCategory.adapter = categoryAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun bindListener() {
        headerView.setRightClickListener { start(SettingActivity::class.java) }
        // 选择收支大类，联动改变类型列表的内容
        vb.rgType.setOnCheckedChangeListener { group, checkedId ->
            type = if (checkedId == R.id.rb_type_out) TypeEnum.OUT.value() else TypeEnum.IN.value()
            setCategoryList()
        }
        categoryAdapter.setOnItemClickListener { adapter, view, position ->
            selectedCate = categoryAdapter.data[position]
            categoryAdapter.setSelectedPos(position)
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
        if (selectedCate == null) {
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
        setCategoryList()

        // 填充具体类型名称
        var selected = 0
        categories.forEachIndexed { index, category ->
            if (category.name == recordToUpdate.categoryName) {
                selected = index
            }
        }
        categoryAdapter.setSelectedPos(selected)
        selectedCate = categoryAdapter.data[selected]
    }

    override fun onResume() {
        super.onResume()
        if (isUpdate) {
            fillOldData()
        } else {
            setCategoryList()
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
            categoryName = selectedCate!!.name
        )

        return if (isUpdate) {
            record.id = recordToUpdate.id
            RecordDao.updateRecord(record)
        } else {
            RecordDao.addRecord(record)
        }
    }

    /**
     * 根据收支的选择填充类型的内容
     */
    private fun setCategoryList() {
        val categories = CategoryDao.getCategoriesByType(type)
        if (categories.isEmpty()) {
            toast(R.string.add_no_category)
            categoryAdapter.setNewData(emptyList())
            selectedCate = null
            return
        }

        categoryAdapter.setNewData(categories)
        categoryAdapter.setSelectedPos(0)
        selectedCate = categoryAdapter.data[0]
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
        setCategoryList()
    }

}
