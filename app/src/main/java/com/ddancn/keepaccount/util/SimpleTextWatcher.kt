package com.ddancn.keepaccount.util

import android.text.Editable
import android.text.TextWatcher

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
open class SimpleTextWatcher : TextWatcher {
    override fun afterTextChanged(s: Editable?) {//
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {//
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {//
    }

}
