package com.ddancn.keepaccount.activity

import android.content.Context
import android.content.Intent
import com.ddancn.keepaccount.R
import com.ddancn.keepaccount.databinding.ActivityUpdateBinding
import com.ddancn.keepaccount.entity.Record
import com.ddancn.lib.base.BaseActivity

/**
 * @author ddan.zhuang
 * 修改页面
 */
const val EXTRA_ARG = "recordToUpdate"

class UpdateActivity : BaseActivity<ActivityUpdateBinding>() {

    var recordToUpdate: Record? = null

    override fun initParam() {
        recordToUpdate = intent.getSerializableExtra(EXTRA_ARG) as Record?
    }

    override fun initView() {
        headerView.setTitle(R.string.update_title)
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
