package com.ddancn.lib.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.ddancn.lib.R;

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
public class ConfirmDialog extends BaseDialog {

    private String confirmText;
    private String cancelText;
    private String title;
    private String message;
    private OnBtnClickListener confirmListener;
    private OnBtnClickListener cancelListener;

    public ConfirmDialog(Context context) {
        super(context, R.layout.dialog_confirm, R.style.CustomDialog);
    }

    @Override
    protected void initView() {
        Button btnConfirm = findViewById(R.id.btn_confirm);
        Button btnCancel = findViewById(R.id.btn_cancel);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvMsg = findViewById(R.id.tv_msg);
        View vDivider = findViewById(R.id.line_vertical);

        btnCancel.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        tvMsg.setVisibility(View.GONE);
        vDivider.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(message)) {
            tvMsg.setText(message);
            tvMsg.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(confirmText)) {
            btnConfirm.setText(confirmText);
            btnConfirm.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(cancelText)) {
            btnCancel.setText(cancelText);
            btnCancel.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(confirmText) && !TextUtils.isEmpty(cancelText)) {
            vDivider.setVisibility(View.VISIBLE);
        }
        btnConfirm.setOnClickListener(v -> {
            if (confirmListener != null && confirmListener.onClick()) {
                dismiss();
            }
        });
        btnCancel.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onClick();
            }
            dismiss();
        });
    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static final class Builder {
        private Context context;
        private String confirmText;
        private String cancelText;
        private String title;
        private String msg;
        private boolean cancelable = true;
        private OnBtnClickListener confirmListener;
        private OnBtnClickListener cancelListener;

        private Builder(Context context) {
            this.context = context;
        }

        public Builder setContext(Context mContext) {
            this.context = mContext;
            return this;
        }

        public Builder setConfirmText(String confirmText) {
            this.confirmText = confirmText;
            return this;
        }

        public Builder setCancelText(String cancelText) {
            this.cancelText = cancelText;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder setConfirmText(@StringRes int resId) {
            this.confirmText = context.getString(resId);
            return this;
        }

        public Builder setCancelText(@StringRes int resId) {
            this.cancelText = context.getString(resId);
            return this;
        }

        public Builder setTitle(@StringRes int resId) {
            this.title = context.getString(resId);
            return this;
        }

        public Builder setMessage(@StringRes int resId) {
            this.msg = context.getString(resId);
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setConfirmListener(OnBtnClickListener confirmListener) {
            this.confirmListener = confirmListener;
            return this;
        }

        public Builder setCancelListener(OnBtnClickListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public ConfirmDialog build() {
            ConfirmDialog confirmDialog = new ConfirmDialog(context);
            confirmDialog.title = this.title;
            confirmDialog.message = this.msg;
            confirmDialog.cancelListener = this.cancelListener;
            confirmDialog.confirmListener = this.confirmListener;
            confirmDialog.confirmText = this.confirmText;
            confirmDialog.cancelText = this.cancelText;
            confirmDialog.setCancelable(cancelable);
            return confirmDialog;
        }
    }
}