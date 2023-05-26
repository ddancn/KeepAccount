package com.ddancn.lib.base;

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.ddancn.lib.R
import com.ddancn.lib.view.HeaderView

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
abstract class BaseFragment : Fragment() {

    protected lateinit var headerView: HeaderView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootLayout = inflater.inflate(R.layout.activity_base, null) as ViewGroup
        headerView = rootLayout.findViewById(R.id.view_header)
        headerView.visibility = if (hasHeader()) View.VISIBLE else View.GONE
        headerView.setTitle(setHeaderTitle())
        inflater.inflate(bindLayout(), rootLayout, true)

        initParam()
        return rootLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        bindListener()
        applyData()
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

    protected fun <T : Any> start(clazz: Class<T>) {
        startActivity(Intent(context, clazz))
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

}
