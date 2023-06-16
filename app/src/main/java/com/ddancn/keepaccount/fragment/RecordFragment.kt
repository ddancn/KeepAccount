package com.ddancn.keepaccount.fragment

import android.annotation.SuppressLint
import android.text.Editable
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.activity.UpdateActivity
import com.ddancn.keepaccount.adapter.RecordAdapter
import com.ddancn.keepaccount.dao.RecordDao
import com.ddancn.keepaccount.databinding.FragmentRecordBinding
import com.ddancn.keepaccount.entity.Record
import com.ddancn.keepaccount.util.*
import com.ddancn.keepaccount.vo.RecordSection
import com.ddancn.lib.base.BaseFragment
import com.ddancn.lib.view.dialog.ConfirmDialog
import com.ddancn.lib.view.dialog.DatePickerDialog

/**
 * @author ddan.zhuang
 * 记录列表页面
 */
class RecordFragment : BaseFragment<FragmentRecordBinding>() {

    private var recordAdapter = RecordAdapter(R.layout.item_record, R.layout.item_record_header)
    private var showMonth: String = getThisMonth()
    private var keyword: String = ""

    override fun hasHeader(): Boolean = false

    override fun initView() {
        vb.rvRecord.layoutManager = LinearLayoutManager(context)
        recordAdapter.emptyView = getEmptyTextView()
        recordAdapter.setFooterView(getFooterTextView())
        vb.rvRecord.adapter = recordAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun bindListener() {
        // 选择日期
        vb.iconDate.setOnClickListener {
            DatePickerDialog.getYMPickerFromToday(
                vb.iconDate.context
            ) { datePicker, year, month, dayOfMonth ->
                showMonth = getFormatYM(year, month + 1)
                toast(getString(R.string.record_switch_month, showMonth))
                // 查记录时清空可能有的搜索内容
                vb.etSearch.setText("")
                keyword = ""
                getRecords()
            }.show()
        }
        // 输入实时搜索
        vb.etSearch.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                keyword = query
                getRecords()
            }
        })
        // 搜索框清空按钮的事件
        vb.etSearch.setOnTouchListener { v, event ->
            if (event.x > vb.etSearch.width
                - vb.etSearch.paddingRight
                - vb.etSearch.compoundDrawables[2].intrinsicWidth
            ) {
                vb.etSearch.setText("")
                keyword = ""
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
        // 点击记录去修改
        recordAdapter.setOnItemClickListener { adapter, view, position ->
            val item = recordAdapter.getItem(position)
            if (item?.isHeader == true) return@setOnItemClickListener
            UpdateActivity.start(view.context, item?.t)
        }
        // 长按记录来删除
        recordAdapter.setOnItemLongClickListener { adapter, view, position ->
            val item = recordAdapter.getItem(position)
            if (item?.isHeader == true) return@setOnItemLongClickListener false
            ConfirmDialog(view.context,
                title = getString(R.string.record_delete_record),
                message = getString(R.string.record_delete_hint),
                confirmText = getString(R.string.record_delete),
                cancelText = getString(R.string.cancel),
                confirmListener = {
                    if (RecordDao.deleteRecordById(item?.t?.id ?: -1) == 1) {
                        toast(R.string.record_delete_succeed)
                        recordAdapter.remove(position)
                    }
                    return@ConfirmDialog true
                }).show()
            return@setOnItemLongClickListener true
        }
    }

    override fun onResume() {
        super.onResume()
        getRecords()
    }

    /**
     * 从数据库中查询记录
     */
    private fun getRecords() {
        if (keyword.isBlank()) {
            setSections(RecordDao.getRecordsByMonth(showMonth))
        } else {
            setSections(RecordDao.searchRecord(keyword))
        }
        if (recordAdapter.data.isEmpty()) {
            toast(R.string.record_search_no_result)
        }
    }

    private fun setSections(map: Map<String, List<Record>>) {
        val list = ArrayList<RecordSection>()
        for ((date, records) in map) {
            list.add(RecordSection(true, date))
            records.forEach { r ->
                list.add(RecordSection(r))
            }
        }
        recordAdapter.setNewData(list)
    }
}
