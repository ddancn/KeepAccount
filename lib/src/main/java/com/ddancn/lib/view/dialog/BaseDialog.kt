package com.ddancn.lib.view.dialog;

import android.app.Dialog
import android.content.Context
import android.os.Bundle

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
abstract class BaseDialog @JvmOverloads constructor(context: Context?, var layoutResId: Int = 0, themeResId: Int = 0)
    : Dialog(context, themeResId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        initView()
    }

    /**
     * 初始化
     */
    protected abstract fun initView()

    interface OnBtnClickListener {
        /**
         * 点击事件
         *
         * @return 是否cancel
         */
        fun onClick(): Boolean
    }
}