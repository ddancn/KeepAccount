package com.ddancn.keepaccount.dialog;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ddancn.keepaccount.R;

import java.util.Objects;

public class NormalDialog extends BaseDialog {

    private Button btnConfirm;
    private Button btnCancel;
    private TextView tvTitle;
    private TextView tvMsg;
    private EditText editText;
    private View vLine;
    private String txtConfirm;
    private String txtCancel;
    private String title;
    private String msg;
    private String edit;
    private OnConfirmListener confirmListener;
    private OnConfirmListenerWithInput confirmListenerWithInput;
    private OnCancelListener cancelListener;

    public NormalDialog(Context context) {
        super(context, R.layout.dialog_normal, R.style.CustomDialog);
    }

    @Override
    protected void initView() {
        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);
        tvTitle = findViewById(R.id.tv_title);
        tvMsg = findViewById(R.id.tv_msg);
        editText = findViewById(R.id.edit_text);
        vLine = findViewById(R.id.line_vertical);
        btnConfirm.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        vLine.setVisibility(View.GONE);

        if(title != null) { tvTitle.setText(title); tvTitle.setVisibility(View.VISIBLE);}
        if(msg != null) {
            tvMsg.setText(msg);
            tvMsg.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
        } else{
            Objects.requireNonNull(getWindow())
                    .setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        if(editText != null){
            editText.setText(edit);
            editText.selectAll();
        }
        if(txtConfirm != null) {btnConfirm.setText(txtConfirm); btnConfirm.setVisibility(View.VISIBLE);}
        if(txtCancel != null) {btnCancel.setText(txtCancel); btnCancel.setVisibility(View.VISIBLE);}
        if(txtConfirm != null && txtCancel !=null) {vLine.setVisibility(View.VISIBLE);}

        btnConfirm.setOnClickListener(v->{
            if (confirmListenerWithInput != null){
                if(confirmListenerWithInput.onClick(editText.getText().toString())) {
                    dismiss();
                }
            } else if(confirmListener != null){
                if(confirmListener.onClick()) {
                    dismiss();
                }
            }
        });
        btnCancel.setOnClickListener(v -> {
            if (cancelListener != null ) {
                cancelListener.onClick();
            }
            dismiss();
        });
    }

    public NormalDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public NormalDialog setMsg(String msg){
        this.msg = msg;
        return this;
    }

    public NormalDialog setEditText(String edit){
        this.edit = edit;
        return this;
    }

    public NormalDialog setOnConfirmClickListener(String txtConfirm, OnConfirmListener onConfirmClickListener){
        if(txtConfirm != null) {
            this.txtConfirm = txtConfirm;
        }
        this.confirmListener = onConfirmClickListener;
        return this;
    }

    public NormalDialog setOnConfirmClickListenerWithInput(String txtConfirm, OnConfirmListenerWithInput onConfirmClickListenerWithInput){
        if(txtConfirm != null) {
            this.txtConfirm = txtConfirm;
        }
        this.confirmListenerWithInput = onConfirmClickListenerWithInput;
        return this;
    }

    public NormalDialog setOnCancelClickListener(String txtCancel, OnCancelListener onCancelClickListener){
        if(txtCancel != null) {
            this.txtCancel = txtCancel;
        }
        this.cancelListener = onCancelClickListener;
        return this;
    }

    public interface OnConfirmListenerWithInput {
        boolean onClick(String input);
    }

    public interface OnConfirmListener {
        boolean onClick();
    }

    public interface OnCancelListener {
        boolean onClick();
    }

}