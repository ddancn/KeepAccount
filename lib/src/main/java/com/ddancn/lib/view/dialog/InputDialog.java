package com.ddancn.lib.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.blankj.utilcode.util.KeyboardUtils;
import com.ddancn.lib.R;

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
public class InputDialog extends BaseDialog {

    private String confirmText;
    private String cancelText;
    private String title;
    private String etMsg;
    private OnConfirmListenerWithInput confirmListener;
    private OnBtnClickListener cancelListener;

    public InputDialog(Context context) {
        super(context, R.layout.dialog_input, R.style.CustomDialog);
    }

    @Override
    protected void initView() {
        Button btnConfirm = findViewById(R.id.btn_confirm);
        Button btnCancel = findViewById(R.id.btn_cancel);
        TextView tvTitle = findViewById(R.id.tv_title);
        EditText editText = findViewById(R.id.edit_text);
        View vLine = findViewById(R.id.line_vertical);

        btnCancel.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        vLine.setVisibility(View.GONE);
        editText.requestFocus();

        KeyboardUtils.toggleSoftInput();

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(etMsg)) {
            editText.setText(etMsg);
            editText.selectAll();
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
            vLine.setVisibility(View.VISIBLE);
        }

        btnConfirm.setOnClickListener(v -> {
            if (confirmListener != null
                    && confirmListener.onClick(editText.getText().toString())) {
                cancel();
            }
        });
        btnCancel.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onClick();
            }
            cancel();
        });
        setOnCancelListener(dialog -> KeyboardUtils.toggleSoftInput());
    }

    /**
     * 返回输入的确定点击事件
     */
    public interface OnConfirmListenerWithInput {
        /**
         * 点击确定
         *
         * @param input 输入内容
         * @return 是否dismiss
         */
        boolean onClick(String input);
    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static final class Builder {
        private Context context;
        private String confirmText;
        private String cancelText;
        private String title;
        private String etMsg;
        private OnConfirmListenerWithInput confirmListener;
        private OnBtnClickListener cancelListener;

        private Builder(Context context) {
            this.context = context;
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

        public Builder setEtMsg(String etMsg) {
            this.etMsg = etMsg;
            return this;
        }

        public Builder setTitle(@StringRes int resId) {
            this.title = context.getString(resId);
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

        public Builder setEtMsg(@StringRes int resId) {
            this.etMsg = context.getString(resId);
            return this;
        }

        public Builder setConfirmListener(OnConfirmListenerWithInput confirmListener) {
            this.confirmListener = confirmListener;
            return this;
        }

        public Builder setCancelListener(OnBtnClickListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public InputDialog build() {
            InputDialog inputDialog = new InputDialog(context);
            inputDialog.title = this.title;
            inputDialog.confirmListener = this.confirmListener;
            inputDialog.cancelText = this.cancelText;
            inputDialog.cancelListener = this.cancelListener;
            inputDialog.confirmText = this.confirmText;
            inputDialog.etMsg = this.etMsg;
            return inputDialog;
        }
    }
}
