package com.ddancn.lib.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.ddancn.lib.R;
import com.ddancn.lib.view.HeaderView;
import com.jaeger.library.StatusBarUtil;

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
public abstract class BaseActivity extends AppCompatActivity{

    private HeaderView headerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        generateContentView();

        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColor(this, Color.WHITE, 0);

        initParam();
        initView();
        bindListener();
        applyData();

    }

    private void generateContentView() {
        ViewGroup rootLayout = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_base, null);

        headerView = rootLayout.findViewById(R.id.view_header);
        headerView.setVisibility(hasHeader() ? View.VISIBLE : View.GONE);
        headerView.setTitle(setHeaderTitle());

        View contentView = LayoutInflater.from(this).inflate(bindLayout(), rootLayout, true);

        setContentView(contentView);
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

    protected void start(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }

    protected void startForResult(Class<?> clazz, int requestCode) {
        startActivityForResult(new Intent(this, clazz), requestCode);
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
    protected abstract void initView();

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

    protected void enableLeftBack() {
        setLeftImage(R.drawable.ic_left_arrow);
        headerView.setLeftClickListener(v -> finish());
    }

    protected void setTitleText(String title) {
        headerView.setTitle(title);
    }
}
