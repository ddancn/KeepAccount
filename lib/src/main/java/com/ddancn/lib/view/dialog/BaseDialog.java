package com.ddancn.lib.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
public abstract class BaseDialog extends Dialog {

    private int mLayoutResId;

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int layoutResId) {
        super(context);
        mLayoutResId = layoutResId;
    }

    public BaseDialog(Context context, int layoutResId, int themeResId) {
        super(context, themeResId);
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
         * @return 是否cancel
         */
        boolean onClick();
    }
}