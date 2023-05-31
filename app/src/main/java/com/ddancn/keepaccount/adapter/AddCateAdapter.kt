package com.ddancn.keepaccount.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.entity.Category

/**
 * @author ddan.zhuang
 * @date 2023/5/30
 * @description
 */
class AddCategoryAdapter(layoutResId: Int, data: List<Category>? = null) :
    BaseQuickAdapter<Category, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Category) {
        helper.setText(R.id.tv_category, item.name)
        helper.getView<TextView>(R.id.tv_category).isSelected = item.isSelected
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedPos(pos: Int) {
        data.forEachIndexed { index, category ->
            category.isSelected = index == pos
        }
        notifyDataSetChanged()
    }

}