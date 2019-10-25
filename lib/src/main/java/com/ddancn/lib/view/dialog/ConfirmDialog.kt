package com.ddancn.lib.view.dialog;

import android.content.Context
import android.view.View
import com.ddancn.lib.R
import kotlinx.android.synthetic.main.dialog_confirm.*

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
class ConfirmDialog @JvmOverloads constructor(context: Context,
                    private val confirmText: String? = null,
                    private val cancelText: String? = null,
                    private val title: String? = null,
                    private val message: String? = null,
                    private val confirmListener: OnBtnClickListener? = null,
                    private val cancelListener: OnBtnClickListener? = null)
    : BaseDialog(context, R.layout.dialog_confirm, R.style.CustomDialog) {

    override fun initView() {
        btn_cancel.visibility = View.GONE
        tv_title.visibility = View.GONE
        tv_msg.visibility = View.GONE
        view_divider_vertical.visibility = View.GONE

        if (!title.isNullOrBlank()) {
            tv_title.text = title
            tv_title.visibility = View.VISIBLE
        }
        if (!message.isNullOrBlank()) {
            tv_msg.text = message
            tv_msg.visibility = View.VISIBLE
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
            if (confirmListener?.onClick() == true) {
                cancel()
            }
        }
        btn_cancel.setOnClickListener {
            if (cancelListener?.onClick() == true) {
                cancel()
            }
        }
    }
}