package com.ddancn.keepaccount.activity

import android.content.Context
import android.content.Intent
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.entity.Record
import com.ddancn.lib.base.BaseActivity

/**
 * @author ddan.zhuang
 * 修改页面
 */
const val EXTRA_ARG = "recordToUpdate"

class UpdateActivity : BaseActivity() {

    var recordToUpdate: Record? = null

    override fun bindLayout(): Int = R.layout.activity_update

    override fun setHeaderTitle(): String {
        return getString(com.ddancn.keepaccount.R.string.update_title)
    }

    override fun initParam() {
        recordToUpdate = intent.getSerializableExtra(EXTRA_ARG) as Record?
    }

    override fun initView() {
        enableLeftBack()
    }

    companion object {
        fun start(context: Context, recordToUpdate: Record?) {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra(EXTRA_ARG, recordToUpdate)
            context.startActivity(intent)
        }
    }
}
