package com.ddancn.keepaccount.fragment

import android.annotation.SuppressLint
import android.text.Editable
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.activity.UpdateActivity
import com.ddancn.keepaccount.adapter.RecordAdapter
import com.ddancn.keepaccount.dao.RecordDao
import com.ddancn.keepaccount.util.getEmptyTextView
import com.ddancn.keepaccount.util.getFooterTextView
import com.ddancn.lib.base.BaseFragment
import com.ddancn.lib.util.SimpleTextWatcher
import com.ddancn.lib.util.getFormatYM
import com.ddancn.lib.util.getThisMonth
import com.ddancn.lib.view.dialog.BaseDialog
import com.ddancn.lib.view.dialog.ConfirmDialog
import com.ddancn.lib.view.dialog.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_record.*

/**
 * @author ddan.zhuang
 * 记录列表页面
 */
class RecordFragment : BaseFragment() {

    private var recordAdapter = RecordAdapter(R.layout.item_record)
    private var showMonth: String = getThisMonth()

    override fun bindLayout(): Int = R.layout.fragment_record

    override fun hasHeader(): Boolean = false

    override fun initView() {
        rv_record.layoutManager = LinearLayoutManager(context)
        recordAdapter.emptyView = getEmptyTextView()
        recordAdapter.setFooterView(getFooterTextView())
        rv_record.adapter = recordAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun bindListener() {
        // 选择日期
        icon_date.setOnClickListener {
            DatePickerDialog.getYMPickerFromToday(icon_date.context,
                    android.app.DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                        showMonth = getFormatYM(year, month + 1)
                        toast(getString(R.string.record_switch_month, showMonth))
                        getRecords()
                        // 查记录时清空可能有的搜索内容
                        et_search.setText("")
                    }).show()
        }
        // 输入实时搜索
        et_search.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isBlank()) {
                    getRecords()
                } else {
                    recordAdapter.setNewData(RecordDao.searchRecord(query))
                }
            }
        })
        // 搜索框清空按钮的事件
        et_search.setOnTouchListener { v, event ->
            if (event.x > et_search.width
                    - et_search.paddingRight
                    - et_search.compoundDrawables[2].intrinsicWidth) {
                et_search.setText("")
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
        // 点击记录去修改
        recordAdapter.setOnItemClickListener { adapter, view, position ->
            UpdateActivity.start(view.context, recordAdapter.getItem(position))
        }
        // 长按记录来删除
        recordAdapter.setOnItemLongClickListener { adapter, view, position ->
            ConfirmDialog(view.context,
                    title = getString(R.string.record_delete_record),
                    message = getString(R.string.record_delete_hint),
                    confirmText = getString(R.string.record_delete),
                    cancelText = getString(R.string.cancel),
                    confirmListener = object : BaseDialog.OnBtnClickListener {
                        override fun onClick(): Boolean {
                            val recordToDelete = recordAdapter.getItem(position)
                            if (RecordDao.deleteRecordById(recordToDelete?.id ?: -1) == 1) {
                                toast(R.string.record_delete_succeed)
                                recordAdapter.remove(position)
                            }
                            return true
                        }
                    }).show()
            false
        }
    }

    override fun onResume() {
        super.onResume()
        getRecords()
    }

    /**
     * 从数据库中查询某月的记录
     */
    private fun getRecords() {
        recordAdapter.setNewData(RecordDao.getRecordsByMonth(showMonth))
        if (recordAdapter.data.isEmpty()) {
            toast(R.string.record_search_no_result)
        }
    }
}
