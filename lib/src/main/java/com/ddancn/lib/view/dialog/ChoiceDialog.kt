package com.ddancn.lib.view.dialog

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ConvertUtils
import com.ddancn.lib.R
import com.ddancn.lib.databinding.DialogChoiceBinding

/**
 * @author ddan.zhuang
 * @date 2023/6/29
 * @description
 */
class ChoiceDialog(
    context: Context,
    private val title: String = "",
    private val message: String = "",
    private val choices: List<String>,
    private val listeners: List<((Int) -> Boolean)?>,
    private val needCancel: Boolean = true
) :
    BaseDialog<DialogChoiceBinding>(context, R.style.CustomDialog) {

    override fun initView() {
        vb.tvTitle.text = title
        vb.tvTitle.visibility = if (title.isBlank()) View.GONE else View.VISIBLE
        vb.tvMsg.text = message
        vb.tvMsg.visibility = if (message.isBlank()) View.GONE else View.VISIBLE

        choices.forEachIndexed { i, choice ->
            vb.llRoot.addView(createDivider())
            vb.llRoot.addView(createBtn(choice).apply {
                setOnClickListener {
                    if (listeners[i]?.invoke(i) == true)
                        dismiss()
                }
            })
        }
        if (needCancel) {
            vb.llRoot.addView(createDivider())
            vb.llRoot.addView(createBtn(context.getString(R.string.btn_cancel)).apply {
                setTextColor(ContextCompat.getColor(context, R.color.colorCancelText))
                setOnClickListener { dismiss() }
            })
        }
    }

    private fun createBtn(choice: String) =
        Button(context).apply {
            text = choice
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ConvertUtils.dp2px(45f)
            )
            background = null
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }

    private fun createDivider() =
        View(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ConvertUtils.dp2px(1f)
            )
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorDivider))
        }
}