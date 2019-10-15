package com.ddancn.keepaccount.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ddancn.keepaccount.Constant;
import com.ddancn.keepaccount.R;
import com.ddancn.keepaccount.adapter.TypeAdapter;
import com.ddancn.keepaccount.dialog.NormalDialog;
import com.ddancn.keepaccount.entity.Record;
import com.ddancn.keepaccount.entity.Type;
import com.ddancn.keepaccount.util.ToastUtil;
import com.joanzapata.iconify.widget.IconTextView;

import org.litepal.LitePal;

import java.util.List;

public class SettingActivity extends AppCompatActivity
        implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {

    IconTextView iconBack;
    IconTextView iconAddIn;
    IconTextView iconAddOut;

    RecyclerView rvTypeIn;
    RecyclerView rvTypeOut;

    private List<Type> inTypes;
    private List<Type> outTypes;
    private TypeAdapter inTypesAdapter;
    private TypeAdapter outTypesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        iconBack = findViewById(R.id.btn_back);
        iconAddIn = findViewById(R.id.icon_add_in);
        iconAddOut = findViewById(R.id.icon_add_out);
        rvTypeIn = findViewById(R.id.rv_type_in);
        rvTypeOut = findViewById(R.id.rv_type_out);

        iconBack.setOnClickListener(v -> this.finish());
        iconAddIn.setOnClickListener(v -> addType(Constant.TYPE_IN));
        iconAddOut.setOnClickListener(v -> addType(Constant.TYPE_OUT));

        GridLayoutManager manager1 = new GridLayoutManager(this, 3);
        GridLayoutManager manager2 = new GridLayoutManager(this, 3);
        rvTypeIn.setLayoutManager(manager1);
        rvTypeOut.setLayoutManager(manager2);

        inTypes = LitePal.where("type is ?", String.valueOf(Constant.TYPE_IN)).find(Type.class);
        inTypesAdapter = new TypeAdapter(R.layout.item_type, inTypes);
        inTypesAdapter.setOnItemClickListener(this);
        inTypesAdapter.setOnItemLongClickListener(this);
        rvTypeIn.setAdapter(inTypesAdapter);

        outTypes = LitePal.where("type is ?", String.valueOf(Constant.TYPE_OUT)).find(Type.class);
        outTypesAdapter = new TypeAdapter(R.layout.item_type, outTypes);
        outTypesAdapter.setOnItemClickListener(this);
        outTypesAdapter.setOnItemLongClickListener(this);
        rvTypeOut.setAdapter(outTypesAdapter);

    }

    private TypeAdapter getAdapter(int type){
        return (type == Constant.TYPE_IN) ? inTypesAdapter : outTypesAdapter;
    }

    private List<Type> getList(int type){
        return (type == Constant.TYPE_IN) ? inTypes : outTypes;
    }

    private void addType(int type) {
        NormalDialog dialog = new NormalDialog(this);
        dialog.setTitle("添加类型")
                .setOnConfirmClickListenerWithInput("添加", typeName -> {
                    if (TextUtils.isEmpty(typeName)) {
                        ToastUtil.show("没有输入类型的名称哦");
                        return false;
                    }
                    Type addType = new Type();
                    addType.setName(typeName);
                    addType.setType(type);
                    if (addType.save()) {
                        ToastUtil.show("添加成功");
                        getAdapter(type).addData(addType);
                    } else {
                        ToastUtil.show("添加失败");
                    }
                    return true;
                })
                .setOnCancelClickListener("取消", null);
        dialog.show();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Type typeToUpdate = (Type) (adapter.getData().get(position));
        int type = typeToUpdate.getType();

        NormalDialog dialog = new NormalDialog(this);
        dialog.setTitle("修改类型")
                .setEditText(typeToUpdate.getName())
                .setOnConfirmClickListenerWithInput("修改", typeName -> {
                    if (TextUtils.isEmpty(typeName)) {
                        ToastUtil.show("没有输入类型的名称哦");
                        return false;
                    }
                    List<Type> types = getList(type);
                    //修改类型
                    Type updateType = new Type();
                    updateType.setName(typeName);
                    updateType.update(types.get(position).getId());
                    //更新类型相关的记录
                    Record record = new Record();
                    record.setTypeName(typeName);
                    record.updateAll("typeName is ?", types.get(position).getName());

                    ToastUtil.show("修改成功");
                    getAdapter(type).getData().get(position).setName(typeName);
                    getAdapter(type).notifyItemChanged(position);
                    return true;
                })
                .setOnCancelClickListener("取消", null);
        dialog.show();
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        Type typeToDelete = (Type) (adapter.getData().get(position));
        int type = typeToDelete.getType();

        NormalDialog dialog = new NormalDialog(this);
        dialog.setTitle("删除类型")
                .setMsg("删除类型会删除所有与之相关的记录，确定要继续吗？")
                .setOnConfirmClickListener("删除", () -> {
                    List<Type> types = getList(type);
                    //先删除相关的记录
                    LitePal.deleteAll(Record.class, "typeName is ?", types.get(position).getName());
                    //删除类型
                    LitePal.delete(Type.class, types.get(position).getId());

                    ToastUtil.show("删除成功");
                    getAdapter(type).remove(position);
                    return true;
                })
                .setOnCancelClickListener("取消", null);
        dialog.show();
        return false;
    }

}
