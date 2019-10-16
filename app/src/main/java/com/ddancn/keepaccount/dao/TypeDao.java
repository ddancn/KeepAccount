package com.ddancn.keepaccount.dao;

import com.ddancn.keepaccount.entity.Type;
import com.ddancn.keepaccount.exception.TypeNameDuplicateException;

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

    public static boolean addType(String name, int type) throws TypeNameDuplicateException {
        if (checkIfExist(name, type)){
            throw new TypeNameDuplicateException();
        }
        Type addType = new Type();
        addType.setName(name);
        addType.setType(type);
        return addType.save();
    }

    public static int updateTypeName(int id, String name) throws TypeNameDuplicateException {
        List<Type> typeById = LitePal.where("id = ?", String.valueOf(id)).find(Type.class);
        if (!typeById.isEmpty()){
            int type = typeById.get(0).getType();
            if (checkIfExist(name, type)){
                throw new TypeNameDuplicateException();
            }
        }
        Type updateType = new Type();
        updateType.setName(name);
        return updateType.update(id);
    }

    public static int deleteType(int id){
        return LitePal.delete(Type.class, id);
    }

    public static boolean checkIfExist(String name, int type){
        return LitePal.isExist(Type.class, "name = ? and type = ?", name, String.valueOf(type));
    }


}
