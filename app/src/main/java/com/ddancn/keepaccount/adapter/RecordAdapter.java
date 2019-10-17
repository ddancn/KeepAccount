package com.ddancn.keepaccount.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.constant.TypeEnum;
import com.ddancn.keepaccount.entity.Record;

import java.util.List;

/**
 * @author ddan.zhuang
 */
public class RecordAdapter extends BaseQuickAdapter<Record, BaseViewHolder> {

    public RecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    public RecordAdapter(int layoutResId, @Nullable List<Record> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Record item) {
        helper.setText(R.id.tv_date, item.getDate())
                .setText(R.id.tv_type, item.getTypeName())
                .setText(R.id.tv_detail, item.getDetail())
                .setText(R.id.tv_money, String.valueOf(item.getMoney()))
                .setTextColor(R.id.tv_money, item.getType() == TypeEnum.IN.value()
                        ? mContext.getResources().getColor(R.color.colorPrimary)
                        : mContext.getResources().getColor(R.color.colorText));
    }

}
