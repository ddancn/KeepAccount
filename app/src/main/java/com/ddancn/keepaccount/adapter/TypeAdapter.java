package com.ddancn.keepaccount.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.entity.Type;

import java.util.List;

public class TypeAdapter extends BaseQuickAdapter<Type, BaseViewHolder> {


    public TypeAdapter(int layoutResId, @Nullable List<Type> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Type item) {
        helper.setText(R.id.tv_type, item.getName());
    }


}
