package com.ddancn.keepaccount.constant

/**
 * @author ddan.zhuang
 * @date 2019/10/16
 */
enum class TypeEnum constructor(private val value: Int) {
    /**
     * 收入
     */
    IN(1),
    /**
     * 支出
     */
    OUT(-1);

    fun value(): Int {
        return value
    }
}
