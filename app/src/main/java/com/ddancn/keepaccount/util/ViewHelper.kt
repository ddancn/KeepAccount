package com.ddancn.keepaccount.util

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.Utils
import com.ddancn.lib.R

/**
 * @author ddan.zhuang
 */

val emptyText: String = Utils.getApp().getString(R.string.empty_text)
val footerText: String = Utils.getApp().getString(R.string.footer_text)

@JvmOverloads
fun getEmptyTextView(text: String = emptyText): View {
    val view = LayoutInflater.from(Utils.getApp()).inflate(R.layout.view_empty_text, null, false)
    val textView = view.findViewById<TextView>(R.id.text)
    textView.text = text
    return view
}

@JvmOverloads
fun getFooterTextView(text: String = footerText): View {
    val view = LayoutInflater.from(Utils.getApp()).inflate(R.layout.view_bottom, null, false)
    val textView = view.findViewById<TextView>(R.id.text)
    textView.text = text
    return view
}

