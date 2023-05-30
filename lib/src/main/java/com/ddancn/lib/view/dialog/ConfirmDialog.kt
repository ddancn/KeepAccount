package com.ddancn.lib.view.dialog;

import android.content.Context
import android.view.View
import com.ddancn.lib.R
import com.ddancn.lib.databinding.DialogConfirmBinding

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
class ConfirmDialog(context: Context,
                    private val confirmText: String? = null,
                    private val cancelText: String? = null,
                    private val title: String? = null,
                    private val message: String? = null,
                    private val confirmListener: OnBtnClickListener? = null,
                    private val cancelListener: OnBtnClickListener? = null)
    : BaseDialog<DialogConfirmBinding>(context, R.style.CustomDialog) {

    override fun initView() {
        vb.btnCancel.visibility = View.GONE
        vb.tvTitle.visibility = View.GONE
        vb.tvMsg.visibility = View.GONE
        vb.viewDividerVertical.visibility = View.GONE

        if (!title.isNullOrBlank()) {
            vb.tvTitle.text = title
            vb.tvTitle.visibility = View.VISIBLE
        }
        if (!message.isNullOrBlank()) {
            vb.tvMsg.text = message
            vb.tvMsg.visibility = View.VISIBLE
        }
        if (!confirmText.isNullOrBlank()) {
            vb.btnConfirm.text = confirmText
            vb.btnConfirm.visibility = View.VISIBLE
        }
        if (!cancelText.isNullOrBlank()) {
            vb.btnCancel.text = cancelText
            vb.btnCancel.visibility = View.VISIBLE
        }
        if (!confirmText.isNullOrBlank() && !cancelText.isNullOrEmpty()) {
            vb.viewDividerVertical.visibility = View.VISIBLE
        }
        vb.btnConfirm.setOnClickListener {
            if (confirmListener?.onClick() == true) {
                cancel()
            }
        }
        vb.btnCancel.setOnClickListener {
            if (cancelListener == null || cancelListener.onClick()) {
                cancel()
            }
        }
    }
}