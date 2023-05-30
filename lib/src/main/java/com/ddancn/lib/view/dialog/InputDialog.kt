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
class InputDialog(context: Context,
                  private val confirmText: String? = null,
                  private val cancelText: String? = null,
                  private val title: String? = null,
                  private val etMsg: String? = null,
                  private val confirmListener: OnConfirmListenerWithInput? = null,
                  private val cancelListener: OnBtnClickListener? = null)
    : BaseDialog<DialogInputBinding>(context, R.style.CustomDialog) {

    override fun initView() {
        vb.btnCancel.visibility = View.GONE
        vb.tvTitle.visibility = View.GONE
        vb.viewDividerVertical.visibility = View.GONE
        vb.editText.requestFocus()

        KeyboardUtils.toggleSoftInput()

        if (!title.isNullOrBlank()) {
            vb.tvTitle.text = title
            vb.tvTitle.visibility = View.VISIBLE
        }
        if (!etMsg.isNullOrBlank()) {
            vb.editText.setText(etMsg)
            vb.editText.visibility = View.VISIBLE
            vb.editText.selectAll()
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
            if (confirmListener?.onClick(vb.editText.text.toString()) == true) {
                cancel()
            }
        }
        vb.btnCancel.setOnClickListener {
            if (cancelListener == null || cancelListener.onClick()) {
                cancel()
            }
        }
        setOnCancelListener {
            KeyboardUtils.toggleSoftInput()
        }
    }

    /**
     * 返回输入的确定点击事件
     */
    interface OnConfirmListenerWithInput {
        /**
         * 点击确定
         *
         * @param input 输入内容
         * @return 是否dismiss
         */
        fun onClick(input: String): Boolean
    }

}
