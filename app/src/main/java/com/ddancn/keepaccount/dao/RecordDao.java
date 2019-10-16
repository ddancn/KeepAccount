package com.ddancn.keepaccount.dao;

import com.ddancn.keepaccount.entity.Record;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
public class RecordDao {

    private RecordDao() {
    }

    public static boolean addOrUpdateRecord(boolean isUpdate, int idToUpdate,
                                            String date, double money, String detail, int type, String typeName) {
        Record record = new Record();
        record.setDate(date);
        record.setMoney(money);
        record.setDetail(detail);
        record.setType(type);
        record.setTypeName(typeName);
        return isUpdate ? record.update(idToUpdate) == 1 : record.save();
    }

    public static List<Record> searchRecord(String query) {
        String condition = "%" + query + "%";
        return LitePal
                .where("date like ? or money like ? or detail like ? or typeName like ?",
                        condition, condition, condition, condition)
                .order("date desc")
                .find(Record.class);
    }

    public static List<Record> getRecordsByMonth(String month) {
        return LitePal
                .where("date like ?", month + "%")
                .order("date desc")
                .find(Record.class);
    }

    public static int deleteRecordById(int id) {
        return LitePal.delete(Record.class, id);
    }

    public static List<Record> getRecordByTypeAndMonth(int type, String month) {
        return LitePal
                .where("date like ? and type is ?",
                        month + "%", String.valueOf(type))
                .find(Record.class);
    }

    public static List<Record> getRecordByTypeAndMonth(int type, String typeName, String month) {
        return LitePal
                .select("money")
                .where("date like ? and typeName is ? and type is ?",
                        month + "%", typeName, String.valueOf(type))
                .find(Record.class);
    }

    public static int updateRecordTypeName(String newName, String oldName) {
        Record record = new Record();
        record.setTypeName(newName);
        return record.updateAll("typeName is ?", oldName);
    }

    public static int deleteRecordByTypeName(String typeName) {
        return LitePal.deleteAll(Record.class, "typeName is ?", typeName);
    }
}
