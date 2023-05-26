package com.ddancn.lib.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.ddancn.lib.R
import com.ddancn.lib.view.HeaderView
import com.jaeger.library.StatusBarUtil

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var headerView: HeaderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        generateContentView()

        StatusBarUtil.setLightMode(this)
        StatusBarUtil.setColor(this, Color.WHITE, 0)

        initParam()
        initView()
        bindListener()
        applyData()
    }

    private fun generateContentView() {
        val rootLayout = LayoutInflater.from(this).inflate(R.layout.activity_base, null) as ViewGroup

        headerView = rootLayout.findViewById(R.id.view_header)
        headerView.visibility = if (hasHeader()) View.VISIBLE else View.GONE
        headerView.setTitle(setHeaderTitle())

        val contentView = LayoutInflater.from(this).inflate(bindLayout(), rootLayout, true)
        setContentView(contentView)
    }

    protected open fun hasHeader(): Boolean {
        return true
    }

    protected fun toast(msg: String?) {
        ToastUtils.showShort(msg)
    }

    protected fun toast(@StringRes resId: Int) {
        ToastUtils.showShort(resId)
    }

    /**
     * 绑定资源文件
     *
     * @return layoutResId
     */
    @LayoutRes
    protected abstract fun bindLayout(): Int

    /**
     * 初始化参数
     */
    protected open fun initParam() {//
    }

    /**
     * 初始化控件
     */
    protected abstract fun initView()

    /**
     * 绑定监听事件
     */
    protected open fun bindListener() {//
    }

    /**
     * 展示数据
     */
    protected open fun applyData() {//
    }

    /**
     * 设置标题
     *
     * @return 标题
     */
    protected open fun setHeaderTitle(): String {
        return ""
    }

    protected fun enableLeftBack() {
        headerView.setLeftImage(R.drawable.ic_left_arrow)
        headerView.setLeftClickListener { finish() }
    }

}
