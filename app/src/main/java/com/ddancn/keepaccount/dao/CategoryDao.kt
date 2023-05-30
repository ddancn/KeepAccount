package com.ddancn.keepaccount.dao

import com.ddancn.keepaccount.entity.Category
import com.ddancn.keepaccount.exception.CategoryNameDuplicateException

import org.litepal.LitePal

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
object CategoryDao {

    /**
     * 根据收支返回类型
     * @param type 类型1收入/-1支出
     */
    fun getCategoriesByType(type: Int): List<Category> {
        return LitePal
                .where("type = ?", type.toString())
                .find(Category::class.java)
    }

    /**
     * 添加类型
     * @param name 类型名称
     * @param type 类型1收入/-1支出
     */
    @Throws(CategoryNameDuplicateException::class)
    fun addCategory(name: String, type: Int): Boolean {
        if (checkIfExist(name, type)) {
            throw CategoryNameDuplicateException()
        }
        val addCategory = Category(name = name, type = type)
        return addCategory.save()
    }

    /**
     * 修改类型名称
     * @param id 要修改的类型id
     * @param name 新名称
     */
    @Throws(CategoryNameDuplicateException::class)
    fun updateCategoryName(id: Int, name: String): Int {
        val categoriesById = LitePal.where("id = ?", id.toString()).find(Category::class.java)
        if (categoriesById.isNotEmpty()) {
            val type = categoriesById[0].type
            if (checkIfExist(name, type)) {
                throw CategoryNameDuplicateException()
            }
        }
        val updateType = Category(name = name)
        return updateType.update(id.toLong())
    }

    /**
     * 删除类型
     * @param id 要删除的类型id
     */
    fun deleteCategory(id: Int): Int {
        return LitePal.delete(Category::class.java, id.toLong())
    }

    /**
     * 检查某类型名是否已经存在
     * @param name 名字
     * @param type 类型1收入/-1支出
     * @return 是否存在
     */
    private fun checkIfExist(name: String, type: Int): Boolean {
        return LitePal.isExist(Category::class.java, "name = ? and type = ?", name, type.toString())
    }

}
