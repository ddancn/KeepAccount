package com.ddancn.lib.view.dialog

import android.content.Context
import android.view.View
import com.blankj.utilcode.util.KeyboardUtils
import com.ddancn.lib.R
import com.ddancn.lib.databinding.DialogInputBinding

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
class InputDialog(
    context: Context,
    private val confirmText: String = "确定",
    private val cancelText: String = "取消",
    private val title: String = "",
    private val etMsg: String = "",
    private val confirmListener: (input: String) -> Boolean,
    private val cancelListener: (() -> Boolean)? = null
) : BaseDialog<DialogInputBinding>(context, R.style.CustomDialog) {

    override fun initView() {
        vb.btnCancel.visibility = View.GONE
        vb.tvTitle.visibility = View.GONE
        vb.viewDividerVertical.visibility = View.GONE

        KeyboardUtils.showSoftInput(vb.editText)

        if (title.isNotBlank()) {
            vb.tvTitle.text = title
            vb.tvTitle.visibility = View.VISIBLE
        }
        if (etMsg.isNotBlank()) {
            vb.editText.setText(etMsg)
            vb.editText.visibility = View.VISIBLE
            vb.editText.selectAll()
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
            if (confirmListener.invoke(vb.editText.text.toString())) {
                cancel()
            }
        }
        vb.btnCancel.setOnClickListener {
            if (cancelListener == null || cancelListener.invoke()) {
                cancel()
            }
        }
        setOnCancelListener {
            KeyboardUtils.hideSoftInput(vb.editText)
        }
    }

    override fun onDetachedFromWindow() {
        KeyboardUtils.hideSoftInput(vb.editText)
    }

}
