package com.ddancn.keepaccount.fragment;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.activity.UpdateActivity;
import com.ddancn.keepaccount.adapter.RecordAdapter;
import com.ddancn.keepaccount.dao.RecordDao;
import com.ddancn.keepaccount.entity.Record;
import com.ddancn.lib.base.BaseFragment;
import com.ddancn.lib.util.SimpleTextWatcher;
import com.ddancn.lib.view.dialog.ConfirmDialog;
import com.ddancn.lib.view.dialog.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author ddan.zhuang
 */
public class RecordFragment extends BaseFragment {

    private EditText editText;
    private ImageView iconDate;
    private RecyclerView rvRecord;

    private RecordAdapter recordAdapter;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    private String showMonth = sdf.format(new Date());

    @Override
    protected int bindLayout() {
        return R.layout.fragment_record;
    }

    @Override
    protected boolean hasHeader() {
        return false;
    }

    @Override
    protected void initView(View root) {
        editText = root.findViewById(R.id.search);
        iconDate = root.findViewById(R.id.icon_date);
        rvRecord = root.findViewById(R.id.rv_record);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvRecord.setLayoutManager(layoutManager);
        recordAdapter = new RecordAdapter(R.layout.item_record);
        rvRecord.setAdapter(recordAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void bindListener() {
        iconDate.setOnClickListener(v ->
                DatePickerDialog.getPickerFromToday(getContext(), (datePicker, year, monthOfYear) -> {
                    showMonth = getString(R.string.date_yyyy_mm, year, monthOfYear + 1);
                    getRecords();
                })
        );
        //输入时即搜索
        editText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                if (TextUtils.isEmpty(query)) {
                    getRecords();
                } else {
                    recordAdapter.setNewData(RecordDao.searchRecord(query));
                }
            }
        });
        //搜索框清空按钮的事件
        editText.setOnTouchListener((v, event) -> {
            if (event.getX() > editText.getWidth()
                    - editText.getPaddingRight()
                    - 32) {
                editText.setText("");
                return true;
            }
            return false;
        });
        recordAdapter.setOnItemClickListener((adapter, view, position) -> {
            UpdateActivity.start(getContext(), recordAdapter.getItem(position));
        });
        recordAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            ConfirmDialog.builder(getContext())
                    .setTitle(R.string.record_delete_record)
                    .setMessage(R.string.record_delete_hint)
                    .setConfirmText(R.string.record_delete)
                    .setCancelText(R.string.cancel)
                    .setConfirmListener(() -> {
                        Record recordToDelete = recordAdapter.getItem(position);
                        if (recordToDelete != null
                                && RecordDao.deleteRecordById(recordToDelete.getId()) == 1) {
                            ToastUtils.showShort(R.string.record_delete_succeed);
                            recordAdapter.remove(position);
                        }
                        return true;
                    }).build().show();
            return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecords();
        // TODO: 2019/10/16 bug
    }

    /**
     * 从数据库中查询某月的记录
     */
    private void getRecords() {
        recordAdapter.setNewData(RecordDao.getRecordsByMonth(showMonth));
        if (recordAdapter.getData().isEmpty()) {
            ToastUtils.showShort(R.string.record_search_no_result);
        }
    }
}
