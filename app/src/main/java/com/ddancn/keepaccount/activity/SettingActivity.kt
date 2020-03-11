package com.ddancn.keepaccount.activity

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.adapter.TypeAdapter
import com.ddancn.keepaccount.constant.TypeEnum
import com.ddancn.keepaccount.dao.RecordDao
import com.ddancn.keepaccount.dao.TypeDao
import com.ddancn.keepaccount.entity.Type
import com.ddancn.keepaccount.exception.TypeNameDuplicateException
import com.ddancn.lib.base.BaseActivity
import com.ddancn.lib.util.getEmptyTextView
import com.ddancn.lib.view.dialog.BaseDialog
import com.ddancn.lib.view.dialog.ConfirmDialog
import com.ddancn.lib.view.dialog.InputDialog
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * @author ddan.zhuang
 * 设置类型页面
 */
class SettingActivity : BaseActivity() {

    private val inTypesAdapter = TypeAdapter(R.layout.item_type)
    private val outTypesAdapter = TypeAdapter(R.layout.item_type)

    override fun bindLayout(): Int {
        return R.layout.activity_setting
    }

    override fun setHeaderTitle(): String {
        return getString(R.string.setting_title)
    }

    override fun initView() {
        enableLeftBack()

        rv_type_in.layoutManager = GridLayoutManager(this, 3)
        rv_type_out.layoutManager = GridLayoutManager(this, 3)
        inTypesAdapter.emptyView = getEmptyTextView(getString(R.string.setting_no_in_type))
        outTypesAdapter.emptyView = getEmptyTextView(getString(R.string.setting_no_out_type))
        rv_type_in.adapter = inTypesAdapter
        rv_type_out.adapter = outTypesAdapter
    }

    override fun bindListener() {
        icon_add_in.setOnClickListener { addType(TypeEnum.IN.value()) }
        icon_add_out.setOnClickListener { addType(TypeEnum.OUT.value()) }
        inTypesAdapter.onItemClickListener = TypeItemClickItemListener()
        inTypesAdapter.onItemLongClickListener = TypeItemLongClickListener()
        outTypesAdapter.onItemClickListener = TypeItemClickItemListener()
        outTypesAdapter.onItemLongClickListener = TypeItemLongClickListener()
    }

    override fun applyData() {
        inTypesAdapter.setNewData(TypeDao.getTypesByType(TypeEnum.IN.value()))
        outTypesAdapter.setNewData(TypeDao.getTypesByType(TypeEnum.OUT.value()))
    }

    private fun getAdapter(type: Int): TypeAdapter {
        return if (type == TypeEnum.IN.value()) inTypesAdapter else outTypesAdapter
    }

    private fun addType(type: Int) {
        InputDialog(context = this,
                title = getString(R.string.setting_add_type),
                confirmText = getString(R.string.confirm),
                cancelText = getString(R.string.cancel),
                confirmListener = object : InputDialog.OnConfirmListenerWithInput {
                    override fun onClick(input: String): Boolean {
                        if (input.isBlank()) {
                            toast(R.string.setting_need_type_name)
                            return false
                        }
                        try {
                            if (TypeDao.addType(input, type)) {
                                toast(R.string.setting_add_succeed)
                                applyData()
                            } else {
                                toast(R.string.setting_add_fail)
                            }
                        } catch (e: TypeNameDuplicateException) {
                            toast(e.message)
                        }
                        return true
                    }
                }).show()
    }

    inner class TypeItemClickItemListener : BaseQuickAdapter.OnItemClickListener {
        override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            val typeToUpdate = adapter?.data?.get(position) as Type
            val type = typeToUpdate.type
            InputDialog(context = this@SettingActivity,
                    title = getString(R.string.setting_update_type),
                    confirmText = getString(R.string.setting_update),
                    cancelText = getString(R.string.cancel),
                    etMsg = typeToUpdate.name,
                    confirmListener = object : InputDialog.OnConfirmListenerWithInput {
                        override fun onClick(input: String): Boolean {
                            if (input.isBlank()) {
                                toast(R.string.setting_need_type_name)
                                return false
                            }
                            //修改类型，更新类型相关的记录
                            try {
                                if (TypeDao.updateTypeName(typeToUpdate.id, input) == 1
                                        && RecordDao.updateRecordTypeName(input, typeToUpdate.name) >= 0) {
                                    ToastUtils.showShort(R.string.setting_update_succeed)
                                    getAdapter(type).data[position].name = (input)
                                    getAdapter(type).notifyItemChanged(position)
                                    return true
                                }
                            } catch (e: TypeNameDuplicateException) {
                                toast(e.message)
                            }
                            return false
                        }
                    }).show()
        }
    }

    inner class TypeItemLongClickListener : BaseQuickAdapter.OnItemLongClickListener {
        override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int): Boolean {
            val typeToDelete = adapter?.data?.get(position) as Type
            val type = typeToDelete.type
            ConfirmDialog(context = this@SettingActivity,
                    title = getString(R.string.setting_delete_type),
                    message = getString(R.string.setting_delete_hint),
                    confirmText = getString(R.string.setting_delete),
                    cancelText = getString(R.string.cancel),
                    confirmListener = object : BaseDialog.OnBtnClickListener {
                        override fun onClick(): Boolean {
                            //先删除相关的记录，并删除类型
                            if (RecordDao.deleteRecordByTypeName(typeToDelete.name) >= 0
                                    && TypeDao.deleteType(typeToDelete.id) == 1) {
                                toast(R.string.setting_delete_succeed)
                                getAdapter(type).remove(position)
                                return true
                            }
                            return false
                        }
                    }).show()
            return false
        }
    }
}
