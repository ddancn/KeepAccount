package com.ddancn.keepaccount.activity;

import android.content.Context;
import android.content.Intent;

import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.entity.Record;
import com.ddancn.lib.base.BaseActivity;

/**
 * @author ddan.zhuang
 */
public class UpdateActivity extends BaseActivity {

    private Record recordToUpdate;
    public static final String EXTRA_ARG = "recordToUpdate";

    public Record getRecordToUpdate() {
        return recordToUpdate;
    }

    public static void start(Context context, Record recordToUpdate){
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtra(EXTRA_ARG, recordToUpdate);
        context.startActivity(intent);
    }

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.update_title);
    }

    @Override
    protected void initParam() {
        recordToUpdate = (Record) getIntent().getSerializableExtra(EXTRA_ARG);
    }

    @Override
    protected void initView() {
        enableLeftBack();
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_update;
    }


}
