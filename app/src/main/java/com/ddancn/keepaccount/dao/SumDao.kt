package com.ddancn.keepaccount.dao

import com.ddancn.keepaccount.constant.TypeEnum
import com.ddancn.keepaccount.entity.Record
import org.litepal.LitePal

/**
 * @author ddan.zhuang
 * @date 2023/5/30
 */
object SumDao {

    /**
     * 计算一个月/一年的总收入、总支出、总计
     *
     * @param date 日期：月份/年份
     * @return List：0->总收入、1->总支出、2->总计
     */
    fun calMonthOrYearSum(date: String): List<Double> {
        val inSum = LitePal.where(
            "type is ? and date like ? ",
            TypeEnum.IN.value().toString(), "$date%"
        )
            .sum(Record::class.java, "money", Double::class.java)
        val outSum = LitePal.where(
            "type is ? and date like ? ",
            TypeEnum.OUT.value().toString(), "$date%"
        )
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
        val cursor = LitePal.findBySQL(
            "select categoryname, sum(money) as type_sum from record " +
                    "where type is ? and date like ? group by categoryName",
            type.toString(), "$date%"
        )
        if (cursor.moveToFirst()) {
            do {
                val nameIndex = cursor.getColumnIndex("categoryname")
                val sumIndex = cursor.getColumnIndex("type_sum")
                if (nameIndex >= 0 && sumIndex >= 0) {
                    val name = cursor.getString(nameIndex)
                    val sum = cursor.getInt(sumIndex).toDouble()
                    typeSum[name] = sum
                }
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
        val cursor = LitePal.findBySQL(
            if (date.length > yearLength) daySql else monthSql,
            type.toString(), "$date%"
        )
        if (cursor.moveToFirst()) {
            do {
                val dIndex = cursor.getColumnIndex("new_date")
                val sIndex = cursor.getColumnIndex("sum")
                if (dIndex >= 0 && sIndex >= 0) {
                    val d = cursor.getString(dIndex)
                    val s = cursor.getInt(sIndex).toDouble()
                    sum[d] = s
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return sum
    }
}