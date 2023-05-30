package com.ddancn.lib.view.dialog;

import android.content.Context
import android.view.View
import com.ddancn.lib.R
import com.ddancn.lib.databinding.DialogConfirmBinding

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
class ConfirmDialog(
    context: Context,
    private val confirmText: String = "确定",
    private val cancelText: String = "取消",
    private val title: String = "",
    private val message: String = "",
    private val confirmListener: (() -> Boolean)? = null,
    private val cancelListener: (() -> Boolean)? = null
) : BaseDialog<DialogConfirmBinding>(context, R.style.CustomDialog) {

    override fun initView() {
        vb.btnCancel.visibility = View.GONE
        vb.tvTitle.visibility = View.GONE
        vb.tvMsg.visibility = View.GONE
        vb.viewDividerVertical.visibility = View.GONE

        if (title.isNotBlank()) {
            vb.tvTitle.text = title
            vb.tvTitle.visibility = View.VISIBLE
        }
        if (message.isNotBlank()) {
            vb.tvMsg.text = message
            vb.tvMsg.visibility = View.VISIBLE
        }
        if (confirmText.isNotBlank()) {
            vb.btnConfirm.text = confirmText
            vb.btnConfirm.visibility = View.VISIBLE
        }
        if (cancelText.isNotBlank()) {
            vb.btnCancel.text = cancelText
            vb.btnCancel.visibility = View.VISIBLE
        }
        if (confirmText.isNotBlank() && cancelText.isNotEmpty()) {
            vb.viewDividerVertical.visibility = View.VISIBLE
        }
        vb.btnConfirm.setOnClickListener {
            if (confirmListener == null || confirmListener.invoke()) {
                cancel()
            }
        }
        vb.btnCancel.setOnClickListener {
            if (cancelListener == null || cancelListener.invoke()) {
                cancel()
            }
        }
    }
}