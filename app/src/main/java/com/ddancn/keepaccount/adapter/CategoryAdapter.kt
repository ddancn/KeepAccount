package com.ddancn.keepaccount.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.entity.Category

/**
 * @author ddan.zhuang
 */
class CategoryAdapter(layoutResId: Int, data: List<Category>? = null)
    : BaseQuickAdapter<Category, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Category) {
        helper.setText(R.id.tv_category, item.name)
    }

}
