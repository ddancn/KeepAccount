package com.ddancn.keepaccount.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ddancn.keepaccount.Constant;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.adapter.TypeAdapter;
import com.ddancn.keepaccount.dao.RecordDao;
import com.ddancn.keepaccount.dao.TypeDao;
import com.ddancn.keepaccount.entity.Type;
import com.ddancn.lib.base.BaseActivity;
import com.ddancn.lib.view.dialog.ConfirmDialog;
import com.ddancn.lib.view.dialog.InputDialog;

/**
 * @author ddan.zhuang
 */
public class SettingActivity extends BaseActivity {

    private ImageView iconAddIn;
    private ImageView iconAddOut;
    private RecyclerView rvTypeIn;
    private RecyclerView rvTypeOut;

    private TypeAdapter inTypesAdapter;
    private TypeAdapter outTypesAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.setting_title);
    }

    @Override
    protected void initView() {
        enableLeftBack();
        iconAddIn = findViewById(R.id.icon_add_in);
        iconAddOut = findViewById(R.id.icon_add_out);
        rvTypeIn = findViewById(R.id.rv_type_in);
        rvTypeOut = findViewById(R.id.rv_type_out);

        GridLayoutManager manager1 = new GridLayoutManager(this, 3);
        GridLayoutManager manager2 = new GridLayoutManager(this, 3);
        rvTypeIn.setLayoutManager(manager1);
        rvTypeOut.setLayoutManager(manager2);
        inTypesAdapter = new TypeAdapter(R.layout.item_type);
        rvTypeIn.setAdapter(inTypesAdapter);
        outTypesAdapter = new TypeAdapter(R.layout.item_type);
        rvTypeOut.setAdapter(outTypesAdapter);
    }

    @Override
    protected void bindListener() {
        iconAddIn.setOnClickListener(v -> addType(Constant.TYPE_IN));
        iconAddOut.setOnClickListener(v -> addType(Constant.TYPE_OUT));
        inTypesAdapter.setOnItemClickListener(new TypeItemClickItemListener());
        inTypesAdapter.setOnItemLongClickListener(new TypeItemLongClickListener());
        outTypesAdapter.setOnItemClickListener(new TypeItemClickItemListener());
        outTypesAdapter.setOnItemLongClickListener(new TypeItemLongClickListener());
    }

    @Override
    protected void applyData() {
        inTypesAdapter.setNewData(TypeDao.getTypesByType(Constant.TYPE_IN));
        outTypesAdapter.setNewData(TypeDao.getTypesByType(Constant.TYPE_OUT));
    }

    private TypeAdapter getAdapter(int type) {
        return (type == Constant.TYPE_IN) ? inTypesAdapter : outTypesAdapter;
    }

    private void addType(int type) {
        InputDialog.builder(this)
                .setTitle(R.string.setting_add_type)
                .setConfirmText(R.string.confirm)
                .setCancelText(R.string.cancel)
                .setConfirmListener(input -> {
                    if (TextUtils.isEmpty(input)) {
                        ToastUtils.showShort(R.string.setting_need_type_name);
                        return false;
                    }
                    if (TypeDao.addType(input, type)) {
                        ToastUtils.showShort(R.string.setting_add_succeed);
                        applyData();
                    } else {
                        ToastUtils.showShort(R.string.setting_add_fail);
                    }
                    return true;
                }).build().show();
    }

    class TypeItemClickItemListener implements BaseQuickAdapter.OnItemClickListener {

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Type typeToUpdate = (Type) (adapter.getData().get(position));
            int type = typeToUpdate.getType();

            InputDialog.builder(SettingActivity.this)
                    .setTitle(R.string.setting_update_type)
                    .setConfirmText(R.string.setting_update)
                    .setCancelText(R.string.cancel)
                    .setEtMsg(typeToUpdate.getName())
                    .setConfirmListener(input -> {
                        if (TextUtils.isEmpty(input)) {
                            ToastUtils.showShort(R.string.setting_need_type_name);
                            return false;
                        }
                        //修改类型，更新类型相关的记录
                        if (TypeDao.updateTypeName(typeToUpdate.getId(), input) == 1
                                && RecordDao.updateRecordTypeName(input, typeToUpdate.getName()) >= 0) {
                            ToastUtils.showShort(R.string.setting_update_succeed);
                            getAdapter(type).getData().get(position).setName(input);
                            getAdapter(type).notifyItemChanged(position);
                        }
                        return true;
                    }).build().show();
        }
    }

    class TypeItemLongClickListener implements BaseQuickAdapter.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
            Type typeToDelete = (Type) (adapter.getData().get(position));
            int type = typeToDelete.getType();

            ConfirmDialog.builder(SettingActivity.this)
                    .setTitle(R.string.setting_delete_type)
                    .setMessage(R.string.setting_delete_hint)
                    .setConfirmText(R.string.setting_delete)
                    .setCancelText(R.string.cancel)
                    .setConfirmListener(() -> {
                        //先删除相关的记录，并删除类型
                        if (RecordDao.deleteRecordByTypeName(typeToDelete.getName()) >= 0
                                && TypeDao.deleteType(typeToDelete.getId()) == 1) {
                            ToastUtils.showShort(R.string.setting_delete_succeed);
                            getAdapter(type).remove(position);
                        }
                        return true;
                    }).build().show();
            return false;
        }
    }
}
