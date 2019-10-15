package com.ddancn.lib.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
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

    /**
     * 初始化
     */
    protected abstract  void initView();

    public interface OnBtnClickListener {
        /**
         * 点击事件
         *
         * @return 是否dismiss
         */
        boolean onClick();
    }
}