package com.ddancn.lib.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.ddancn.lib.R;
import com.ddancn.lib.view.HeaderView;

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
public abstract class BaseFragment extends Fragment{

    private HeaderView headerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootLayout = (ViewGroup) inflater.inflate(R.layout.activity_base, null);

        headerView = rootLayout.findViewById(R.id.view_header);
        headerView.setVisibility(hasHeader() ? View.VISIBLE : View.GONE);
        headerView.setTitle(setHeaderTitle());

        inflater.inflate(bindLayout(), rootLayout, true);

        initParam();
        initView(rootLayout);
        bindListener();
        applyData();

        return rootLayout;
    }

    protected boolean hasHeader() {
        return true;
    }

    protected void toast(String msg) {
        ToastUtils.showShort(msg);
    }

    protected void toast(int resId) {
        ToastUtils.showShort(resId);
    }

    /**
     * 绑定资源文件
     *
     * @return layoutResId
     */
    protected abstract @LayoutRes int bindLayout();

    /**
     * 初始化参数
     */
    protected void initParam() {
    }

    /**
     * 初始化控件
     */
    protected abstract void initView(View root);

    /**
     * 绑定监听事件
     */
    protected void bindListener() {
    }

    /**
     * 展示数据
     */
    protected void applyData() {
    }

    /**
     * 设置标题
     *
     * @return 标题
     */
    protected String setHeaderTitle() {
        return "";
    }

    protected void setLeftText(String text) {
        headerView.setLeftText(text);
    }

    protected void setLeftText(@StringRes int resId) {
        headerView.setLeftText(getString(resId));
    }

    protected void setRightText(String text) {
        headerView.setRightText(text);
    }

    protected void setRightText(@StringRes int resId) {
        headerView.setRightText(getString(resId));
    }

    protected void setLeftImage(@DrawableRes int resId) {
        headerView.setLeftImage(resId);
    }

    protected void setRightImage(@DrawableRes int resId) {
        headerView.setRightImage(resId);
    }

    protected void setLeftClickListener(View.OnClickListener listener) {
        headerView.setLeftClickListener(listener);
    }

    protected void setRightClickListener(View.OnClickListener listener) {
        headerView.setRightClickListener(listener);
    }
}
