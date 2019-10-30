package com.ddancn.keepaccount.dao

import com.ddancn.keepaccount.entity.Type
import com.ddancn.keepaccount.exception.TypeNameDuplicateException

import org.litepal.LitePal

/**
 * @author ddan.zhuang
 * @date 2019/10/15
 */
object TypeDao {

    fun getTypesByType(type: Int): List<Type> {
        return LitePal
                .where("type = ?", type.toString())
                .find(Type::class.java)
    }

    @Throws(TypeNameDuplicateException::class)
    fun addType(name: String, type: Int): Boolean {
        if (checkIfExist(name, type)) {
            throw TypeNameDuplicateException()
        }
        val addType = Type(name = name, type = type)
        return addType.save()
    }

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

    fun deleteType(id: Int): Int {
        return LitePal.delete(Type::class.java, id.toLong())
    }

    /**
     * 检查某类型名是否已经存在
     * @param name 名字
     * @param type 类型1/-1
     * @return 是否存在
     */
    private fun checkIfExist(name: String, type: Int): Boolean {
        return LitePal.isExist(Type::class.java, "name = ? and type = ?", name, type.toString())
    }

}
