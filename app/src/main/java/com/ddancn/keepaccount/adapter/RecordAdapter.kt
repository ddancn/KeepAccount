package com.ddancn.keepaccount.adapter

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.constant.TypeEnum
import com.ddancn.keepaccount.vo.RecordSection

/**
 * @author ddan.zhuang
 */
class RecordAdapter(@LayoutRes layoutResId: Int, @LayoutRes sectionHeadResId: Int, data: List<RecordSection>? = null) :
    BaseSectionQuickAdapter<RecordSection, BaseViewHolder>(layoutResId, sectionHeadResId, data) {

    override fun convert(helper: BaseViewHolder, item: RecordSection) {
        val record = item.t
        helper.setText(R.id.tv_category, record.categoryName)
            .setText(R.id.tv_detail, record.detail)
            .setText(R.id.tv_money, record.money.toString())
            .setTextColor(
                R.id.tv_money, if (record.type == TypeEnum.IN.value())
                    mContext.resources.getColor(R.color.colorPrimary, mContext.theme)
                else mContext.resources.getColor(R.color.colorText, mContext.theme)
            )
    }

    override fun convertHead(helper: BaseViewHolder, item: RecordSection) {
        helper.setText(R.id.tv_date, item.header)
    }
}
