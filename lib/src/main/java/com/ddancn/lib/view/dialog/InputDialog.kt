package com.ddancn.lib.view.dialog

import android.content.Context
import android.view.View
import com.blankj.utilcode.util.KeyboardUtils
import com.ddancn.lib.R
import kotlinx.android.synthetic.main.dialog_input.*

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
    : BaseDialog(context, R.layout.dialog_input, R.style.CustomDialog) {

    override fun initView() {
        btn_cancel.visibility = View.GONE
        tv_title.visibility = View.GONE
        view_divider_vertical.visibility = View.GONE

        KeyboardUtils.toggleSoftInput()

        if (!title.isNullOrBlank()) {
            tv_title.text = title
            tv_title.visibility = View.VISIBLE
        }
        if (!etMsg.isNullOrBlank()) {
            edit_text.setText(etMsg)
            edit_text.visibility = View.VISIBLE
        }
        if (!confirmText.isNullOrBlank()) {
            btn_confirm.text = confirmText
            btn_confirm.visibility = View.VISIBLE
        }
        if (!cancelText.isNullOrBlank()) {
            btn_cancel.text = cancelText
            btn_cancel.visibility = View.VISIBLE
        }
        if (!confirmText.isNullOrBlank() && !cancelText.isNullOrEmpty()) {
            view_divider_vertical.visibility = View.VISIBLE
        }
        btn_confirm.setOnClickListener {
            if (confirmListener?.onClick(edit_text.text.toString()) == true) {
                cancel()
            }
        }
        btn_cancel.setOnClickListener {
            if (cancelListener?.onClick() == true) {
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
