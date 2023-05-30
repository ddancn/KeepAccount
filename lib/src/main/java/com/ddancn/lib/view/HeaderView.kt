package com.ddancn.lib.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ddancn.lib.databinding.ViewHeaderBinding

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
class HeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var vb: ViewHeaderBinding =
        ViewHeaderBinding.inflate(LayoutInflater.from(context), this, true)

    private var leftClickListener: OnClickListener? = null
    private var rightClickListener: OnClickListener? = null

    init {
        vb.ivLeft.setOnClickListener { leftClickListener?.onClick(it) }
        vb.tvLeft.setOnClickListener { leftClickListener?.onClick(it) }
        vb.ivRight.setOnClickListener { rightClickListener?.onClick(it) }
        vb.tvRight.setOnClickListener { rightClickListener?.onClick(it) }
    }

    fun setTitle(text: String) {
        vb.tvTitle.text = text
    }

    fun setTitle(@StringRes resId: Int) {
        setTitle(context.getString(resId))
    }

    fun setLeftText(text: String) {
        vb.tvLeft.visibility = View.VISIBLE
        vb.ivLeft.visibility = View.GONE
        vb.tvLeft.text = text
    }

    fun setLeftText(@StringRes resId: Int) {
        setLeftText(context.getString(resId))
    }

    fun setRightText(text: String) {
        vb.tvRight.visibility = View.VISIBLE
        vb.ivRight.visibility = View.GONE
        vb.tvRight.text = text
    }

    fun setRightText(@StringRes resId: Int) {
        setRightText(context.getString(resId))
    }

    fun setLeftImage(@DrawableRes resId: Int) {
        vb.ivLeft.visibility = View.VISIBLE
        vb.tvLeft.visibility = View.GONE
        vb.ivLeft.setImageResource(resId)
    }

    fun setRightImage(@DrawableRes resId: Int) {
        vb.ivRight.visibility = View.VISIBLE
        vb.tvRight.visibility = View.GONE
        vb.ivRight.setImageResource(resId)
    }

    fun setLeftClickListener(listener: OnClickListener) {
        leftClickListener = listener
    }

    fun setRightClickListener(listener: OnClickListener) {
        rightClickListener = listener
    }

}
