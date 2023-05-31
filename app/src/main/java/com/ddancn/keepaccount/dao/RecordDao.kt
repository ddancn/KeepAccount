package com.ddancn.keepaccount.dao

import com.ddancn.keepaccount.entity.Record
import org.litepal.LitePal

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
object RecordDao {

    /**
     * 添加记录
     */
    fun addRecord(record: Record): Boolean {
        return record.save()
    }

    /**
     * 修改记录
     */
    fun updateRecord(record: Record): Boolean {
        return record.update(record.id.toLong()) == 1
    }

    /**
     * 搜索记录
     * @param query 关键词
     */
    fun searchRecord(query: String): List<Record> {
        val condition = "%$query%"
        return LitePal
            .where(
                "date like ? or money like ? or detail like ? or categoryName like ?",
                condition, condition, condition, condition
            )
            .order("date")
            .find(Record::class.java)
            .reversed()
    }

    /**
     * 按月获取记录
     * @param month 月份，格式yyyy-MM
     */
    fun getRecordsByMonth(month: String): List<Record> {
        return LitePal
            .where("date like ?", "$month%")
            .order("date")
            .find(Record::class.java)
            .reversed()
    }

    /**
     * 删除记录
     * @param id 要删除的记录id
     */
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
    fun updateRecordCategoryName(newName: String, oldName: String?): Int {
        val record = Record(categoryName = newName)
        return record.updateAll("categoryName is ?", oldName)
    }

    /**
     * 删除某类型的记录，删除类型时调用
     *
     * @param categoryName 类型名
     * @return int
     */
    fun deleteRecordByCategoryName(categoryName: String?): Int {
        return LitePal.deleteAll(Record::class.java, "categoryName is ?", categoryName)
    }
}
