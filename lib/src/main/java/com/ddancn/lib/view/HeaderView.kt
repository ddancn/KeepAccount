package com.ddancn.lib.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ddancn.lib.R
import kotlinx.android.synthetic.main.view_header.view.*

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
class HeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private var leftClickListener: OnClickListener? = null
    private var rightClickListener: OnClickListener? = null

    init {
        View.inflate(context, R.layout.view_header, this)
        iv_left.setOnClickListener { leftClickListener?.onClick(it) }
        tv_left.setOnClickListener { leftClickListener?.onClick(it) }
        iv_right.setOnClickListener { rightClickListener?.onClick(it) }
        tv_right.setOnClickListener { rightClickListener?.onClick(it) }
    }

    fun setTitle(text: String) {
        tv_title.text = text
    }

    fun setTitle(@StringRes resId: Int) {
        setTitle(resId)
    }

    fun setLeftText(text: String) {
        tv_left.visibility = View.VISIBLE
        iv_left.visibility = View.GONE
        tv_left.text = text
    }

    fun setLeftText(@StringRes resId: Int) {
        setLeftText(context.getString(resId))
    }

    fun setRightText(text: String) {
        tv_right.visibility = View.VISIBLE
        iv_right.visibility = View.GONE
        tv_right.text = text
    }

    fun setRightText(@StringRes resId: Int) {
        setRightText(context.getString(resId))
    }

    fun setLeftImage(@DrawableRes resId: Int) {
        iv_left.visibility = View.VISIBLE
        tv_left.visibility = View.GONE
        iv_left.setImageResource(resId)
    }

    fun setRightImage(@DrawableRes resId: Int) {
        iv_right.visibility = View.VISIBLE
        tv_right.visibility = View.GONE
        iv_right.setImageResource(resId)
    }

    fun setLeftClickListener(listener: OnClickListener){
        leftClickListener = listener
    }

    fun setRightClickListener(listener: OnClickListener){
        rightClickListener = listener
    }

}
