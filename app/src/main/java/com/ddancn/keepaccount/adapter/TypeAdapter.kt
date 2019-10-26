package com.ddancn.keepaccount.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.entity.Type

/**
 * @author ddan.zhuang
 */
class TypeAdapter(layoutResId: Int, data: List<Type>? = null)
    : BaseQuickAdapter<Type, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Type) {
        helper.setText(R.id.tv_type, item.name)
    }

}
