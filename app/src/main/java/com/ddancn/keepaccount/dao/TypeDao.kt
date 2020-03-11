package com.ddancn.keepaccount.dao

import com.ddancn.keepaccount.entity.Type
import com.ddancn.keepaccount.exception.TypeNameDuplicateException

import org.litepal.LitePal

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
object TypeDao {

    /**
     * 根据收支返回类型
     * @param type 类型1收入/-1支出
     */
    fun getTypesByType(type: Int): List<Type> {
        return LitePal
                .where("type = ?", type.toString())
                .find(Type::class.java)
    }

    /**
     * 添加类型
     * @param name 类型名称
     * @param type 类型1收入/-1支出
     */
    @Throws(TypeNameDuplicateException::class)
    fun addType(name: String, type: Int): Boolean {
        if (checkIfExist(name, type)) {
            throw TypeNameDuplicateException()
        }
        val addType = Type(name = name, type = type)
        return addType.save()
    }

    /**
     * 修改类型名称
     * @param id 要修改的类型id
     * @param name 新名称
     */
    @Throws(TypeNameDuplicateException::class)
    fun updateTypeName(id: Int, name: String): Int {
        val typesById = LitePal.where("id = ?", id.toString()).find(Type::class.java)
        if (typesById.isNotEmpty()) {
            val type = typesById[0].type
            if (checkIfExist(name, type)) {
                throw TypeNameDuplicateException()
            }
        }
        val updateType = Type(name = name)
        return updateType.update(id.toLong())
    }

    /**
     * 删除类型
     * @param id 要删除的类型id
     */
    fun deleteType(id: Int): Int {
        return LitePal.delete(Type::class.java, id.toLong())
    }

    /**
     * 检查某类型名是否已经存在
     * @param name 名字
     * @param type 类型1收入/-1支出
     * @return 是否存在
     */
    private fun checkIfExist(name: String, type: Int): Boolean {
        return LitePal.isExist(Type::class.java, "name = ? and type = ?", name, type.toString())
    }

}
