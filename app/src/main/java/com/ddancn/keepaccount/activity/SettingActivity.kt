package com.ddancn.keepaccount.activity

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.adapter.CategoryAdapter
import com.ddancn.keepaccount.constant.TypeEnum
import com.ddancn.keepaccount.dao.CategoryDao
import com.ddancn.keepaccount.dao.RecordDao
import com.ddancn.keepaccount.databinding.ActivitySettingBinding
import com.ddancn.keepaccount.entity.Category
import com.ddancn.keepaccount.exception.CategoryNameDuplicateException
import com.ddancn.keepaccount.util.getEmptyTextView
import com.ddancn.lib.base.BaseActivity
import com.ddancn.lib.view.dialog.ConfirmDialog
import com.ddancn.lib.view.dialog.InputDialog

/**
 * @author ddan.zhuang
 * 设置类型页面
 */
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    private val inAdapter = CategoryAdapter(R.layout.item_category)
    private val outAdapter = CategoryAdapter(R.layout.item_category)

    override fun initView() {
        headerView.setTitle(R.string.setting_title)
        enableLeftBack()

        vb.rvCategoryIn.layoutManager = GridLayoutManager(this, 3)
        vb.rvCategoryOut.layoutManager = GridLayoutManager(this, 3)
        inAdapter.emptyView = getEmptyTextView(getString(R.string.setting_no_in_category))
        outAdapter.emptyView = getEmptyTextView(getString(R.string.setting_no_out_category))
        vb.rvCategoryIn.adapter = inAdapter
        vb.rvCategoryOut.adapter = outAdapter
    }

    override fun bindListener() {
        vb.iconAddIn.setOnClickListener { addCategory(TypeEnum.IN.value()) }
        vb.iconAddOut.setOnClickListener { addCategory(TypeEnum.OUT.value()) }
        inAdapter.onItemClickListener = CategoryItemClickItemListener()
        inAdapter.onItemLongClickListener = CategoryItemLongClickListener()
        outAdapter.onItemClickListener = CategoryItemClickItemListener()
        outAdapter.onItemLongClickListener = CategoryItemLongClickListener()
    }

    override fun applyData() {
        inAdapter.setNewData(CategoryDao.getCategoriesByType(TypeEnum.IN.value()))
        outAdapter.setNewData(CategoryDao.getCategoriesByType(TypeEnum.OUT.value()))
    }

    private fun getAdapter(type: Int): CategoryAdapter {
        return if (type == TypeEnum.IN.value()) inAdapter else outAdapter
    }

    private fun addCategory(type: Int) {
        InputDialog(
            context = this,
            title = getString(R.string.setting_add_category),
            confirmListener = { input ->
                if (input.isBlank()) {
                    toast(R.string.setting_need_category_name)
                    return@InputDialog false
                }
                try {
                    if (CategoryDao.addCategory(input, type)) {
                        toast(R.string.setting_add_succeed)
                        applyData()
                    } else {
                        toast(R.string.setting_add_fail)
                    }
                } catch (e: CategoryNameDuplicateException) {
                    toast(e.message)
                }
                return@InputDialog true
            }).show()
    }

    inner class CategoryItemClickItemListener : BaseQuickAdapter.OnItemClickListener {
        override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            val categoryToUpdate = adapter?.data?.get(position) as Category
            val type = categoryToUpdate.type
            InputDialog(context = this@SettingActivity,
                title = getString(R.string.setting_update_category),
                etMsg = categoryToUpdate.name,
                confirmText = getString(R.string.setting_update),
                confirmListener = { input ->
                    if (input.isBlank()) {
                        toast(R.string.setting_need_category_name)
                        return@InputDialog false
                    }
                    //修改类型，更新类型相关的记录
                    try {
                        if (CategoryDao.updateCategoryName(categoryToUpdate.id, input) == 1
                            && RecordDao.updateRecordCategoryName(input, categoryToUpdate.name) >= 0
                        ) {
                            toast(R.string.setting_update_succeed)
                            getAdapter(type).data[position].name = (input)
                            getAdapter(type).notifyItemChanged(position)
                            return@InputDialog true
                        }
                    } catch (e: CategoryNameDuplicateException) {
                        toast(e.message)
                    }
                    return@InputDialog false

                }).show()
        }
    }

    inner class CategoryItemLongClickListener : BaseQuickAdapter.OnItemLongClickListener {
        override fun onItemLongClick(
            adapter: BaseQuickAdapter<*, *>?,
            view: View?,
            position: Int
        ): Boolean {
            val categoryToDelete = adapter?.data?.get(position) as Category
            val type = categoryToDelete.type
            ConfirmDialog(context = this@SettingActivity,
                title = getString(R.string.setting_delete_category),
                message = getString(R.string.setting_delete_hint),
                confirmText = getString(R.string.setting_delete),
                confirmListener = {
                    //先删除相关的记录，并删除类型
                    if (RecordDao.deleteRecordByCategoryName(categoryToDelete.name) >= 0
                        && CategoryDao.deleteCategory(categoryToDelete.id) == 1
                    ) {
                        toast(R.string.setting_delete_succeed)
                        getAdapter(type).remove(position)
                    }
                    return@ConfirmDialog true
                }).show()
            return false
        }
    }
}
