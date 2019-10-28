package com.ddancn.keepaccount.fragment

import android.text.Editable
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.activity.UpdateActivity
import com.ddancn.keepaccount.adapter.RecordAdapter
import com.ddancn.keepaccount.dao.RecordDao
import com.ddancn.lib.base.BaseFragment
import com.ddancn.lib.util.*
import com.ddancn.lib.view.dialog.BaseDialog
import com.ddancn.lib.view.dialog.ConfirmDialog
import com.ddancn.lib.view.dialog.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_record.*

/**
 * @author ddan.zhuang
 */
class RecordFragment : BaseFragment() {

    private var recordAdapter = RecordAdapter(R.layout.item_record)
    private var showMonth: String = getThisMonth()

    override fun bindLayout(): Int {
        return R.layout.fragment_record
    }

    override fun hasHeader(): Boolean {
        return false
    }

    override fun initView() {
        rv_record.layoutManager = LinearLayoutManager(context)
        recordAdapter.emptyView = getEmptyTextView()
        recordAdapter.setFooterView(getFooterTextView())
        rv_record.adapter = recordAdapter
    }

    override fun bindListener() {
        icon_date.setOnClickListener {
            DatePickerDialog.getYMPickerFromToday(icon_date.context,
                    android.app.DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                        showMonth = getFormatYM(year, month + 1)
                        toast(showMonth)
                        getRecords()
                    }).show()
        }
        //输入时即搜索
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
        //搜索框清空按钮的事件
        et_search.setOnTouchListener { v, event ->
            if (event.x > et_search.width
                    - et_search.paddingRight
                    - et_search.compoundDrawables[2].intrinsicWidth) {
                et_search.setText("")
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
        recordAdapter.setOnItemClickListener { adapter, view, position ->
            UpdateActivity.start(view.context, recordAdapter.getItem(position)) }
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
                                ToastUtils.showShort(R.string.record_delete_succeed)
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
            ToastUtils.showShort(R.string.record_search_no_result)
        }
    }
}
