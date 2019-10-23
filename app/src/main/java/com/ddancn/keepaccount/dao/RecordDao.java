package com.ddancn.keepaccount.dao;

import android.database.Cursor;

import com.ddancn.keepaccount.constant.TypeEnum;
import com.ddancn.keepaccount.entity.Record;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
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
        List<Record> result = LitePal
                .where("date like ? or money like ? or detail like ? or typeName like ?",
                        condition, condition, condition, condition)
                .order("date desc")
                .find(Record.class);
        Collections.reverse(result);
        return result;
    }

    public static List<Record> getRecordsByMonth(String month) {
        List<Record> result = LitePal
                .where("date like ?", month + "%")
                .find(Record.class);
        Collections.reverse(result);
        return result;
    }

    public static int deleteRecordById(int id) {
        return LitePal.delete(Record.class, id);
    }

    /**
     * 更新记录的类型名，更新类型的名称时调用
     *
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
     *
     * @param typeName 类型名
     * @return int
     */
    public static int deleteRecordByTypeName(String typeName) {
        return LitePal.deleteAll(Record.class, "typeName is ?", typeName);
    }

    /**
     * 计算一个月/一年的总收入、总支出、总计
     *
     * @param date 日期：月份/年份
     * @return List：0->总收入、1->总支出、2->总计
     */
    public static List<Double> calMonthOrYearSum(String date) {
        Double inSum = LitePal.where("type is ? and date like ? ", String.valueOf(TypeEnum.IN.value()), date + "%").sum(Record.class, "money", Double.class);
        Double outSum = LitePal.where("type is ? and date like ? ", String.valueOf(TypeEnum.OUT.value()), date + "%").sum(Record.class, "money", Double.class);
        Double sum = inSum - outSum;
        List<Double> result = new ArrayList<>(3);
        result.add(inSum);
        result.add(outSum);
        result.add(sum);
        return result;
    }

    /**
     * 计算一个月/一年中每个类型的总值
     *
     * @param type 类型
     * @param date 日期：月份/年份
     * @return 键值对：类型名->总值
     */
    public static Map<String, Double> calTypeSum(int type, String date) {
        Map<String, Double> typeSum = new LinkedHashMap<>();
        Cursor cursor = LitePal.findBySQL("select typename, sum(money) as type_sum from record " +
                        "where type is ? and date like ? group by typeName",
                String.valueOf(type), date + "%");

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
     * 计算一个月中每日/一年中每月的总值
     *
     * @param type 类型
     * @param date 日期：月份/年份
     * @return 键值对：日期->总值
     */
    public static Map<String, Double> calDayOrMonthSum(int type, String date) {
        Map<String, Double> sum = new LinkedHashMap<>();
        // date格式yyyy-MM-dd
        final int yearLength = 4;
        // 月中的每日
        String daySql = "select substr(date,9,2) as new_date, sum(money) as sum from record " +
                "where type is ? and date like ? group by date order by date";
        // 年中的每月
        String monthSql = "select substr(date,6,2) as new_date, sum(money) as sum from record " +
                "where type is ? and date like ? group by new_date order by new_date";
        Cursor cursor = LitePal.findBySQL(date.length() > yearLength ? daySql : monthSql,
                String.valueOf(type), date + "%");
        if (cursor.moveToFirst()) {
            do {
                String d = cursor.getString(cursor.getColumnIndex("new_date"));
                double s = cursor.getInt(cursor.getColumnIndex("sum"));
                sum.put(d, s);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sum;
    }


}
