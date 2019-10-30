package com.ddancn.keepaccount.dao

import com.ddancn.keepaccount.constant.TypeEnum
import com.ddancn.keepaccount.entity.Record
import org.litepal.LitePal
import java.util.*

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
object RecordDao {

    fun addOrUpdateRecord(isUpdate: Boolean, idToUpdate: Int,
                          date: String, money: Double, detail: String, type: Int, typeName: String): Boolean {
        val record = Record(date = date, money = money, detail = detail, type = type, typeName = typeName)
        return if (isUpdate) record.update(idToUpdate.toLong()) == 1 else record.save()
    }

    fun searchRecord(query: String): List<Record> {
        val condition = "%$query%"
        val result = LitePal
                .where("date like ? or money like ? or detail like ? or typeName like ?",
                        condition, condition, condition, condition)
                .order("date desc")
                .find(Record::class.java)
        result.reverse()
        return result
    }

    fun getRecordsByMonth(month: String): List<Record> {
        val result = LitePal
                .where("date like ?", "$month%")
                .order("date")
                .find(Record::class.java)
        result.reverse()
        return result
    }

    fun deleteRecordById(id: Int): Int {
        return LitePal.delete(Record::class.java, id.toLong())
    }

    /**
     * 更新记录的类型名，更新类型的名称时调用
     *
     * @param newName 新类型名
     * @param oldName 旧类型名
     * @return int
     */
    fun updateRecordTypeName(newName: String, oldName: String?): Int {
        val record = Record(typeName = newName)
        return record.updateAll("typeName is ?", oldName)
    }

    /**
     * 删除某类型的记录，删除类型时调用
     *
     * @param typeName 类型名
     * @return int
     */
    fun deleteRecordByTypeName(typeName: String?): Int {
        return LitePal.deleteAll(Record::class.java, "typeName is ?", typeName)
    }

    /**
     * 计算一个月/一年的总收入、总支出、总计
     *
     * @param date 日期：月份/年份
     * @return List：0->总收入、1->总支出、2->总计
     */
    fun calMonthOrYearSum(date: String): List<Double> {
        val inSum = LitePal.where("type is ? and date like ? ",
                TypeEnum.IN.value().toString(), "$date%")
                .sum(Record::class.java, "money", Double::class.java)
        val outSum = LitePal.where("type is ? and date like ? ",
                TypeEnum.OUT.value().toString(), "$date%")
                .sum(Record::class.java, "money", Double::class.java)
        val sum = inSum - outSum
        return listOf(inSum, outSum, sum)
    }

    /**
     * 计算一个月/一年中每个类型的总值
     *
     * @param type 类型
     * @param date 日期：月份/年份
     * @return 键值对：类型名->总值
     */
    fun calTypeSum(type: Int, date: String): Map<String, Double> {
        val typeSum = LinkedHashMap<String, Double>()
        val cursor = LitePal.findBySQL("select typename, sum(money) as type_sum from record " +
                "where type is ? and date like ? group by typeName",
                type.toString(), "$date%")
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex("typename"))
                val sum = cursor.getInt(cursor.getColumnIndex("type_sum")).toDouble()
                typeSum[name] = sum
            } while (cursor.moveToNext())
        }
        cursor.close()
        return typeSum
    }

    /**
     * 计算一个月中每日/一年中每月的总值
     *
     * @param type 类型
     * @param date 日期：月份/年份
     * @return 键值对：日期->总值
     */
    fun calDayOrMonthSum(type: Int, date: String): Map<String, Double> {
        val sum = LinkedHashMap<String, Double>()
        // date格式yyyy-MM-dd
        val yearLength = 4
        // 月中的每日
        val daySql = "select substr(date,9,2) as new_date, sum(money) as sum from record " +
                "where type is ? and date like ? group by date order by date"
        // 年中的每月
        val monthSql = "select substr(date,6,2) as new_date, sum(money) as sum from record " +
                "where type is ? and date like ? group by new_date order by new_date"
        val cursor = LitePal.findBySQL(if (date.length > yearLength) daySql else monthSql,
                type.toString(), "$date%")
        if (cursor.moveToFirst()) {
            do {
                val d = cursor.getString(cursor.getColumnIndex("new_date"))
                val s = cursor.getInt(cursor.getColumnIndex("sum")).toDouble()
                sum[d] = s
            } while (cursor.moveToNext())
        }
        cursor.close()
        return sum
    }

}
