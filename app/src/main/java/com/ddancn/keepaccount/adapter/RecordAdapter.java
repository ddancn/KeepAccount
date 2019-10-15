package com.ddancn.keepaccount.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.entity.Record;

import java.util.List;

public class RecordAdapter extends BaseQuickAdapter<Record, BaseViewHolder> {


    public RecordAdapter(int layoutResId, @Nullable List<Record> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Record item) {
        helper.setText(R.id.tv_date, item.getDate());
        helper.setText(R.id.tv_type, item.getTypeName());
        helper.setText(R.id.tv_detail, item.getDetail());
        helper.setText(R.id.tv_money, String.valueOf(item.getMoney()));
    }


}
