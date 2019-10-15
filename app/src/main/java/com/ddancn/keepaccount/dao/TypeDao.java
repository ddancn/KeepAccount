package com.ddancn.keepaccount.dao;

import com.ddancn.keepaccount.entity.Type;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
public class TypeDao {
    private TypeDao() {
    }

    public static List<Type> getTypesByType(int type) {
        return LitePal
                .where("type = ?", String.valueOf(type))
                .find(Type.class);
    }

    public static boolean addType(String name, int type){
        Type addType = new Type();
        addType.setName(name);
        addType.setType(type);
        return addType.save();
    }

    public static int updateTypeName(int id, String name){
        Type updateType = new Type();
        updateType.setName(name);
        return updateType.update(id);
    }

    public static int deleteType(int id){
        return LitePal.delete(Type.class, id);
    }
}
