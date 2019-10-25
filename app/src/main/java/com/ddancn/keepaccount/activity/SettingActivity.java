package com.ddancn.keepaccount.activity;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.adapter.TypeAdapter;
import com.ddancn.keepaccount.constant.TypeEnum;
import com.ddancn.keepaccount.dao.TypeDao;
import com.ddancn.keepaccount.entity.Type;
import com.ddancn.lib.base.BaseActivity;
import com.ddancn.lib.util.ViewUtilKt;

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
        inTypesAdapter.setEmptyView(ViewUtilKt.getEmptyTextView(getString(R.string.setting_no_in_type)));
        rvTypeIn.setAdapter(inTypesAdapter);
        outTypesAdapter = new TypeAdapter(R.layout.item_type);
        outTypesAdapter.setEmptyView(ViewUtilKt.getEmptyTextView(getString(R.string.setting_no_out_type)));
        rvTypeOut.setAdapter(outTypesAdapter);
    }

    @Override
    protected void bindListener() {
        iconAddIn.setOnClickListener(v -> addType(TypeEnum.IN.value()));
        iconAddOut.setOnClickListener(v -> addType(TypeEnum.OUT.value()));
        inTypesAdapter.setOnItemClickListener(new TypeItemClickItemListener());
        inTypesAdapter.setOnItemLongClickListener(new TypeItemLongClickListener());
        outTypesAdapter.setOnItemClickListener(new TypeItemClickItemListener());
        outTypesAdapter.setOnItemLongClickListener(new TypeItemLongClickListener());
    }

    @Override
    protected void applyData() {
        inTypesAdapter.setNewData(TypeDao.getTypesByType(TypeEnum.IN.value()));
        outTypesAdapter.setNewData(TypeDao.getTypesByType(TypeEnum.OUT.value()));
    }

    private TypeAdapter getAdapter(int type) {
        return (type == TypeEnum.IN.value()) ? inTypesAdapter : outTypesAdapter;
    }

    private void addType(int type) {
//        InputDialog.builder(this)
//                .setTitle(R.string.setting_add_type)
//                .setConfirmText(R.string.confirm)
//                .setCancelText(R.string.cancel)
//                .setConfirmListener(input -> {
//                    if (TextUtils.isEmpty(input)) {
//                        ToastUtils.showShort(R.string.setting_need_type_name);
//                        return false;
//                    }
//                    try {
//                        if (TypeDao.addType(input, type)) {
//                            ToastUtils.showShort(R.string.setting_add_succeed);
//                            applyData();
//                        } else {
//                            ToastUtils.showShort(R.string.setting_add_fail);
//                        }
//                    } catch (TypeNameDuplicateException e){
//                        toast(e.getMessage());
//                    }
//
//                    return true;
//                }).build().show();
    }

    class TypeItemClickItemListener implements BaseQuickAdapter.OnItemClickListener {

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Type typeToUpdate = (Type) (adapter.getData().get(position));
            int type = typeToUpdate.getType();

//            InputDialog.builder(SettingActivity.this)
//                    .setTitle(R.string.setting_update_type)
//                    .setConfirmText(R.string.setting_update)
//                    .setCancelText(R.string.cancel)
//                    .setEtMsg(typeToUpdate.getName())
//                    .setConfirmListener(input -> {
//                        if (TextUtils.isEmpty(input)) {
//                            ToastUtils.showShort(R.string.setting_need_type_name);
//                            return false;
//                        }
//                        //修改类型，更新类型相关的记录
//                        try {
//                            if (TypeDao.updateTypeName(typeToUpdate.getId(), input) == 1
//                                    && RecordDao.updateRecordTypeName(input, typeToUpdate.getName()) >= 0) {
//                                ToastUtils.showShort(R.string.setting_update_succeed);
//                                getAdapter(type).getData().get(position).setName(input);
//                                getAdapter(type).notifyItemChanged(position);
//                            }
//                        } catch (TypeNameDuplicateException e){
//                            toast(e.getMessage());
//                        }
//                        return true;
//                    }).build().show();
        }
    }

    class TypeItemLongClickListener implements BaseQuickAdapter.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
            Type typeToDelete = (Type) (adapter.getData().get(position));
            int type = typeToDelete.getType();

//            ConfirmDialog.builder(SettingActivity.this)
//                    .setTitle(R.string.setting_delete_type)
//                    .setMessage(R.string.setting_delete_hint)
//                    .setConfirmText(R.string.setting_delete)
//                    .setCancelText(R.string.cancel)
//                    .setConfirmListener(() -> {
//                        //先删除相关的记录，并删除类型
//                        if (RecordDao.deleteRecordByTypeName(typeToDelete.getName()) >= 0
//                                && TypeDao.deleteType(typeToDelete.getId()) == 1) {
//                            ToastUtils.showShort(R.string.setting_delete_succeed);
//                            getAdapter(type).remove(position);
//                        }
//                        return true;
//                    }).build().show();
            return false;
        }
    }
}
