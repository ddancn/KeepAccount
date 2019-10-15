package com.ddancn.keepaccount.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.ddancn.keepaccount.Constant;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.entity.Record;
import com.ddancn.keepaccount.entity.Type;
import com.ddancn.keepaccount.util.ToastUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {

    DatePicker datePicker;
    EditText etMoney;
    EditText etDetail;
    RadioGroup rgType;
    Spinner spinnerType;
    Button btnAdd;

    private int type = Constant.TYPE_OUT;
    private String typeName;

    private Record toUpdateRecord;
    public static final String EXTRA_ARG = "recordToUpdate";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        datePicker = findViewById(R.id.date_picker);
        etMoney = findViewById(R.id.et_money);
        etDetail = findViewById(R.id.et_detail);
        rgType = findViewById(R.id.rg_type);
        spinnerType = findViewById(R.id.spinner_type);
        btnAdd = findViewById(R.id.btn_add);

        toUpdateRecord = (Record) getIntent().getSerializableExtra(EXTRA_ARG);
        initData();

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
            String money = etMoney.getText().toString();
            if ("".equals(money)) {
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
            record.update(toUpdateRecord.getId());
            ToastUtil.show("修改成功");
            finish();
        });
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
            spinnerType.setAdapter(null);
            return;
        }
        List<String> types = new ArrayList<>();
        for (Type t : typeList) {
            types.add(t.getName());
        }
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, types);
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinnerType.setAdapter(spinnerAdapter);

    }

    /**
     * 填入原本的数据
     */
    private void initData() {
        String[] date = toUpdateRecord.getDate().split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int dayOfMonth = Integer.parseInt(date[2]);
        datePicker.updateDate(year, month - 1, dayOfMonth);

        etMoney.setText(String.valueOf(toUpdateRecord.getMoney()));
        etDetail.setText(toUpdateRecord.getDetail());

        List<Type> types = LitePal
                .where("type = ?", String.valueOf(toUpdateRecord.getType()))
                .find(Type.class);
        type = toUpdateRecord.getType();
        if (type == Constant.TYPE_IN) {
            rgType.check(R.id.rb_type_in);
        } else {
            rgType.check(R.id.rb_type_out);
        }
        setSpinnerContent();

        int selected = 0;
        int size = types.size();
        for (int i = 0; i < size; i++) {
            if (types.get(i).getName().equals(toUpdateRecord.getTypeName())) {
                selected = i;
                break;
            }
        }
        spinnerType.setSelection(selected, true);

    }
}
