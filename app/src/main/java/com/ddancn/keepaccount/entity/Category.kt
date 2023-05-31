package com.ddancn.keepaccount.entity

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

/**
 * @author ddan.zhuang
 */
data class Category(
    var id: Int = 0,
    var name: String = "",
    var type: Int = 1,
    @Column(ignore = true) var isSelected: Boolean = false
) : LitePalSupport()
