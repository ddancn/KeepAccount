package com.ddancn.keepaccount.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.activity.SettingActivity;
import com.ddancn.keepaccount.activity.UpdateActivity;
import com.ddancn.keepaccount.constant.TypeEnum;
import com.ddancn.keepaccount.dao.RecordDao;
import com.ddancn.keepaccount.dao.TypeDao;
import com.ddancn.keepaccount.entity.Record;
import com.ddancn.keepaccount.entity.Type;
import com.ddancn.lib.base.BaseFragment;
import com.ddancn.lib.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author ddan.zhuang
 * 添加或修改记录
 */
public class AddFragment extends BaseFragment {

    private DatePicker datePicker;
    private EditText etMoney;
    private EditText etDetail;
    private RadioGroup rgType;
    private Spinner spinnerType;
    private Button btnAdd;

    private int type = TypeEnum.OUT.value();
    private boolean isUpdate = false;
    private Record recordToUpdate = new Record();

    @Override
    protected int bindLayout() {
        return R.layout.fragment_add;
    }

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected boolean hasHeader() {
        return !isUpdate;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UpdateActivity) {
            isUpdate = true;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof UpdateActivity) {
            recordToUpdate = ((UpdateActivity) getActivity()).getRecordToUpdate();
        }
    }

    @Override
    protected void initView(View root) {
        setRightImage(R.drawable.ic_setting);
        datePicker = root.findViewById(R.id.date_picker);
        etMoney = root.findViewById(R.id.et_money);
        etDetail = root.findViewById(R.id.et_detail);
        rgType = root.findViewById(R.id.rg_type);
        spinnerType = root.findViewById(R.id.spinner_type);
        btnAdd = root.findViewById(R.id.btn_add);
    }

    @Override
    protected void bindListener() {
        setRightClickListener(v -> startActivity(new Intent(getContext(), SettingActivity.class)));
        // 获取radio group收支选项，联动改变类型spinner的内容
        rgType.setOnCheckedChangeListener((group, checkId) -> {
            type = checkId == R.id.rb_type_out ? TypeEnum.OUT.value() : TypeEnum.IN.value();
            setSpinnerContent();
        });

        // 按钮点击事件
        btnAdd.setOnClickListener(v -> {
            if (!checkParamLegal()) {
                return;
            }
            if (saveOrUpdateRecord()) {
                ToastUtils.showShort(isUpdate ? R.string.update_succeed : R.string.add_succeed);
                if (isUpdate && getActivity() != null) {
                    getActivity().finish();
                } else {
                    reset();
                }
            } else {
                ToastUtils.showShort(isUpdate ? R.string.update_fail : R.string.add_fail);
            }
        });
    }

    /**
     * 判断是否合法
     *
     * @return 是否合法
     */
    private boolean checkParamLegal() {
        if (TextUtils.isEmpty(etMoney.getText().toString())) {
            ToastUtils.showShort(R.string.add_need_money);
            return false;
        }
        if (TextUtils.isEmpty((String) spinnerType.getSelectedItem())) {
            ToastUtils.showShort(R.string.add_need_type);
            return false;
        }
        return true;
    }

    /**
     * 修改页面，先填上原来的数据
     */
    private void initData() {
        btnAdd.setText(getString(R.string.update_btn));
        String[] date = recordToUpdate.getDate().split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int dayOfMonth = Integer.parseInt(date[2]);
        datePicker.updateDate(year, month - 1, dayOfMonth);

        etMoney.setText(String.valueOf(recordToUpdate.getMoney()));
        etDetail.setText(recordToUpdate.getDetail());

        List<Type> types = TypeDao.getTypesByType(recordToUpdate.getType());
        type = recordToUpdate.getType();
        rgType.check(type == TypeEnum.IN.value() ? R.id.rb_type_in : R.id.rb_type_out);
        setSpinnerContent();

        int selected = 0;
        int size = types.size();
        for (int i = 0; i < size; i++) {
            if (types.get(i).getName().equals(recordToUpdate.getTypeName())) {
                selected = i;
                break;
            }
        }
        spinnerType.setSelection(selected, true);

    }

    @Override
    public void onResume() {
        super.onResume();
        setSpinnerContent();
        if (isUpdate) {
            initData();
        }
    }

    private boolean saveOrUpdateRecord() {
        int y = datePicker.getYear();
        int m = datePicker.getMonth() + 1;
        int d = datePicker.getDayOfMonth();
        String date = DateUtil.getFormatYMD(y, m, d);
        return RecordDao.addOrUpdateRecord(isUpdate,
                recordToUpdate.getId(),
                date,
                Double.parseDouble(etMoney.getText().toString()),
                etDetail.getText().toString(),
                type,
                (String) spinnerType.getSelectedItem());
    }

    /**
     * 根据收支的选择填充spinner的内容
     */
    private void setSpinnerContent() {
        List<Type> types = TypeDao.getTypesByType(type);
        if (types == null || types.size() == 0) {
            ToastUtils.showShort(R.string.add_no_type);
            return;
        }
        List<String> typeNames = new ArrayList<>();
        for (Type t : types) {
            typeNames.add(t.getName());
        }
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, typeNames);
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinnerType.setAdapter(spinnerAdapter);
    }

    /**
     * 重置清空
     */
    private void reset() {
        Calendar c = Calendar.getInstance();
        datePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
        etMoney.setText("");
        etDetail.setText("");
        type = TypeEnum.OUT.value();
        rgType.check(R.id.rb_type_out);
        setSpinnerContent();
    }

}
