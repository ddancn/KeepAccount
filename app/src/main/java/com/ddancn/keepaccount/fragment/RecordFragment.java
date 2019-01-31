package com.ddancn.keepaccount.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.activity.UpdateActivity;
import com.ddancn.keepaccount.util.ToastUtil;
import com.ddancn.keepaccount.adapter.RecordAdapter;
import com.ddancn.keepaccount.dialog.DatePickerDialog;
import com.ddancn.keepaccount.dialog.NormalDialog;
import com.ddancn.keepaccount.entity.Record;
import com.joanzapata.iconify.widget.IconTextView;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecordFragment extends Fragment
        implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {

    EditText editText;
    IconTextView iconDate;
    RecyclerView recyclerView;

    private List<Record> recordList = new ArrayList<>();
    private RecordAdapter recordAdapter;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    private String showMonth = sdf.format(new Date());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_record, container, false);
        editText = root.findViewById(R.id.search);
        iconDate = root.findViewById(R.id.icon_date);
        recyclerView = root.findViewById(R.id.rv_record);

        iconDate.setOnClickListener(v -> showDatePickDialog());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                if(TextUtils.isEmpty(query)){
                    recordList.clear();
                    showMonth = sdf.format(new Date());
                    getRecords();
                } else {
                    String condition = "%" + query + "%";
                    recordList.clear();
                    recordList.addAll(LitePal
                            .where("date like ? or money like ? " +
                                            "or detail like ? or typeName like ?",
                                    condition, condition, condition, condition)
                            .order("date desc")
                            .find(Record.class));
                }
                recordAdapter.notifyDataSetChanged();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recordList = LitePal
                .where("date like ?", showMonth + "%")
                .order("date desc")
                .find(Record.class);
        recordAdapter = new RecordAdapter(R.layout.item_record, recordList);
        recordAdapter.setOnItemClickListener(this);
        recordAdapter.setOnItemLongClickListener(this);
        recyclerView.setAdapter(recordAdapter);

        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getRecords();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecords();
    }

    /**
     * 从数据库中查询某月的记录
     */
    private void getRecords() {
        recordList.clear();
        recordList.addAll(LitePal
                .where("date like ?", showMonth + "%")
                .order("date desc")
                .find(Record.class));
        if (recordList.isEmpty())
            ToastUtil.show("查不到记录鸭");
        recordAdapter.notifyDataSetChanged();
    }

    /**
     * 只有年月的日期选择框
     */
    private void showDatePickDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    showMonth = year + "-" + ((monthOfYear < 9) ? "0" : "") + (monthOfYear + 1);
                    getRecords();
                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getContext(), UpdateActivity.class);
        intent.putExtra(UpdateActivity.EXTRA_ARG, recordList.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        NormalDialog dialog = new NormalDialog(getContext());
        dialog.setTitle("删除记录")
                .setMsg("真的要删除这条记录吗？")
                .setOnConfirmClickListener("删除", () -> {
                    LitePal.delete(Record.class, recordList.get(position).getId());
                    ToastUtil.show("删除成功");
                    recordAdapter.remove(position);
                    return true;
                })
                .setOnCancelClickListener("取消", null);
        dialog.show();
        return false;
    }
}
