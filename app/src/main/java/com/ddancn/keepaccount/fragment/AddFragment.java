package com.ddancn.keepaccount.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.ddancn.keepaccount.activity.MainActivity;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.activity.SettingActivity;
import com.ddancn.keepaccount.util.ToastUtil;
import com.ddancn.keepaccount.entity.Record;
import com.ddancn.keepaccount.entity.Type;
import com.joanzapata.iconify.widget.IconTextView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddFragment extends Fragment {

    DatePicker datePicker;
    EditText etMoney;
    EditText etDetail;
    RadioGroup rgType;
    Spinner spinnerType;
    Button btnAdd;
    IconTextView btnSetting;

    private int type = MainActivity.TYPE_OUT;
    private String typeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add, container, false);
        datePicker = root.findViewById(R.id.date_picker);
        etMoney = root.findViewById(R.id.et_money);
        etDetail = root.findViewById(R.id.et_detail);
        rgType = root.findViewById(R.id.rg_type);
        spinnerType = root.findViewById(R.id.spinner_type);
        btnAdd = root.findViewById(R.id.btn_add);
        btnSetting = root.findViewById(R.id.btn_setting);

        // 获取radio group收支选项，联动改变类型spinner的内容
        rgType.setOnCheckedChangeListener((group, checkId) -> {
            if (checkId == R.id.rbtn_type_out)
                type = MainActivity.TYPE_OUT;
            else if (checkId == R.id.rbtn_type_in)
                type = MainActivity.TYPE_IN;
            setSpinnerContent();
        });
        // 默认设置radio group为支出
        setSpinnerContent();

        btnSetting.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        });

        // 按钮点击事件，判断是否合法并且存下记录
        btnAdd.setOnClickListener(v -> {
            String money = etMoney.getText().toString();
            if (money.equals("")) {
                ToastUtil.show("请输入钱数");
                return;
            }
            typeName = (String) spinnerType.getSelectedItem();
            if (typeName == null) {
                ToastUtil.show("请选择类型");
                return;
            }
            int y = datePicker.getYear();
            int m = datePicker.getMonth() + 1;
            int d = datePicker.getDayOfMonth();
            String date = y + "-"
                    + ((m < 10) ? "0" : "") + m + "-"
                    + ((d < 10) ? "0" : "") + d;

            Record record = new Record();
            record.setDate(date);
            record.setMoney(Double.parseDouble(money));
            record.setDetail(etDetail.getText().toString());
            record.setType(type);
            record.setTypeName(typeName);
            if (record.save()) {
                ToastUtil.show("添加成功");
                reset();
            } else
                ToastUtil.show("添加失败");
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setSpinnerContent();
    }

    /**
     * 根据收支的选择填充spinner的内容
     */
    protected void setSpinnerContent() {
        List<Type> typeList = LitePal
                .select("name")
                .where("type = ?", String.valueOf(type))
                .find(Type.class);
        if (typeList == null || typeList.size() == 0) {
            ToastUtil.show("先在设置里创建类型~");
            return ;
        }
        List<String> types = new ArrayList<>();
        for (Type t : typeList) {
            types.add(t.getName());
        }
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, types);
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinnerType.setAdapter(spinnerAdapter);

    }

    /**
     * 重置清空
     */
    private void reset(){
        Calendar c = Calendar.getInstance();
        datePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
        etMoney.setText("");
        etDetail.setText("");
        type = MainActivity.TYPE_OUT;
        rgType.check(R.id.rbtn_type_out);
        setSpinnerContent();
    }

}
