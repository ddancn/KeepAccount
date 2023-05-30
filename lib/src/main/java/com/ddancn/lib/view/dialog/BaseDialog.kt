package com.ddancn.lib.view.dialog;

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.ViewBindingUtil

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
abstract class BaseDialog<VB : ViewBinding>(context: Context, themeResId: Int = 0) :
    Dialog(context, themeResId) {

    protected lateinit var vb: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
        setContentView(vb.root)
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