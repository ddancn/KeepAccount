package com.ddancn.keepaccount.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.blankj.utilcode.util.ToastUtils;
import com.ddancn.keepaccount.Constant;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.activity.SettingActivity;
import com.ddancn.keepaccount.dao.RecordDao;
import com.ddancn.keepaccount.dao.TypeDao;
import com.ddancn.keepaccount.entity.Type;
import com.ddancn.lib.base.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author ddan.zhuang
 */
public class AddFragment extends BaseFragment {

    private DatePicker datePicker;
    private EditText etMoney;
    private EditText etDetail;
    private RadioGroup rgType;
    private Spinner spinnerType;
    private Button btnAdd;

    private int type = Constant.TYPE_OUT;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_add;
    }

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.app_name);
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
            if (checkId == R.id.rb_type_out) {
                type = Constant.TYPE_OUT;
            } else if (checkId == R.id.rb_type_in) {
                type = Constant.TYPE_IN;
            }
            setSpinnerContent();
        });

        // 按钮点击事件，判断是否合法并且存下记录
        btnAdd.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etMoney.getText().toString())) {
                ToastUtils.showShort(R.string.add_need_money);
                return;
            }
            if (TextUtils.isEmpty((String) spinnerType.getSelectedItem())) {
                ToastUtils.showShort(R.string.add_need_type);
                return;
            }
            if (saveRecord()) {
                ToastUtils.showShort(R.string.add_succeed);
                reset();
            } else {
                ToastUtils.showShort(R.string.add_fail);
            }
        });
    }

    @Override
    protected void applyData() {
        // 默认设置radio group为支出
        setSpinnerContent();
    }

    @Override
    public void onResume() {
        super.onResume();
        setSpinnerContent();
    }

    /**
     * 保存记录
     *
     * @return 是否保存成功
     */
    private boolean saveRecord() {
        int y = datePicker.getYear();
        int m = datePicker.getMonth() + 1;
        int d = datePicker.getDayOfMonth();
        String date = String.format(Locale.CHINA, "%d-%2d-%2d", y, m, d);
        return RecordDao.addRecord(
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
        List<Type> typeList = TypeDao.getTypesByType(type);
        if (typeList == null || typeList.size() == 0) {
            ToastUtils.showShort(R.string.add_no_type);
            return;
        }
        List<String> typeNames = new ArrayList<>();
        for (Type t : typeList) {
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
        type = Constant.TYPE_OUT;
        rgType.check(R.id.rb_type_out);
        setSpinnerContent();
    }

}
