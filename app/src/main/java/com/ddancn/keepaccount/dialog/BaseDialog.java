package com.ddancn.keepaccount.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public abstract class BaseDialog extends Dialog {

    protected Context mContext;
    protected int mLayoutResId;

    public BaseDialog(Context context) {
        super(context);
        mContext = context;
    }

    public BaseDialog(Context context, int layoutResId) {
        super(context);
        mContext = context;
        mLayoutResId = layoutResId;
    }

    public BaseDialog(Context context, int layoutResId, int themeResId) {
        super(context, themeResId);
        mContext = context;
        mLayoutResId = layoutResId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mLayoutResId);
        initView();
    }

    abstract protected void initView();
}