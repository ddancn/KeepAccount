package com.ddancn.keepaccount.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.constant.TypeEnum
import com.ddancn.keepaccount.entity.Record

/**
 * @author ddan.zhuang
 */
class RecordAdapter(layoutResId: Int, data: List<Record>? = null)
    : BaseQuickAdapter<Record, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Record) {
        helper.setText(R.id.tv_date, item.date)
                .setText(R.id.tv_category, item.categoryName)
                .setText(R.id.tv_detail, item.detail)
                .setText(R.id.tv_money, item.money.toString())
                .setTextColor(R.id.tv_money, if (item.type == TypeEnum.IN.value())
                    mContext.resources.getColor(R.color.colorPrimary)
                else mContext.resources.getColor(R.color.colorText))
    }

}
