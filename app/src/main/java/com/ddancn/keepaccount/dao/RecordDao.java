package com.ddancn.keepaccount.dao;

import android.database.Cursor;

import com.ddancn.keepaccount.constant.TypeEnum;
import com.ddancn.keepaccount.entity.Record;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 更新记录的类型名，更新类型的名称时调用
     * @param newName 新类型名
     * @param oldName 旧类型名
     * @return int
     */
    public static int updateRecordTypeName(String newName, String oldName) {
        Record record = new Record();
        record.setTypeName(newName);
        return record.updateAll("typeName is ?", oldName);
    }

    /**
     * 删除某类型的记录，删除类型时调用
     * @param typeName 类型名
     * @return int
     */
    public static int deleteRecordByTypeName(String typeName) {
        return LitePal.deleteAll(Record.class, "typeName is ?", typeName);
    }

    /**
     * 计算一个月的总收入、总支出、总计
     * @param month 月份
     * @return List：0->总收入、1->总支出、2->总计
     */
    public static List<Double> calMonthSum(String month){
        Double inSum = LitePal.where("type is ? and date like ? ", String.valueOf(TypeEnum.IN.value()), month + "%").sum(Record.class, "money", Double.class);
        Double outSum = LitePal.where("type is ? and date like ? ", String.valueOf(TypeEnum.OUT.value()), month + "%").sum(Record.class, "money", Double.class);
        Double sum = inSum - outSum;
        List<Double> result = new ArrayList<>(3);
        result.add(inSum);
        result.add(outSum);
        result.add(sum);
        return result;
    }

    /**
     * 计算一个月中每个类型的总值
     * @param type 类型
     * @param month 月份
     * @return 键值对：类型名->总值
     */
    public static Map<String, Double> calTypeSum(int type, String month){
        Map<String, Double> typeSum = new LinkedHashMap<>();
        Cursor cursor = LitePal.findBySQL("select typename, sum(money) as type_sum from record " +
                        "where type is ? and date like ? group by typeName",
                String.valueOf(type), month + "%");

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("typename"));
                double sum = cursor.getInt(cursor.getColumnIndex("type_sum"));
                typeSum.put(name, sum);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return typeSum;
    }

    /**
     * 计算一个月中每日的总值
     * @param type 类型
     * @param month 月份
     * @return 键值对：日期->总值
     */
    public static Map<String, Double> calDailySum(int type, String month) {
        Map<String, Double> dailySum = new LinkedHashMap<>();
        Cursor cursor = LitePal.findBySQL("select date, sum(money) as daily_sum from record " +
                        "where type is ? and date like ? group by date order by date",
                String.valueOf(type), month + "%");

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                double sum = cursor.getInt(cursor.getColumnIndex("daily_sum"));
                dailySum.put(date, sum);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dailySum;
    }
}
