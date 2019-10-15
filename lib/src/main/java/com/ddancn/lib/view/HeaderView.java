package com.ddancn.lib.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;

import com.ddancn.lib.R;

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
public class HeaderView extends LinearLayout {

    private TextView tvTitle;
    private TextView tvLeft;
    private TextView tvRight;
    private ImageView ivLeft;
    private ImageView ivRight;

    private OnClickListener leftClickListener;
    private OnClickListener rightClickListener;

    public HeaderView(Context context) {
        super(context);
        init(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View root = View.inflate(context, R.layout.view_header, this);

        tvTitle = root.findViewById(R.id.tv_title);
        tvLeft = root.findViewById(R.id.tv_left);
        tvRight = root.findViewById(R.id.tv_right);
        ivLeft = root.findViewById(R.id.iv_left);
        ivRight = root.findViewById(R.id.iv_right);

        ivLeft.setOnClickListener(v -> {
            if (leftClickListener != null) {
                leftClickListener.onClick(v);
            }
        });
        tvLeft.setOnClickListener(v -> {
            if (leftClickListener != null) {
                leftClickListener.onClick(v);
            }
        });
        ivRight.setOnClickListener(v -> {
            if (rightClickListener != null) {
                rightClickListener.onClick(v);
            }
        });
        tvRight.setOnClickListener(v -> {
            if (rightClickListener != null) {
                rightClickListener.onClick(v);
            }
        });
    }

    public void setLeftClickListener(OnClickListener leftClickListener) {
        this.leftClickListener = leftClickListener;
    }

    public void setRightClickListener(OnClickListener rightClickListener) {
        this.rightClickListener = rightClickListener;
    }

    public void setTitle(String text){
        tvTitle.setText(text);
    }

    public void setTitle(@StringRes int resId){
        tvTitle.setText(resId);
    }

    public void setLeftText(String text){
        tvLeft.setVisibility(VISIBLE);
        ivLeft.setVisibility(GONE);
        tvLeft.setText(text);
    }

    public void setRightText(String text){
        tvRight.setVisibility(VISIBLE);
        ivRight.setVisibility(GONE);
        tvRight.setText(text);
    }

    public void setLeftImage(@DrawableRes int resId){
        ivLeft.setVisibility(VISIBLE);
        tvLeft.setVisibility(GONE);
        ivLeft.setImageResource(resId);
    }

    public void setRightImage(@DrawableRes int resId){
        ivRight.setVisibility(VISIBLE);
        tvRight.setVisibility(GONE);
        ivRight.setImageResource(resId);
    }

}
